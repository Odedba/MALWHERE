package main.project.obfuscation.tests;

import java.util.ArrayList;
import java.util.List;
import workshop.tools.JSFunctionCallExtractor;
import workshop.tools.RhinoASTBuilder;

/**
 * Obfuscated JavaScript (and malicious JavaScript in particular) use 
 * functions that evaluate expressions or execute string statements.
 * The attacker exploits the execution backdoor, passes a string 
 * representing the code to one of the below listed functions,
 * and the code is executed.
 * Thus, a code contains one of these functions raises our suspicion,  
 * and is handled accordingly.   
 * Functions that can evaluate strings in JavaScript:
 * -----------------------------------
 * 	1. eval()
 *  2. Function
 *	2. new Function(param1, ..., paramN, funcBody)
 *	3. setTimeout()
 *	4. document.write()
 * */
public class StringEvaluationTest implements ITest{
	
	/*
	 * The score constant that is added to total score with each
	 * appearance of a suspicious function.
	 */ 
	private static final int weightOfEval = 5;
	private static final int weightOfOtherFunctions = 1;
	
	/* Passing threshold */
	private static final int scoreThreshold = 5;
	
	/* Script's total score */ 
	private static int totalScore = 0;
	
	@Override
	public double getTestWeight() {		
		return WeightType.HIGH.getWeight();
	}

	/**
	 * main steps of the algorithm:
	 * 1. Based on the AST of the program, iterate over all possible function calls.
	 * 2. if we recognize a call to 'eval', 'Function', 'setTimeout', 
	 * 'new Function' or 'document.write' update score (the 'eval' function adds
	 * more weight to the total score than other functions).
	 * Note: 'new Function' and 'document.write' are extracted slight differently
	 * using helper functions (because of the AST structure). 
	 * 3. Compare total score with threshold.    
	 *   
	 * @param javascriptPath - path to JavaScript source file.
	 * @param javascriptCode - the input program (as string).
	 * @return true if code contains suspicious calls and false otherwise.
	 */
	@Override
	public boolean isSuspicious(String javascriptPath, String javascriptCode) {
		/* All function calls in script */
		List<String> callsInScript = JSFunctionCallExtractor.extractFunctionCalls(javascriptPath);
		
		/* Script's total score */ 
		totalScore = 0;
		
		for (String functionCall : callsInScript) {
			if(functionCall.equals("eval"))
				totalScore += weightOfEval;
			
			if(functionCall.equals("Function") ||
			   functionCall.equals("setTimeout"))
				totalScore += weightOfOtherFunctions;
		}
		
		/* The script's AST */
		ArrayList<String> ast = RhinoASTBuilder.getAST(javascriptPath);
		
		/* The helper functions handle document.write and Function and update the score */
		totalScore += documentWriteHandler(ast);
		totalScore += newFunctionHandler(ast);
		
		/* To pass the test, the accumulated score should be the threshold value or more. */
		return totalScore >= scoreThreshold;		
	}

	/**
	 * Helper function. Counts the number of calls to document.write
	 * @param ast - The AST.
	 * @return number of calls to document.write
	 */
	private static int documentWriteHandler(List<String> ast) {
		/* Holds the number of calls to document.write */
		int counter = 0; 
		
		for (int i = 0; i < ast.size()-3; i++) {
			String call = ast.get(i);
			String getProp = ast.get(i+1);
			String document = ast.get(i+2);
			String write = ast.get(i+3);
			
			/* Extracting the type of the node */
			int type_index_call = call.indexOf(' ') + 1;
			int type_index_getprop = getProp.indexOf(' ') + 1;
			int type_index_document = document.indexOf(' ') + 1;
			int type_index_write = write.indexOf(' ') + 1;
			
			/* Check sequence of nodes in AST to recognize the call */
			if(call.substring(type_index_call).equals("CALL") &&
			   getProp.substring(type_index_getprop).equals("GETPROP") &&
			   document.substring(type_index_getprop).startsWith("NAME:") &&
			   write.substring(type_index_getprop).startsWith("NAME:") &&
			   document.substring(type_index_document + "NAME: ".length()).equals("document") &&		
			   write.substring(type_index_write + "NAME: ".length()).equals("write")) {
				counter++;
			}		
		}
		return counter;
	}
	
	/**
	 * Helper function. Counts the number of calls to the 'Function'
	 * constructor - i.e new Function(param1, ..., paramN, funcBody)
	 * @param ast - The AST.
	 * @return number of calls to 'Function' constructor
	 */
	private static int newFunctionHandler(List<String> ast) {
		/* Holds the number of calls to 'Function' constructor */
		int counter = 0; 
		
		for (int i = 0; i < ast.size()-1; i++) {
			String _new = ast.get(i);
			String function = ast.get(i+1);
			
			/* Extracting the type of the node */
			int type_index_new = _new.indexOf(' ') + 1;
			int type_index_function = function.indexOf(' ') + 1;
			
			/* Check sequence of nodes in AST to recognize the constructor */
			if(_new.substring(type_index_new).equals("NEW") &&
			   function.substring(type_index_function).startsWith("NAME:") &&
			   function.substring(type_index_function + "NAME: ".length()).equals("Function")) {
				counter++;
			}		
		}
		return counter;
	}
	
}
