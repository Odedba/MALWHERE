
package main.project.obfuscation.tests;

import java.util.List;

import workshop.tools.JSStringExtractor;

/**
 * This class handles one of the characteristics of obfuscated code.
 * In this case it checks if the code contains strings of unreasonable
 * length. Obfuscators use long strings for various goals, including
 * writing JavaScript code as string, encoding variable names, passing
 * huge arrays (represented as strings) as parameters to functions
 * and many more. 
 */
public class LongStringsTest implements ITest {
	
	/* String length constants */
	private static final int lowLength = 40;
	private static final int lowPlusLength = 60;
	private static final int mediumLength = 80;
	private static final int mediumPlusLength = 100;
	private static final int longLength = 120;
	private static final int longPlusLength = 140;
	private static final int veryLongLength = 160;
	private static final int ultraLongLength = 260;
	private static final int monsterLongLength = 360;
	
	/* Thresholds for the test: low, medium, high */
	private static final int lowScore = 6;
	private static final int mediumScore = 12;
	private static final int highScore = 31;
	
	/* Script's total score */ 
	private static int totalScore = 0;


	@Override
	public double getTestWeight() {
		if(totalScore >= highScore)
			return WeightType.VERY_HIGH.getWeight();
		
		if(totalScore >= mediumScore)
			return WeightType.MEDIUM.getWeight();
		
		return WeightType.LOW.getWeight();
	}

	/**
	 * The implementation of the test (main steps):
	 * 1. Extract all strings from source code, based on 
	 * the AST of the program.
	 * 2. update a global counter that accumulates score
	 *  according to the the length of each extracted string.
	 * 3. Compare the accumulated score with threshold.
	 * 
	 * @param javascriptPath - path to JavaScript source file.
	 * @param javascriptCode - the input program (as string).
	 * @return true if we found enough long strings in code 
	 * according to the defined thresholds, and false otherwise.
	 */
	@Override
	public boolean isSuspicious(String javascriptPath , String javascriptCode) {
		/* Getting all strings in source code */
		List<String> allStringsInCode = JSStringExtractor.extractFromFile(javascriptPath);
		totalScore = 0;
		
		for (String s : allStringsInCode) {
			totalScore = updateScore(s, totalScore);
		}
		/* To pass the test, the accumulated score should be the low threshold value or more. */
		return (totalScore >= lowScore);
	}		

	
	/**
	 * Helper function that checks the length of the string,
	 * and updates the total score accordingly.
	 */
	private static int updateScore(String s, int score) {
		if(s.length() > lowLength)
			score++;
		if(s.length() > lowPlusLength)
			score++;
		if(s.length() > mediumLength)
			score++;
		if(s.length() > mediumPlusLength)
			score++;
		if(s.length() > longLength)
			score++;
		if(s.length() > longPlusLength)
			score++;
		if(s.length() > veryLongLength)
			score++;
		if(s.length() > ultraLongLength)
			score += mediumScore;
		if(s.length() > monsterLongLength)
			score += highScore;
		return score;
	}
}
