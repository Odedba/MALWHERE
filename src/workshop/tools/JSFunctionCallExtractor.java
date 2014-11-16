package workshop.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class handles script function calls, based on the AST of the script. 
 * The static method it contains, extracts and lists all the functions
 * that are called in the script.
 */
public class JSFunctionCallExtractor {

	/*
	 * Keeping a hash-table with the desired outputs prevents the program from
	 * repeating the computation over and over for the same file.
	 */
	private static HashMap<String, List<String>> callsMap = new HashMap<String, List<String>>();

	/**
	 * Extracts all function calls from script and returns as a list of Strings.
	 * 
	 * @param sourcePath
	 * @return list of all the functions that are called.
	 */
	public static List<String> extractFunctionCalls(String sourcePath) {
		if (callsMap.containsKey(sourcePath)) {
			return callsMap.get(sourcePath);
		}
		List<String> calls = new ArrayList<String>();
		List<String> ast = RhinoASTBuilder.getAST(sourcePath);
		for (int i = 0; i < ast.size(); i++) {
			int j = 1;
			String node = ast.get(i);
			int type_index = node.indexOf(' ') + 1;

			/* Every call to a function in script starts with a node labeled 'CALL' */
			if (node.length() > type_index && node.substring(type_index).startsWith("CALL")) {
				/* Collect names of called functions from AST, handle the out of ordinary cases */
				if(i + j < ast.size()) {
					String successor1 = ast.get(i + j);
					int type_index_successor1 = successor1.indexOf(' ') + 1;
					/* Base case */
					if(successor1.substring(type_index_successor1).startsWith("NAME:")) {
						calls.add(successor1.substring(type_index_successor1 + "NAME: ".length()));
						continue;
					}
					/*
					 *  A suspicious call: instead of calling to the function with it's name,
					 *  call the function using an access to some array, extracting the name
					 *  of the function.
					 */
					if(successor1.substring(type_index_successor1).equals("GETELEM")) {
						calls.add("!! WARNING !!");
						continue;
					}
					if(successor1.substring(type_index_successor1).equals("FUNCTION")) {
						calls.add("function");
						continue;
					}
					j++;
				}
				if(i + j < ast.size()) {
					String successor1 = ast.get(i + j - 1);
					String successor2 = ast.get(i + j);
					int type_index_successor1 = successor1.indexOf(' ') + 1;
					int type_index_successor2 = successor2.indexOf(' ') + 1;
					if(successor1.substring(type_index_successor1).equals("GETPROP") &&
							successor2.substring(type_index_successor2).equals("CALL")) {
						continue;
					}
					j++;
				}
				
				if(i + j < ast.size()) {
					String successor1 = ast.get(i + j - 2);
					String successor2 = ast.get(i + j - 1);
					String successor3 = ast.get(i + j);
					int type_index_successor1 = successor1.indexOf(' ') + 1;
					int type_index_successor2 = successor2.indexOf(' ') + 1;
					int type_index_successor3 = successor3.indexOf(' ') + 1;
					/* Suspicious call */
					if(successor1.substring(type_index_successor1).equals("GETPROP") &&
							successor2.substring(type_index_successor2).equals("GETELEM")) {
						if(successor3.substring(type_index_successor3).equals("CALL")) {
							calls.add("!! WARNING !!");
					}
						continue;
					}
					if(successor1.substring(type_index_successor1).equals("GETPROP") &&
							successor2.substring(type_index_successor2).startsWith("STRING:") &&
							successor3.substring(type_index_successor3).startsWith("NAME:")) {
						calls.add(successor3.substring(type_index_successor3 + "NAME: ".length()));
						continue;
					}
					if(successor1.substring(type_index_successor1).equals("GETPROP") &&
							successor2.substring(type_index_successor2).equals("REGEXP") &&
							successor3.substring(type_index_successor3).startsWith("NAME:")) {
						calls.add(successor3.substring(type_index_successor3 + "NAME: ".length()));
						continue;
					}
					j++;
				}
				
				/* Call helper functions to handle common cases */
				int numOfProperties = numOfGetProperty(ast, i);
				String s = getCorrectName(ast, i, numOfProperties);
				if(numOfProperties > 0 && s != null) {
					calls.add(s);
				}
			}
		}
		callsMap.put(sourcePath, calls);
		return calls;
	}
				
	
	/**
	 * The functions below helps in correctly extracting the function calls,
	 * and is based on the general structure of the AST. 
	 * 
	 */
	private static int numOfGetProperty(List<String> ast, int indexOfStart) {
		int getPropertyCount = 0;
		for (int i = indexOfStart + 1; i < ast.size(); i++) {
			String node = ast.get(i);
			int type_index = node.indexOf(' ') + 1;
			if (!(node.length() > type_index && node.substring(type_index).startsWith("GETPROP")))
				break;
			getPropertyCount++;
		}
		return getPropertyCount;	
	}
	
	private static String getCorrectName(List<String> ast, int indexOfStart, int numOfProperties) {
		int getNameCount = 0;
		for (int i = indexOfStart + numOfProperties + 1; i < ast.size(); i++) {
			String node = ast.get(i);
			int type_index = node.indexOf(' ') + 1;
			if (!(node.length() > type_index && node.substring(type_index).startsWith("NAME:")) &&
					!(node.length() > type_index && node.substring(type_index).equals("THIS")))
				break;
			getNameCount++;
		}
		if(getNameCount > numOfProperties) {
			String line = ast.get(indexOfStart + 2 * numOfProperties + 1);
			int type_index_line = line.indexOf(' ') + 1;
			return line.substring(type_index_line + "NAME: ".length());
		}
		return null;
	}
}
