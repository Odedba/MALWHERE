package workshop.tools;

import java.util.List;
import java.util.Set;

public class ExpressionCounter {

	/***
	 * Breaks the given strings into words and counts the number of words that belong
	 * to the Set of expressions.
	 * 
	 * @param strings - The strings.
	 * @param expressions - The expressions.
	 * @return The number expressions in the strings.
	 */
	public static int count(Set<String> expressions, List<String> strings){
		String delimiters = "[ .,|?!()\\[\\]:'{}=\t;\"<>\n/\\\\]+";
		int counter = 0;
		
		for (String str : strings) {
			String[] words = str.split(delimiters);
			
			for (String tok : words) {
				if(expressions.contains(tok)){
					counter++;
				}
			}
			
		}

		return counter;
	}

}
