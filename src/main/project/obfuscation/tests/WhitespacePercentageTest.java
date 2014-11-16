package main.project.obfuscation.tests;

import workshop.tools.WhitespaceCounter;


/**
 * This class handles one of the characteristics of obfuscated code.
 * Obfuscated JavaScript source codes are many times minified, in order
 * to accomplish two goals. The first is making the code less readable,
 * and the second is to minimize the amount of space the script consumes. 
 * Thus, a source code with a very low proportion of whitespace 
 * characters raises the suspicion that the code is obfuscated.
 *  
 */
public class WhitespacePercentageTest implements ITest {
	
	/* Test passing threshold */
	private static final double thresholdProportion = 0.06;

	@Override
	public double getTestWeight() {
		return WeightType.VERY_HIGH.getWeight();
	}

	/**
	 * Main steps of the algorithm:
	 * 1. Counting the number of whitespace characters in code.
	 * 2. Divide it by the length of the source getting the proportion.
	 * 3. Compare it with threshold proportion.
	 * 
	 * @param javascriptPath - path to JavaScript source file.
	 * @param javascriptCode - the input program (as string).
	 * @return true if the proportion of whitespaces is large
	 * and we suspect the code as obfuscated, false otherwise.
	 */
	@Override
	public boolean isSuspicious(String javascriptPath, String javascriptCode) {
		
		double count = WhitespaceCounter.countWhitespaces(javascriptCode);
		double len = javascriptCode.length();
		if(len == 0)
			return false; // can be divided by 'len'.
		return (count/len) < thresholdProportion;
	}

}