package workshop.tools;

/**
 * Count whitespace characters at the given string.
 */
public class WhitespaceCounter {

	public static double countWhitespaces(String s) {
		double count = 0.0;
		for (int i = 0; i < s.length(); i++) {
			if(Character.isWhitespace(s.charAt(i)))
				count++;
		}
		return count;
	}
}
