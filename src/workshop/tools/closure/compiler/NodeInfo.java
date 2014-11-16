package workshop.tools.closure.compiler;

import com.google.javascript.rhino.Node;

public class NodeInfo {
	
	
	/**
	 * Extracts the name out of a Node's label, given the label starts with the "NAME: " prefix.
	 * 
	 * @param node - the Node.
	 * @return The Node's name.
	 */
	public static String getName(Node node){
		String name = node.toString();
		int start = ("NAME: ").length();
		int end = name.indexOf(' ', start);
		name = name.substring(start, end);
		return name;
	}
	
	
	/**
	 * Extracts the string out of a Node's label, given the label starts with the "STRING: " prefix.
	 * 
	 * @param node - the Node.
	 * @return The Node's string.
	 */
	public static String getString(Node node){
		String name = node.toString();
		int start = ("STRING: ").length();
		int end = name.indexOf(' ', start);
		name = name.substring(start, end);
		return name;
	}
	
	
}
