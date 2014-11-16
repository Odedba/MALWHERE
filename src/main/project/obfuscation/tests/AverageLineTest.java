package main.project.obfuscation.tests;

import workshop.tools.JSCommentRemover;
import workshop.tools.JSLineCounter;

/**
 * This class handles one of the characteristics of obfuscated code.
 * Obfuscated or malicious JavaScript code tend to contain very long
 * code lines. The reason for this is for example code minification,
 * or very long arrays and strings. 
 */
public class AverageLineTest implements ITest {
	
	/* Test passing threshold */
	private static final double thresholdLength = 100.0;

	@Override
	public double getTestWeight() {
		return WeightType.VERY_HIGH.getWeight();
	}

	/**
	 * Main steps of the algorithm:
	 * 1. Counting the number of commas in code.
	 * 2. Divide it by the length of the source getting the 
	 * length of line average.
	 * 3. Compare it with threshold.
	 * 
	 * @param javascriptPath - path to JavaScript source file.
	 * @param javascriptCode - the input program (as string).
	 * @return true if the length-of-line average is large
	 * and we suspect the code as obfuscated, false otherwise.
	 */
	@Override
	public boolean isSuspicious(String javascriptPath, String javascriptCode) {
		/*
		 * Removing comments is necessary in this case, as clean
		 * code is required for correct analysis. 
		 */
		String code = JSCommentRemover.removeComments(javascriptCode);
		double count = JSLineCounter.lineCounter(code);
		double len = code.length();
		if(len == 0)
			return false; // can be divided by 'len'.
		return (len/count) >= thresholdLength;
	}
}