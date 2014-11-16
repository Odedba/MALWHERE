package workshop.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Extract all Strings from a JS code. 
 */
public class JSStringExtractor {
	private static HashMap<String, ArrayList<String>> stringMap = new HashMap<String, ArrayList<String>>();


	/**
	 * Extracts all strings from the script at 'sourcePath' and returns as a list of Strings.
	 * 
	 * @param sourcePath - Path to source file.
	 * @return List of Strings from the script.
	 */
	public static ArrayList<String> extractFromFile(String sourcePath){
		if(stringMap.containsKey(sourcePath)){
			return stringMap.get(sourcePath);
		}
		ArrayList<String> strings = new ArrayList<String>();
		Iterator<String> it = RhinoASTBuilder.getAST(sourcePath).iterator();
		while(it.hasNext()){
			String line = it.next();

			int type_index = line.indexOf(' ') + 1;

			if(type_index >= 0 && line.length() > type_index && line.substring(type_index).startsWith("STRING: ")){
				strings.add(line.substring((type_index) + ("STRING: ").length()));
			}
		}
		stringMap.put(sourcePath, strings);
		return strings;
	}


	/**
	 * Creates a list of all script's strings with no duplicates.
	 * 
	 * @param sourcePath - Path to source file.
	 * @return List of all script's strings with no duplicates.
	 */
	public static List<String> extractStringsNoDuplicates(String sourcePath) {
		List<String> l = extractFromFile(sourcePath);
		Set<String> s = new HashSet<String>();
		s.addAll(l);
		List<String> strings = new ArrayList<String>();
		strings.addAll(s);
		return strings;	
	}


	/**
	 * Removes all string contents from the script. The function returns
	 * the same source code, but with the difference of replacing
	 * the original strings with single whitespace strings (i.e: " ").
	 * 
	 * @param sourcePath - Path to source file.
	 * @param sourceCode - Code found at 'sourcePath'.
	 * @return sourceCode with single whitespace strings.
	 */
	public static String removeStringContents(String sourcePath, String sourceCode) {

		List<String> strings = extractStringsNoDuplicates(sourcePath);
		String result = sourceCode;

		for (String s : strings) {
			String st = "\"";
			st = st.concat(s);
			st = st.concat("\"");
			result = result.replace(st, "\"\"");
		}

		for (String s : strings) {
			String st = "'";
			st = st.concat(s);
			st = st.concat("'");
			result = result.replace(st, "''");
		}
		return result;	
	}

}
