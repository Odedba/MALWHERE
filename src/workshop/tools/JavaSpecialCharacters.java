package workshop.tools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** 
 * Creating a set of the special characters in java.
 * In order for he java compiler to read those characters
 * or symbols as they are, one must concatenate the "\\" 
 * prefix.
 */
public class JavaSpecialCharacters {

	private static String[] specialSeparatorsAsArray  = {"/*", "!", "@", "#", "$", "%", "^","&",
			"*", "(", ")", "\"", "{", "}", "[", "]", "|", "\\", "?", "/",
			"<", ">", ",", "."};
	public static Set<String> specialSeparators = new HashSet<String>(Arrays.asList(specialSeparatorsAsArray));
}
