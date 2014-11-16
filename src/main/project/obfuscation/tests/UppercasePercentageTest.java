package main.project.obfuscation.tests;

import workshop.tools.CharactersIdentifier;
import workshop.tools.JSCommentRemover;


/**
 * This class handles one of the characteristics of obfuscated code.
 * In this case it checks if the code contains an unreasonable number
 * of UpperCase letters, as obfuscators tend to inject long strings
 * of UpperCase letters into the code. 
 */
public class UppercasePercentageTest implements ITest {
	
	/* Test passing threshold */
	private static final double thresholdProportion = 0.25;

	@Override
	public double getTestWeight() {
		return WeightType.VERY_HIGH.getWeight();
	}

	/**
	 * Main steps of the algorithm:
	 * 1. Count the number of UpperCase letters in code.
	 * 2. Divide it by the length of the source getting the proportion.
	 * 3. Compare it with threshold proportion.
	 * 
	 * @param javascriptPath - path to JavaScript source file.
	 * @param javascriptCode - the input program (as string).
	 * @return true if the proportion of UpperCase letters is large
	 * and we suspect the code as obfuscated, false otherwise.
	 */
	@Override
	public boolean isSuspicious(String javascriptPath, String javascriptCode) {
		/*
		 * Removing comments is necessary in this case, as clean
		 * code is required for correct analysis. 
		 */
		String code = JSCommentRemover.removeComments(javascriptCode);
		double codeLength = code.length();
		if(codeLength == 0)
			return false;
		double counter = 0.0;

		/* Computing the relative part of UpperCase letters in code. */
		for (int i = 0; i < codeLength; i++) {
			if(CharactersIdentifier.uppercaseLetters.contains(code.charAt(i)))
				counter++;
		}
		/*
		 *  We consider suspicious a code that 25% of it's 
		 * characters are UpperCase letters. 
		 */
		 return counter / codeLength >= thresholdProportion;
	}
}
