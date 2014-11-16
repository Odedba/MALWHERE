package main.project;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import org.javatuples.Pair;
import workshop.tools.FileToString;
import workshop.tools.RhinoASTBuilder;
import workshop.tools.URLExtractor;
import workshop.tools.VirustotalAnalyzer;


public class Run {
	private static File inputFile = null;
	private static String javascriptCode = "";
	private static Set<String> urls = null;
	/*value assigned to malicious JS code*/
	private static final int MALICIOUS = -1;
	/*value assigned to suspicious JS code*/
	private static final int UNKNOWN = 0;
	/*value assigned to harmless JS code*/
	private static final int BENIGN = 1;
	
	
	public static void readFile(File file) throws IOException{
		inputFile = file;
		RhinoASTBuilder.getAST(inputFile.getPath());
		javascriptCode = FileToString.read(inputFile);
	}

	
	public static String runMinifiedTester() {
		String output = "Testing For Minified Code...\n\n";
		String result = (MinifiedTester.isMinified(javascriptCode) ? "minified\n" : "not minified\n");
		output+= "Test Result For "+inputFile.getName()+": "+result+"\n";	
		return output;
	}
	
	
	public static String runObfuscationTester() {
		String output = "Testing For Obfuscated Code...\n\n";
		String result = (ObfuscationTester.isObfuscated(inputFile.getPath(), javascriptCode) ? "obfuscated\n" : "not obfuscated\n");
		output+= "Test Result For "+inputFile.getName()+": "+result+"\n";
		return output;
	}
	
	
	public static void runMaliciousnessTester(JTextPane outputTxt) {
		String output = "Testing For Malicious Code...\n\n";
		double result;
		try {
			result = MaliciousnessTester.getMalScore(javascriptCode, inputFile);
			output+= "Test Score For "+inputFile.getName()+": "+result;
			
			if(result == BENIGN){
				output+= " (File Was Found To Be Safe)\n";
			}
			else if(result == UNKNOWN){
				output+= " (Test Was Not Conclusive)\n";
			}
			else if(result == MALICIOUS){
				output+= " (Malware Detected!)\n";
			}
			
			outputTxt.setText(output);
			
		} catch (IOException e) {
			outputTxt.setText("An Error Occurred While Running Maliciousness Tester");
		}
	}
	
	
	public static void runDeobfuscatorAnalysis(JTextPane outputTxt, JMenuItem mntmURLInspectionReport) {
		String output = "Generating Deobfuscation Report...\n\n";
		Pair<String, Set<String>> result = DeobfuscatorAnalysis.generateReport(inputFile.getPath());
		output+= "Report For "+inputFile.getName()+":\n"+result.getValue0()+"\n";
		outputTxt.setText(output);
		urls = result.getValue1();
		mntmURLInspectionReport.setEnabled(!result.getValue1().isEmpty());
	}
	
	
	public static void runURLSearch(JTextPane outputTxt, JMenuItem mntmURLInspectionReport){
		outputTxt.setText("Searching Code For URLs...\n\n");
		String result = "";
		try {
			String code = FileToString.read(inputFile);
			Set<String> results = new HashSet<String>(); 
			results.addAll(URLExtractor.extract(code));
			for (String url : results) {
				result+= url+"\n";
			}
			
			if(result.isEmpty()){
				outputTxt.setText("No URLs Found For "+inputFile.getName()+"\n");
			}
			else{
				urls = results;
				mntmURLInspectionReport.setEnabled(true);
				outputTxt.setText("URLs Found:\n"+result);
			}
		} catch (IOException e) {
			outputTxt.setText("An Error Occurred While Trying To Extract URLs");
		}
	}
	
	
	public static String getMessage(){
		return "Please Wait While Calculating Score...";
	}

	
	public static void runURLInspection(JTextPane outputTxt) {
		String result = "VirusTotal's Inspection Of The URLs Found in "+inputFile.getName()+" Produced The Following:\n\n";
		for (String url : urls) {
			String answer = VirustotalAnalyzer.getUrlReport(url)+"\n";
			result+= answer;
			
			if(answer.startsWith("Failed To Generate Report: Exceeded maximum number of requests per minute")){
				break;
			}
		}
		outputTxt.setText(result);
	}
	

}
