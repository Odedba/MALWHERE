package workshop.tools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CharactersIdentifier {

	/* Uppercase English letters for local and external use. */
	private static Character[] uppercaseLettersAsArray = 
		{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 
		'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
		'Y', 'Z'};
	public static Set<Character> uppercaseLetters =
			new HashSet<Character>(Arrays.asList(uppercaseLettersAsArray));


	/* Lowercase English letters for local and external use. */
	private static Character[] lowercaseLettersAsArray = 
		{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 
		'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
		'y', 'z'};
	public static Set<Character> lowercaseLetters =
			new HashSet<Character>(Arrays.asList(lowercaseLettersAsArray));


	/* Digits for local and external use. */
	private static Character[] digitsAsArray = 
		{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	public static Set<Character> digits =
			new HashSet<Character>(Arrays.asList(digitsAsArray));


	/* Digits for local and external use. */
	private static Character[] topKeyboardCharacrersAsArray = 
		{'!', '@', '#', '$', '%', '^', '&', '*', '~', '.'};
	public static Set<Character> topKeyboardCharacters =
			new HashSet<Character>(Arrays.asList(topKeyboardCharacrersAsArray));
}

