package workshop.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles defined names, based on the AST of the script.
 * In JavaScript, it is possible to define variables by using the 
 * reserved word 'var'. For example, in order to define a variable with
 * the name "gameOver" use the line: var gameOver
 * Similarly, the reserved word 'function' is used to define functions. 
 * The static method this class contains extracts and lists all the defined
 * names in script. 
 */
public class JSDefinedNamesExtractor {

	public static List<String> extractDefinedNames(String sourcePath) {
		/* List of defined names */
		List<String> definedNames = new ArrayList<String>();
		/* Script's AST */
		List<String> ast = RhinoASTBuilder.getAST(sourcePath);
		
		for (int i = 0; i < ast.size()-1; i++) {
			String varOrFunction = ast.get(i);
			String name = ast.get(i+1);
			
			/* Extracting the type of the node */
			int type_index_var_or_function = varOrFunction.indexOf(' ') + 1;
			int type_index_name = name.indexOf(' ') + 1;
			
			/* Check sequence of nodes in AST to recognize the definition */
			if((varOrFunction.substring(type_index_var_or_function).equals("VAR") ||
				varOrFunction.substring(type_index_var_or_function).equals("FUNCTION")) &&
			    name.substring(type_index_name).startsWith("NAME:")) {
				definedNames.add(name.substring(type_index_name + "NAME: ".length()));
			}		
		}
		return definedNames;
	}
}
