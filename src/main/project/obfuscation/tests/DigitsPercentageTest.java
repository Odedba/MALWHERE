package main.project.obfuscation.tests;

import workshop.tools.CharactersIdentifier;
import workshop.tools.JSCommentRemover;

/**
 * This class handles one of the characteristics of obfuscated code.
 * It recognizes source codes with a suspiciously high percentage 
 * of digit characters.  
 */
public class DigitsPercentageTest implements ITest {
	
	/* Test passing thresholds */
	private static final double highThresholdProportion = 0.43;
	private static final double mediumThresholdProportion = 0.2;
	private static final double lowThresholdProportion = 0.15;

	/* Test passing thresholds */
	private static double globalProportion = 0.0;
	
	@Override
	public double getTestWeight() {
		if(globalProportion > highThresholdProportion)
			return WeightType.VERY_HIGH.getWeight();
		if(globalProportion > mediumThresholdProportion)
			return WeightType.HIGH.getWeight();
		
		return WeightType.MEDIUM.getWeight();
	}

	/**
	 * Main steps of the algorithm:
	 * 1. Count the number of digits in code.
	 * 2. Divide it by the length of the source getting the proportion.
	 * 3. Compare it with threshold proportion.
	 * 
	 * @param javascriptPath - path to JavaScript source file.
	 * @param javascriptCode - the input program (as string).
	 * @return true if the digits proportion is large and we suspect
	 * the code as obfuscated, false otherwise.
	 */
	@Override
	public boolean isSuspicious(String javascriptPath, String javascriptCode) {
		/*
		 * Removing comments is necessary in this case, as clean
		 * code is required for correct analysis. 
		 */
		String code = JSCommentRemover.removeComments(javascriptCode);
		globalProportion = 0.0;
		int counter = 0;
		double len = code.length();
		if(len == 0)
			return false; // can be divide by 'len'.
		for (int i = 0; i < code.length(); i++) {
			if(CharactersIdentifier.digits.contains(code.charAt(i)))
				counter++;
		}
		globalProportion = (counter/len);
		return globalProportion >= lowThresholdProportion;
	}

}