package workshop.tools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JSReservedWords {

	/** 
	 * All JavaScript reserved words, including the ones reserved
	 * for future use in the language.
	 */
	private static String[] reservedJavaScriptWords = 
		{"abstract", "else", "instanceof", "super", "boolean",
		 "enum", "int", "switch", "break", "export", "interface",
		 "synchronized", "byte", "extends", "let", "this",  
		 "case", "false", "long", "throw", "catch", "final", "native",
		 "throws", "char", "finally", "new", "transient", "class",
		 "float", "null", "true", "const", "for", "package", "try",  
		 "continue", "function", "private", "typeof", "debugger", 
		 "goto", "protected", "var", "default",	"if", "public",
		 "void", "delete", "implements", "return", "volatile", "do",
		 "import", "short", "while", "double", "in", "static", "with"};
	
	public static Set<String> keywords = new HashSet<String>(Arrays.asList(reservedJavaScriptWords));
}
