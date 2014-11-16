package main.project.obfuscation.tests;

import workshop.tools.JSCommentRemover;

/**
 * This class handles one of the characteristics of obfuscated code.
 * Obfuscated or malicious JavaScript code tend to contain a number
 * of commas exceeding the norm. The reason for this is that obfuscators
 * (or attackers) usually generate long objects as arrays and strings 
 * that contain encoded information or malicious code, separated by 
 * commas.  
 */
public class CommasPercentageTest implements ITest {
	
	/* Test passing threshold */
	private static final double thresholdProportion = 0.1;

	@Override
	public double getTestWeight() {
		return WeightType.VERY_HIGH.getWeight();
	}

	/**
	 * Main steps of the algorithm:
	 * 1. Count the number of commas in code.
	 * 2. Divide it by the length of the source getting the proportion.
	 * 3. Compare it with threshold proportion.
	 * 
	 * @param javascriptPath - path to JavaScript source file.
	 * @param javascriptCode - the input program (as string).
	 * @return true if the proportion of commas is large
	 * and we suspect the code as obfuscated, false otherwise.
	 */
	@Override
	public boolean isSuspicious(String javascriptPath, String javascriptCode) {
		/*
		 * Removing comments is necessary in this case, as clean
		 * code is required for correct analysis. 
		 */
		String code = JSCommentRemover.removeComments(javascriptCode);
		double count = 0.0;
		double len = code.length();
		if(len == 0)
			return false; // can be divided by 'len'.
		for (int i = 0; i < code.length(); i++) {
			if(code.charAt(i) == ',')
				count++;
		}
		return (count/len) >= thresholdProportion;
	}

}
