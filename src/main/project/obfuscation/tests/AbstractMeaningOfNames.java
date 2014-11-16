package main.project.obfuscation.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import workshop.tools.CharactersIdentifier;

/**
 * This abstract class is the base class for handling script's names.
 * It lies down the foundations for analyzing the structure and meaning
 * of names in the script.
 * The use of meaningless names is common, because it is a simple to
 * implement technique that adds to the obfuscation level.  
 * The class contains several methods and constants for distinguishing 
 * between reasonable and suspicious names:
 * 1. The hugeWordsPull field - a large set of strings contains 355,000 words,
 * that serves as a dictionary for names (or identifier parts after breaking).
 * The set contains words that have some kind of a meaning (it makes sense
 * if a programmer use it).
 * 2. The camelCaseBreaker and underscoreBreaker functions that break 
 * names into parts according to the CamelCase and underscore conventions.
 * 3. The isLegitimateName function that based on the tools described above,
 * eventually determines if the name is legitimate.    
 * The extending classes of AbstractNames can use the same logic for analyzing
 * names legitimacy, while differently defining which names to extract from 
 * the AST of the script.   
 */
public abstract class AbstractMeaningOfNames {

	/* Holding the pull of words in memory */
	private Set<String> hugeWordsPull = new HashSet<String>();
	
	/* Identifier threshold */
	private final double identifierThreshold = 0.65;
	
	/* Abstract methods */
	public abstract boolean isSuspicious(String javascriptPath, String javascriptCode);
	public abstract double getTestWeight();
	
	/**
	 * The isLegitimateName function determines if the passed
	 * name is a legitimate name.
	 * @param name - the name extracted from the script's AST.
	 * @return true if the name is considered valid, false otherwise.
	 */
	protected boolean isLegitimateName(String name) {
		/* Dropping the underscore at start */
		if(name.charAt(0) == '_') {
			name = name.substring(1);
		}
		/* Some non-legitimate cases */
		int len = name.length();
		if(len == 1 && name.charAt(0) != 'i' && name.charAt(0) != 'j') {
			return false;
		}
		if(CharactersIdentifier.uppercaseLetters.contains(name.charAt(0))) {
			StringBuffer firstChar = new StringBuffer(name.substring(0,1).toLowerCase());
			name = firstChar.append(name.substring(1)).toString();
		}
		if(name.charAt(0) == '_' || name.charAt(name.length()-1) == '_') {
			return false;
		}
		if(name.contains("0x") || name.contains("0X")) {
			return false;
		}
		if(len == 2 && CharactersIdentifier.digits.contains(name.charAt(1))) {
			return false;
		}
		if(len == 2 && CharactersIdentifier.lowercaseLetters.contains(name.charAt(0)) &&
				CharactersIdentifier.uppercaseLetters.contains(name.charAt(1))) {
			return false;
		}
		if(len == 2 && CharactersIdentifier.lowercaseLetters.contains(name.charAt(1)) &&
				CharactersIdentifier.uppercaseLetters.contains(name.charAt(0))) {
			return false;
		}
		/* Identifier is ready for breaking according to conventions */
		if(underscoreCount(name) > 0) {
			return isLegitimateIdentifier(underscoreBreaker(name));
		} else {
			return isLegitimateIdentifier(camelCaseBreaker(name));
		}
	}
	
	/**
	 * Helper function that counts the number of underscores
	 * in the string.
	 * @param name - the name extracted from the script's AST.
	 * @return the number of underscores in name.
	 */
	private int underscoreCount(String name) {
		int count = 0;
		for (int i = 0; i < name.length(); i++) {
			if(name.charAt(i) == '_')
				count++;
		}
		return count;
	}
	
	/**
	 * Helper function that counts the number of UpperCase letters
	 * in the string.
	 * @param name - the name extracted from the script's AST.
	 * @return the number of UpperCase letters in name.
	 */
	private int uppercaseCount(String name) {
		return uppercaseLettersIndexes(name).size();
	}
	
	/**
	 * Helper function for marking the UpperCase letters indexes.
	 * @param name - the name extracted from the script's AST.
	 * @return a linked list contains the indexes.
	 */
	private List<Integer> uppercaseLettersIndexes(String name) {
		List<Integer> indexes = new LinkedList<Integer>();
		indexes.add(0);

		for (int i = 0; i < name.length(); i++) {
			char curr = name.charAt(i);
			if(CharactersIdentifier.uppercaseLetters.contains(curr))
				indexes.add(i);
		}
		return indexes;
	}
	
	/**
	 * Helper function for marking the underscores indexes.
	 * @param name - the name extracted from the script's AST.
	 * @return a linked list contains the indexes.
	 */
	private List<Integer> underscoresIndexes(String name) {
		List<Integer> indexes = new LinkedList<Integer>();
		indexes.add(0);

		for (int i = 0; i < name.length(); i++) {
			char curr = name.charAt(i);
			if(curr == '_')
				indexes.add(i);
		}
		return indexes;
	}
	
	/**
	 * camelCaseBreaker function is in  charge of breaking identifier
	 * names according to the convention.
	 * @param identifierName - the name extracted from the script's AST.
	 * @return an array contains the composing words of the name.
	 */
	private String[] camelCaseBreaker(String identifierName) {
		int numOfUppercaseLetters = uppercaseCount(identifierName);
		List<Integer> indexes = uppercaseLettersIndexes(identifierName);
		
		/* Holds the words that compose a single identifier. */ 
		String[] afterBreak = new String[numOfUppercaseLetters]; 

		/* Iterate over the composing words, inserting words into array. */
		for(int j = 0; j < numOfUppercaseLetters - 1; j++) {
			int start = j;
			int end = j+1;
			String singleWord = identifierName.substring(indexes.get(start), indexes.get(end));
			singleWord = singleWord.toLowerCase();
			afterBreak[j] = singleWord;
		}
		String lastWord = identifierName.substring(indexes.get(numOfUppercaseLetters-1));
		lastWord = lastWord.toLowerCase();
		afterBreak[numOfUppercaseLetters-1] = lastWord;
		return afterBreak;
	}
	

	/**
	 * underscoreBreaker function is in  charge of breaking identifier
	 * names according to the convention.
	 * @param identifierName - the name extracted from the script's AST.
	 * @return an array contains the composing words of the name.
	 */
	private String[] underscoreBreaker(String identifierName) {
		int numOfUnderscores = underscoreCount(identifierName);
		List<Integer> indexes = underscoresIndexes(identifierName);
		
		/* Holds the words that compose a single identifier. */ 
		String[] afterBreak = new String[numOfUnderscores+1]; 
		if(numOfUnderscores == 0) {
			afterBreak[0] = identifierName.toLowerCase();
			return afterBreak;
		}
			
		/* Iterate over the composing words, inserting words into array. */
		for(int j = 0; j < numOfUnderscores; j++) {
			int start = j;
			int end = j+1;
			String singleWord = identifierName.substring(indexes.get(start)+1, indexes.get(end));
			if(j == 0) {
				singleWord = identifierName.substring(indexes.get(start), indexes.get(end));
			}
			singleWord = singleWord.toLowerCase();
			afterBreak[j] = singleWord;
		}
		String lastWord = identifierName.substring(indexes.get(numOfUnderscores)+1);
		lastWord = lastWord.toLowerCase();
		afterBreak[numOfUnderscores] = lastWord;
		return afterBreak;
	}

	
	/**
	 * Helper function that takes the array of words after breaking
	 * an identifier, and counts the number of words in the array
	 * that "HugeWordPull" contains. It determines if an identifier
	 * has a meaningful name.
	 * @param words - array of the composing words of an identifier.
	 * @return true if and only if at least 'identifierThreshold' 
	 * of the words are in "HugeWordPull".
	 */
	private boolean isLegitimateIdentifier(String[] words) {
		double counter = 0.0;
		double shortCounter = 0.0;
		double length = words.length;

		for (int i = 0; i < words.length; i++) {
			if(words[i].length() == 1)
				shortCounter++;
			if(hugeWordsPull.contains(words[i]))
				counter++;
		}
		return ((counter / length > identifierThreshold) && shortCounter < 3); 
	}
	
	/**
	 * Helper function that loads the pull of words into memory.
	 * If the pool has been loaded already to memory, the process
	 * won't repeat again.
	 * @throws FileNotFoundException 
	 */
	protected Set<String> loadPullOfWords() throws FileNotFoundException {
		File pool = new File("./lib/HugeWordPool");
		Scanner words = new Scanner(pool);
		if (!hugeWordsPull.isEmpty()) {
			words.close();
			return hugeWordsPull;
		}
		while (words.hasNextLine()) {
			String curr = words.nextLine();
			hugeWordsPull.add(curr);
		}
		words.close();
		return hugeWordsPull;
	}
}
