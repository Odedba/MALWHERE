package workshop.tools.deobfuscator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.javatuples.Pair;
import workshop.tools.URLExtractor;
import workshop.tools.closure.compiler.ClosureCompiler;
import workshop.tools.closure.compiler.ClosureCompilerException;
import workshop.tools.deobfuscator.DeobfuscatorResult.Type;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.rhino.Node;

public class Analyzer {

	private static HashMap<String, Pair<AnalysisResult, Integer>> analysisResultMap = 
			new HashMap<String, Pair<AnalysisResult, Integer>>();


	/**
	 * <p>Given a path to JavaScript code and the number of iterations allowed for the analysis to execute on loops
	 * inside the code,</p>
	 * <p>Analyzes the results given by JSDeobfuscator for the code found at the given path.</p> 
	 * 
	 * @param sourceCodePath - Path to the source code.
	 * @param iterations - The number of iterations allowed.
	 * @return The results of the analysis as an instance of AnalysisResult. 
	 * @throws FailingHttpStatusCodeException
	 * @throws ClosureCompilerException
	 * @throws IOException
	 */
	public static AnalysisResult analyze(String sourceCodePath) 
			throws FailingHttpStatusCodeException, ClosureCompilerException, IOException{

		//checking if analysis result already exists
		int iterations = JSDeobfuscator.getItrations();

		if(analysisResultMap.containsKey(sourceCodePath)){
			Pair<AnalysisResult, Integer> resultPair = analysisResultMap.get(sourceCodePath);
			if(resultPair.getValue1() == iterations){
				return resultPair.getValue0();
			}
		}

		//initialization
		boolean createsIframe = false;
		boolean hasHiddenFrame = false;
		boolean writesScript = false;
		HashSet<String> urls = new HashSet<String>();

		//running deobfuscator and analyzing results
		JSDeobfuscator.setIterations(iterations);
		HashSet<DeobfuscatorResult> deobfuscatorResults = JSDeobfuscator.deobfuscate(sourceCodePath);

		for (DeobfuscatorResult  result : deobfuscatorResults) {

			//checking if has iframe creation
			createsIframe = (createsIframe || hasIframeCreation(result));

			//checking if has hidden frames
			hasHiddenFrame = (hasHiddenFrame || hasHiddenFrame(result));

			//checking if has script creation
			writesScript = (writesScript || writesScript(result));

			//adding all URLs found in the script
			urls.addAll(URLExtractor.extract(result.getInfo()));

		}

		//saving and returning result
		AnalysisResult result = new AnalysisResult(
				createsIframe,
				hasHiddenFrame,
				writesScript,
				urls);
		analysisResultMap.put(sourceCodePath, new Pair<AnalysisResult, Integer>(result, iterations));

		return result;
	}


	/**
	 * Checks if the given deobfuscator result contains code that writes a script element,
	 * Or assigns JavaScript code to DOM or BOM elements.
	 * 
	 * @param result - The deobfuscator result
	 * @return True if contains code that writes a script, false otherwise.
	 */
	private static boolean writesScript(DeobfuscatorResult result) {
		String info = result.getInfo();
		if(result.getType() == Type.CREATE_ELEMENT && info.contains("script")){
			return true;
		}

		ArrayList<String> elements = extractHTMLElements(info, "script");
		if(!elements.isEmpty()){
			return true;
		}

		if(result.getType() == Type.OBJECT_ASSIGN){
			String[] words = info.trim().split("\\s+");
			if(words.length > 1){

				//handle anonymous function
				if(words[0].equals("function") && words[1].startsWith("(")){
					info = "("+info+")();";
				}

				try {
					Pair<Node, ControlFlowGraph<Node>> cfg = ClosureCompiler.getCfg(info);
					if(cfg.getValue0().children().iterator().hasNext()){
						return true;
					}
				} catch (IOException e) {}
			}
		}

		return false;
	}


	/**
	 * <p>Checks if the given result includes an HTML iframe element that is hidden,</p>
	 * <p>Whether explicitly using the 'hidden' style, or implicitly through low</p>
	 * <p>values 'height' and 'width'.</p>
	 * 
	 * @param result -The deobfuscator result
	 * @return True if has hidden iframe element, false otherwise.
	 */
	private static boolean hasHiddenFrame(DeobfuscatorResult result) {
		String info = result.getInfo();
		ArrayList<String> elements = extractHTMLElements(info, "iframe");
		for (String element : elements) {

			String hiddenRegex = "(style)='.*(visibility)[\\s]*:[\\s]*(hidden);.*'";
			Pattern pattern = Pattern.compile(hiddenRegex);
			Matcher hiddenMatcher = pattern.matcher(element);

			if(hiddenMatcher.find()){
				return true;
			}

			String widthRegex = "(width)[\\s]*=[\\s]*('1?[0-9]'|\"1?[0-9]\"|1?[0-9])";
			pattern = Pattern.compile(widthRegex);
			Matcher widthMatcher = pattern.matcher(element);

			String heightRegex = "(height)[\\s]*=[\\s]*('1?[0-9]'|\"1?[0-9]\"|1?[0-9])";
			pattern = Pattern.compile(heightRegex);
			Matcher  heightMatcher = pattern.matcher(element);

			if(widthMatcher.find() && heightMatcher.find()){
				return true;
			}

		}

		return false;
	}


	/**
	 * Checks if the given deobfuscator result contains a script that creates an iframe,
	 * or indicates such action is performed by the original source code.
	 * 
	 * @param result - the deobfuscator result
	 * @return True if an iframe is created, false otherwise.
	 */
	private static boolean hasIframeCreation(DeobfuscatorResult result) {
		String info = result.getInfo();
		if((result.getType() == Type.CREATE_ELEMENT && info.contains("iframe"))
				|| info.contains("document.createElement('iframe')")
				|| info.contains("document.createElement(\"iframe\")")
				|| result.getType() == Type.DOCUMENT_WRITE && info.contains("iframe")
				|| result.getType() == Type.DOCUMENT_WRITELN && info.contains("iframe")
				|| !extractHTMLElements(info, "iframe").isEmpty()){
			return true;
		}

		return false;
	}


	/**
	 * Given a text and an HTML tag, extracts all the elements bearing that tag from the text.   
	 * 
	 * @param text - The text.
	 * @param tag - the HTML tag.
	 * @return The first element with that tag in the text.
	 */
	private static ArrayList<String> extractHTMLElements(String text, String tag){
		ArrayList<String> elements = new ArrayList<String>();
		String result;
		String regex = ".*(<"+tag+".*>.*</"+tag+">).*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		while(matcher.find()){
			result = matcher.group(1);

			if(!result.isEmpty()){
				elements.add(result);
			}
		}

		return elements;
	}

}
