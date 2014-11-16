package main.project;

import java.util.HashSet;
import java.util.Set;
import org.javatuples.Pair;
import workshop.tools.deobfuscator.AnalysisResult;
import workshop.tools.deobfuscator.Analyzer;
import workshop.tools.deobfuscator.JSDeobfuscator;

public class DeobfuscatorAnalysis {
	/*number of iterations allowed for the evaluation to loop*/
	private static final int DEOBFUSCATOR_ITERATIONS = 5000;

	public static Pair<String, Set<String>> generateReport(String path) {
		String r1 = "";
		String r2 = "";
		String r3 = "";
		String r4 = "";
		
		Set<String> urls = new HashSet<String>();
		
		try {
			JSDeobfuscator.setIterations(DEOBFUSCATOR_ITERATIONS);
			AnalysisResult analysis = Analyzer.analyze(path);

			if(analysis.createsIframe()){
				r1 = "Creates iframe\n";
			}

			if(analysis.hasHiddenFrame()){
				r2 = "Has Hidden iframe\n";
			}

			if(analysis.writesScript()){
				r3 = "writes a script\n";
			}

			urls = analysis.getUrls();

			if(!urls.isEmpty()){
				
				r4 = "URLs Found:\n";
				for (String url : urls) {
					r4+= url+"\n";
				}
			}


		} catch (Exception e) {
			return new Pair<String, Set<String>>("Failed To Generate Report\n", urls); 
		}

		String result = r1+r2+r3+r4;

		if(result.isEmpty()){
			return new Pair<String, Set<String>>("Nothing To Report!\n", urls);
		}

		return new Pair<String, Set<String>>(result,urls);
	}



}
