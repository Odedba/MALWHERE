package main.project.obfuscation.tests;

import java.util.List;

import workshop.tools.JSFunctionCallExtractor;

/**
 * This class handles one of the characteristics of obfuscated code.
 * In this case it checks the existence of the 'replace' function that helps 
 * obfuscators (or attackers) in making source code less readable. 
 */
public class ExistenceOfReplaceTest implements ITest {

	/* Holds the number of calls to replace in the script */ 
	private static int numOfCallsToReplace = 0;
	
	/* Test passing threshold */ 
	private static final int lowThreshold = 0;
	private static final int highThreshold = 2;
	
	@Override
	public double getTestWeight() {
		if(numOfCallsToReplace >= highThreshold)
			return WeightType.MEDIUM.getWeight();
		
		return WeightType.LOW.getWeight();
	}

	/**
	 * The implementation of the test (main steps):
	 * 1. Based on the AST of the script, extract all functions
	 * that are called in script.  
	 * 2. Count the calls to 'replace' and determine the importance
	 * of the test accordingly.
	 * 
	 * @param javascriptPath - path to JavaScript source file.
	 * @param javascriptCode - the input program (as string).
	 * @return true if we found a call to 'replace' and
	 * false otherwise.
	 */
	@Override
	public boolean isSuspicious(String javascriptPath, String javascriptCode) {
		numOfCallsToReplace = 0;
		/* All function calls in script */
		List<String> calls = JSFunctionCallExtractor.extractFunctionCalls(javascriptPath);
		for (String call : calls) {
			if(call.equals("replace"))
				numOfCallsToReplace++;
		}
		/* To pass the test, the accumulated score should be the low threshold value or more. */
		return numOfCallsToReplace > lowThreshold;	
	}

}
