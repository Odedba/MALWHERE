
package main.project.obfuscation.tests;

import java.util.List;

import workshop.tools.JSCommentRemover;
import workshop.tools.JSNameExtractor;
import workshop.tools.JSStringExtractor;

/**
 * This class implements an obfuscation test.
 * In this case it checks if the code contains a suspiciously high number 
 * of hexadecimal variables, as obfuscators tend to replace variable names
 * with hexadecimal numbers. We use the AST of the script in order to
 * extract all variable names.
 */
public class HexaDecimalVariableTest implements ITest {

	/* 
	 * These constants represent the thresholds above which the score 
	 * of the test will be added to the total obfuscation score.
	 */
	private static final int numOfHexadecimalsThreshold = 5;
	private static final int numOfHexadecimalsLowThreshold = 2;
	
	/* Global counter */ 
	private static int counter = 0;
	
	@Override
	public double getTestWeight() {
		if(counter >= numOfHexadecimalsThreshold)
			return WeightType.VERY_HIGH.getWeight();
		
		return WeightType.MEDIUM.getWeight();
	}

	/**
	 * The implementation of the test: count the appearances
	 * of hexadecimal numbers (based on AST and "pure" code),
	 * comparing it to threshold.
	 * 
	 * @param javascriptPath - path to JavaScript source file.
	 * @param javascriptCode - the input program (as string).
	 * @return true if we found enough hexadecimal numbers in code 
	 * according to the defined threshold, and false otherwise.
	 */
	@Override
	public boolean isSuspicious(String javascriptPath, String javascriptCode) {
		counter = 0;
		
		/* All names in script */
		List<String> names = JSNameExtractor.extractNames(javascriptPath);
		for (String name : names) {
			if(name.startsWith("_0x") || name.startsWith("0x") || name.startsWith("_0X") || name.startsWith("0X"))
				counter++;
		}
		if(counter >= numOfHexadecimalsThreshold)
			return true;
		/*
		 * Removing comments and string contents is necessary in this case,
		 * as clean code is required for correct analysis. 
		 */
		String code = JSCommentRemover.removeComments(javascriptCode);
		code = JSStringExtractor.removeStringContents(javascriptPath, javascriptCode);
		
		for (int i = 0; i < code.length(); i++) {
			String sub = code.substring(i); 
			if(sub.startsWith("0x") || sub.startsWith("0X"))
				counter++;
		}
		/*
		 * A code that contains a number of hexadecimal numbers that is above
		 * predefined threshold is considered suspicious.
		 */
		return counter >= numOfHexadecimalsLowThreshold;
	}
}
