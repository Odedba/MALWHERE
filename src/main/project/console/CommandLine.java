package main.project.console;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.javatuples.Pair;
import main.project.DeobfuscatorAnalysis;
import main.project.MaliciousnessTester;
import main.project.Run;
import workshop.tools.FileToString;
import workshop.tools.URLExtractor;
import workshop.tools.VirustotalAnalyzer;



public class CommandLine {
	/*value assigned to malicious JS code*/
	private static final int MALICIOUS = -1;
	/*value assigned to suspicious JS code*/
	private static final int UNKNOWN = 0;
	/*value assigned to harmless JS code*/
	private static final int BENIGN = 1;
	private static Set<String> urls = null;


	public static String runMaliciousnessTester(File inputFile) throws IOException {
		String javascriptCode = FileToString.read(inputFile);
		String output = "";
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

			return output;

		} catch (IOException e) {
			return "An Error Occurred While Running Maliciousness Tester\n";
		}
	}


	public static String runURLSearch(File inputFile){
		String result = "";
		try {
			String code = FileToString.read(inputFile);
			Set<String> results = new HashSet<String>(); 
			results.addAll(URLExtractor.extract(code));
			for (String url : results) {
				result+= url+"\n";
			}

			if(result.isEmpty()){
				return "No URLs Found For "+inputFile.getName()+"\n";
			}
			else{
				urls = results;
				return "URLs Found:\n"+result;
			}
		} catch (IOException e) {
			return "An Error Occurred While Trying To Extract URLs\n";
		}
	}


	public static String runDeobfuscatorAnalysis(File inputFile) {
		String output = "";
		Pair<String, Set<String>> result = DeobfuscatorAnalysis.generateReport(inputFile.getPath());
		output+= "Report For "+inputFile.getName()+":\n"+result.getValue0()+"\n";
		urls = result.getValue1();
		return output;
	}


	public static String runURLInspection(File inputFile) {
		if(urls.isEmpty()){
			return "No URLs To Inspect";	
		}

		String result = "VirusTotal's Inspection Of The URLs Found in "+inputFile.getName()+" Produced The Following:\n\n";
		for (String url : urls) {
			String answer = VirustotalAnalyzer.getUrlReport(url)+"\n";
			result+= answer;

			if(answer.startsWith("Failed To Generate Report: Exceeded maximum number of requests per minute")){
				break;
			}
		}
		return result;
	}


	public static void main(String[] args) {
		String filePath = "";
		File inputFile = null;
		boolean min = false;
		boolean obf = false;
		boolean mal = false;
		boolean s = false;
		boolean g = false;
		boolean u = false;
		boolean p = false;

		for (int i = 0; i < args.length; i++) {


			if(args[i].equals("-min")){
				min = true;
			}

			if(args[i].equals("-obf")){
				obf = true;
			}

			if(args[i].equals("-mal")){
				mal = true;
			}

			if(args[i].equals("-s")){
				s = true;
			}

			if(args[i].equals("-g")){
				g = true;
			}

			if(args[i].equals("-u")){
				u = true;
			}

			if(args[i].equals("-p")){
				p = true;

				if(i < args.length-1){
					filePath = args[i+1];
					inputFile = new File(args[i+1]);
					if(!inputFile.exists()){
						System.out.println("File not found - Please provide a valid path");
						System.exit(1);
					}
					i++;
				}
			}

		}

		if(args.length < 2){
			System.out.println("Usage:\n"
					+ "-p: path of file to inspect, followed by a valid path to an existing file.\n"
					+ "-min: run minified tester on file.\n"
					+ "-obf: run obfuscation tester on file.\n"
					+ "-mal: run maliciousness tester on file.\n"
					+ "-s: search file's code for URLs.\n"
					+ "-g: generate deobfuscator report for the file.\n"
					+ "-u: generate the URL inspection report for the file.\n");

			System.exit(1);
		}


		if(!p){
			System.out.println("Error - no file provided for scan");
			System.exit(1);
		}

		try{
			Run.readFile(inputFile);
		}
		catch(Exception e){
			System.out.println("Failed to open file at path: "+filePath);
			System.exit(1);
		}

		if(min){
			System.out.println("\nMinified Tester Result:\n"+Run.runMinifiedTester());
		}

		if(obf){
			System.out.println("\nObfuscation Tester Result:\n"+Run.runObfuscationTester());
		}

		if(mal){
			try{
				System.out.println("\nMaliciousness Tester Result:\n"+runMaliciousnessTester(inputFile));
			}
			catch(Exception e){
				System.out.println("Failed To Run Maliciousness Tester");
				e.printStackTrace();
			}
		}

		if(s){
			System.out.println("\nURL Search Result:\n"+runURLSearch(inputFile));
		}

		if(g){
			System.out.println("\nDeobfuscator Report:\n"+runDeobfuscatorAnalysis(inputFile));
		}

		if(u){
			System.out.println("\nURL Inspection Report:\n"+runURLInspection(inputFile));
		}

	}
}

