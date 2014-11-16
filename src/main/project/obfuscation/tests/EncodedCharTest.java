package main.project.obfuscation.tests;

import java.util.List;

import workshop.tools.JSFunctionCallExtractor;
import workshop.tools.RhinoASTBuilder;
import workshop.tools.js.beautifier.JSBeautifier;
import workshop.tools.js.beautifier.JSBeautifierOptions;

/**
 * This class is in charge of exposing encoded characters.
 * Obfuscators tend to replace ASCII characters with encoded
 * characters, making the code less readable, using several 
 * known encoding methods. Thus, we use the js.beautifier 
 * tool included in project, which has a feature of replacing
 * those encoded chars with ASCII chars. In addition, JavaScript
 * has a standard built-in function named 'unescape' that decodes
 * escape chars. Finding encoded characters in code or a call to
 * 'unescape' is an excellent indication that the code has gone
 * through obfuscation. 
 */
public class EncodedCharTest implements ITest{

	@Override
	public double getTestWeight() {
		return WeightType.VERY_HIGH.getWeight();
	}

	/** 
	 * An implementation of the test:
	 * as mentioned before, the js.beautifier is capable of 
	 * immediately exposing the encoded characters. Hence, 
	 * we execute this feature on code and compare the result
	 * with the original source code. 
	 * In addition, based on the AST of the script, check if 
	 * there is a call to 'unescape' function (explicit or implicit).
	 * 
	 * @param javascriptPath - path to JavaScript source file.
	 * @param javascriptCode - the input program (as string).
	 * @return true if the source code contains escape chars 
	 * or an attempt for decoding chars using JS built-in function 
	 * and false otherwise.
	 */
	@Override
	public boolean isSuspicious(String javascriptPath, String javascriptCode) {
		
		JSBeautifier beautifier = new JSBeautifier();
		JSBeautifierOptions options= new JSBeautifierOptions();
		
		/* Execute the beautifier with and without the exposing feature */
		options.unescape_strings = false;
		String before = beautifier.js_beautify(javascriptCode, options);

		options.unescape_strings = true;
		String after = beautifier.js_beautify(javascriptCode, options);
		
		/* The list of functions called in the script */
		List<String> calls = JSFunctionCallExtractor.extractFunctionCalls(javascriptPath);
		/* The script's AST */
		List<String> ast = RhinoASTBuilder.getAST(javascriptPath);
		
		return !before.equals(after) || calls.contains("unescape") || unescapeVariable(ast, calls);
	}
	
	/**
	 * Helper function that checks if there is an implicit call to 
	 * 'unescape' in the file.
	 * An obfuscator/attacker can assign the name "unescape" to a certain
	 * variable and then use the name of the variable when calling to 
	 * unescape. The 'unescapeVariable' function aims to expose this trick. 
	 * @param ast - The AST
	 * @param calls - Calls to functions in the file.
	 * @return true if and only if the condition above holds.
	 */
	private static boolean unescapeVariable(List<String> ast, List<String> calls) {
		for (int i = 0; i < ast.size()-2; i++) {
			String var = ast.get(i);
			String varName = ast.get(i+1);
			String unescape = ast.get(i+2);
			
			/* Extracting the type of the node */
			int type_index_var = var.indexOf(' ') + 1;
			int type_index_varName = varName.indexOf(' ') + 1;
			int type_index_unescape = unescape.indexOf(' ') + 1;
			
			/* Check sequence of nodes in AST to recognize the call */
			if(var.substring(type_index_var).equals("VAR") &&
			   varName.substring(type_index_varName).startsWith("NAME:") &&
			   unescape.substring(type_index_varName).startsWith("NAME:") &&
			   unescape.substring(type_index_unescape + "NAME: ".length()).equals("unescape") &&
			    calls.contains(varName.substring(type_index_unescape + "NAME: ".length()))) {
				return true;
			}		
		}
		return false;
	}
	
}
