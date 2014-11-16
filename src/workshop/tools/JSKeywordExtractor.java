package workshop.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class handles script keywords, based on the AST of the script. 
 * The static method it contains, extracts and lists all the keywords
 * in the script.
 */
public class JSKeywordExtractor {

	/* JS keywords */
	private static final Set<String> KEYWORDS = new HashSet<String>(
			Arrays.asList("while", "true", "false", "function", "break",
					"case", "catch", "const", "continue", "debugger",
					"default", "delete", "do", "else", "finally", "for", "if",
					"in", "instanceof", "new", "return", "switch", "this",
					"throw", "try", "typeof", "var", "void", "with", "null"));

	/*
	 *  Keeping a hash-table with the desired outputs prevents the program 
	 *  from repeating the computation over and over for the same file.
	 */
	private static HashMap<String, List<String>> keywordsMap = new HashMap<String, List<String>>();

	/**
	 * Extracts all keywords from script and returns as a list of Strings.
	 * 
	 * @param sourcePath - Path to source file.
	 * @return List of all JS keywords in script.
	 */
	public static List<String> extractKeywords(String sourcePath) {
		if (keywordsMap.containsKey(sourcePath)) {
			return keywordsMap.get(sourcePath);
		}
		List<String> keywords = new ArrayList<String>();
		Iterator<String> it = RhinoASTBuilder.getAST(sourcePath).iterator();
		while (it.hasNext()) {
			String line = it.next();

			int type_index = line.indexOf(' ') + 1;
			String keyword = line.substring(type_index).toLowerCase();
			
			/* Dealing with delete keyword exception */
			if(keyword.equals("delprop")) {
				keywords.add("delete");
				continue;
			}
			if (KEYWORDS.contains(keyword)) {
				keywords.add(line.substring(type_index).toLowerCase());
			}
		}

		keywordsMap.put(sourcePath, keywords);
		return keywords;
	}
}

