package workshop.tools.closure.compiler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.javatuples.Pair;
import workshop.tools.FileToString;
import workshop.tools.StringToFile;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DotFormatter;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;

/**
 *	This class uses the Google Closure Compiler to compile a JS code from a given source file.
 *	<p><b>Description:</b></p>
 *	Compiler (and the other classes in its package) does the following:
 * <ul>
 * <li>parses JS code
 * <li>checks for undefined variables
 * <li>performs optimizations such as constant folding and constants inlining
 * <li>renames variables (to short names)
 * <li>outputs compact JavaScript code
 * </ul>
 *
 * External variables are declared in 'externs' files. For instance, the file
 * may include definitions for global javascript/browser objects such as
 * window, document.</p>
 * @see <a href="https://developers.google.com/closure/compiler/">https://developers.google.com/closure/compiler/</a>
 */
public class ClosureCompiler {
	private static HashMap<String, Pair<Node, ControlFlowGraph<Node>>> cfgFileMap =
			new HashMap<String, Pair<Node, ControlFlowGraph<Node>>>();
	
	
	/**
	 * Runs the google closure compiler on the code in 'sourcePath' at WHITESPACE_ONLY 
	 * compilation level.
	 * 
	 * @param sourcePath - Path to source file.
	 * @param recompile - True if code should be recompiled, false otherwise. 
	 * @return The resulting code.
	 * @throws ClosureCompilerException
	 */
	public static String compile(
			String sourcePath,
			boolean recompile) throws ClosureCompilerException{
		return compile(sourcePath, CompilationLevel.WHITESPACE_ONLY, recompile);
	}


	/**
	 * Runs the google closure compiler on the code in 'sourcePath' at 'level' 
	 * compilation level.
	 * 
	 * @param sourcePath - Path to source file.
	 * @param level - Instance of CompilationLevel.
	 * @param recompile - True if code should be recompiled, false otherwise.
	 * @return the resulting code.
	 * @throws ClosureCompilerException
	 */
	public static String compile(
			String sourcePath, 
			CompilationLevel level,
			boolean recompile) throws ClosureCompilerException{


		//initialization
		Compiler compiler = new Compiler();
		CompilerOptions options = new CompilerOptions();
		SourceFile externs = SourceFile.fromCode("externs.js", "");
		SourceFile input = null;

		//setting compile level
		switch(level){
		case WHITESPACE_ONLY:
			CompilationLevel.WHITESPACE_ONLY.setOptionsForCompilationLevel(options);
			break;
		case SIMPLE_OPTIMIZATIONS:
			CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
			break;
		case ADVANCED_OPTIMIZATIONS:
			CompilationLevel.ADVANCED_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
			break;
		default:
			CompilationLevel.WHITESPACE_ONLY.setOptionsForCompilationLevel(options);
			break;
		}

		//compiling
		input = SourceFile.fromFile(sourcePath);
		compiler.disableThreads();
		if(!compiler.compile(externs, input, options).success){
			throw new ClosureCompilerException("error: closure compiler failed to compile JS code"); 
		}

		//outputting result
		String compiledCode = compiler.toSource();
		return compiledCode;
	}


	/**
	 * Builds the CFG for the given source code.
	 * 
	 * @param sourceCode - Path to JS source file.
	 * @return Pair comprised of the CFG root and graph of Node objects
	 * @throws IOException
	 */
	public static Pair<Node, ControlFlowGraph<Node>> getCfg(String sourceCode) throws IOException{

		//initialization
		Compiler compiler = new Compiler();
		compiler.disableThreads();
		ControlFlowAnalysis analysis = new ControlFlowAnalysis(compiler, true, true);
		Node root = compiler.parseSyntheticCode("cfg", sourceCode);

		//building the CFG
		analysis.process(null, root);
		ControlFlowGraph<Node> graph = analysis.getCfg();

		return new Pair<Node, ControlFlowGraph<Node>>(root, graph);
	}


	/**
	 * Builds the CFG for the source code found at the given path.
	 * 
	 * @param sourcePath - Path to JS source file.
	 * @return Pair comprised of the CFG root and graph of Node objects.
	 * @throws IOException
	 */
	public static Pair<Node, ControlFlowGraph<Node>> getCfgForFileAt(String sourcePath) throws IOException{
		if(cfgFileMap.containsKey(sourcePath)){
			return cfgFileMap.get(sourcePath);
		}

		String code = FileToString.read(new File(sourcePath));
		Pair<Node, ControlFlowGraph<Node>> cfgPair = getCfg(code);

		cfgFileMap.put(sourcePath, cfgPair);

		return cfgPair;
	}


	/**
	 * Builds the CFG for the given source code.
	 * 
	 * @param sourceCode - Path to JS source file.
	 * @return String representation of the CFG in DOT format.
	 * @throws IOException if failed to open file at sourcePath
	 */
	public static String getDotCfg(String sourceCode) throws IOException{
		Pair<Node, ControlFlowGraph<Node>> cfg = getCfg(sourceCode);
		Node root = cfg.getValue0();
		ControlFlowGraph<Node> graph = cfg.getValue1(); 
		String dotCfg = DotFormatter.toDot(root, graph);
		return dotCfg;
	}


	/**
	 * Builds the DOT CFG for the given source code, and outputs to 'targetPath'.
	 * 
	 * @param sourcePath - path to JS source file.
	 * @param sourceCode - JS source code.
	 * @param targetPath - path to target file.
	 * @return String representation of the CFG in DOT format.
	 * @throws IOException if failed to open file at sourcePath, or failed to write to targetPath.
	 */
	public static String outputDotCfgToFile(String sourcePath, String sourceCode, String targetPath) throws IOException{
		String graph = getDotCfg(sourceCode);
		StringToFile.write(graph, targetPath);
		return graph;
	}


}
