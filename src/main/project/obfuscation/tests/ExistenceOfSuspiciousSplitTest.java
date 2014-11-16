package main.project.obfuscation.tests;

import workshop.tools.JSCommentRemover;
import workshop.tools.JSReservedWords;
import workshop.tools.JavaSpecialCharacters;


/**
 * This class handles one of the characteristics of obfuscated code.
 * In this case it checks the existence of the 'split' function that helps 
 * obfuscators in making the original code less readable.
 * We found out that obfuscators tends to scramble the order of a code (which 
 * is of course very hard to read for humans) but they keep track with the order
 * of execution with code integrated orders.
 * Our goal here is to locate the split function, and then imitate it's action as
 * intended during the opening of the code. Finding reserved words of JavaScript is a 
 * significant hint for the fact that the code has gone trough obfuscation.
 */
public class ExistenceOfSuspiciousSplitTest implements ITest {
	
	/* splitHandler function is recursive and uses this flag */
	private static boolean recursion = false;
	
	/* The number of reserved words in the string threshold */
	private static final int numOfReservedWordsThreshold = 2;
	
	/* The offset needed to start searching after split occurrence */
	private static final int splitOffset = 7;
	
	@Override
	public double getTestWeight() {
		return WeightType.VERY_HIGH.getWeight();
	}
	
	/**
	 * The main steps of this algorithm are:
	 * 1. Recognize a full 'split' pattern.
	 * 2. Retrieve the string that split function operates on.
	 * 3. Retrieve the separator character passed as parameter to split
	 *    while paying attention to special separator characters in Java
	 *    that require special treatment.
	 * 4. Break the string into tokens.
	 * 5. Search for reserved JavaScript words in the set of tokens.
	 *    
	 * @param javascriptPath - path to JavaScript source file.
	 * @param javascriptCode - the input program (as string).
	 * @return true if a suspicious split is found and false otherwise.
	 */
	@Override
	public boolean isSuspicious(String javascriptPath, String javascriptCode) {
		/*
		 * Removing comments is necessary in this case, as clean
		 * code is required for correct analysis. 
		 */
		String code = JSCommentRemover.removeComments(javascriptCode);
		recursion = false;
		return splitHandler(code);
	}
		
private static boolean splitHandler(String programStr) {
		
		int codeLength = programStr.length();
		int firstOccurenceOfSplit = programStr.indexOf(".split(");
		int startSearchForClosingBracket = firstOccurenceOfSplit + splitOffset;
		int indexOfClosingBracket = programStr.indexOf(')', firstOccurenceOfSplit);
		int indexOfParametersStart = startSearchForClosingBracket + 1;
		int indexOfParametersEnd = indexOfClosingBracket - 1;
		String retrievedString = "";
		
		/* The cases when we don't recognize the full "split" pattern. */
		if(codeLength == startSearchForClosingBracket)
			return recursion;
		char c = programStr.charAt(startSearchForClosingBracket);
		if (firstOccurenceOfSplit == -1 || 
				(c != '\'' && c != '\"')) {
			return recursion;
		}
		/*
		 * If there is no closing bracket the template is incomplete, and the
		 * function returns.
		 */
		if (indexOfClosingBracket == -1)
			return recursion;
		/* Checking for the validity of the parameter's string */
		if(programStr.charAt(indexOfParametersEnd) != c)
			return recursion;
		
		/* Retrieving the string representing the parameters for split. */
		String separator = programStr.substring(indexOfParametersStart, indexOfParametersEnd);

		/*
		 * Retrieving the string to split. Check also for incomplete
		 * templates.
		 */
		if (firstOccurenceOfSplit <= 1)
			return splitHandler(programStr.substring(firstOccurenceOfSplit + 2));
		if (programStr.charAt(firstOccurenceOfSplit - 1) != '\''
				&& programStr.charAt(firstOccurenceOfSplit - 1) != '\"')
			return splitHandler(programStr.substring(firstOccurenceOfSplit + 2));

		int indexOfStartingQuote = -1;
		int indexOfEndingQuote = firstOccurenceOfSplit - 1;

		if (programStr.charAt(indexOfEndingQuote) == '\'') {
			for (int i = indexOfEndingQuote - 1; i > -1; i--) {
				/* string bounds check. */
				if (i == 0 && programStr.charAt(i) != '\'')
					return splitHandler(programStr.substring(firstOccurenceOfSplit + 2));
				/* index of the start of the string has been found. */
				if (programStr.charAt(i) == '\'') {
					indexOfStartingQuote = i;
					break;
				}
			}
			/* The "input" String for split function */
			 retrievedString = programStr.substring(indexOfStartingQuote + 1,
					indexOfEndingQuote);
		}

		if (programStr.charAt(indexOfEndingQuote) == '\"') {
		for (int i = indexOfEndingQuote - 1; i > -1; i--) {
			/* string bounds check. */
			if (i == 0 && programStr.charAt(i) != '\"')
				return splitHandler(programStr.substring(firstOccurenceOfSplit + 2));
			/* index of the start of the string has been found. */
			if (programStr.charAt(i) == '\"') {
				indexOfStartingQuote = i;
				break;
			}
		}
		/* The "input" String for split function */
		retrievedString = programStr.substring(indexOfStartingQuote + 1,
				indexOfEndingQuote);
		}
		
		/*  
		 * Getting the pull of strings after separating the input string.
		 * Handling with special Java separators first.
		 */
		if(JavaSpecialCharacters.specialSeparators.contains(separator)) {
			separator = "\\" + separator;
		} 
		String[] afterSeparation = retrievedString.split(separator);
		
		 /* If we do locate the split function, reserved words search will be started. */
		int numOfKeywordsInString = 0;
		for (int i = 0; i < afterSeparation.length; i++) {
			if(JSReservedWords.keywords.contains(afterSeparation[i])) {
				numOfKeywordsInString++;
			}
		}
		/* If the string contains 2 keywords or more, it is considered suspicious. */ 
		if(numOfKeywordsInString >= numOfReservedWordsThreshold) 
			recursion = true;
		
		return recursion;
	}
}

