package main.project.obfuscation.tests;

import workshop.tools.JSLineCounter;
import workshop.tools.js.beautifier.JSBeautifier;

/**
 * This class implements a test dealing with source code appearance.
 * We include in our project the JSBeautifier tool which
 * is capable of making a JavaScript code more readable. 
 * For example, it will separate lines where needed and
 * organize the lines according to correct indentation.
 * Codes that gone through obfuscation tend to be compact
 * (sometimes even minified). Thus, measuring the difference
 * between a code and the beautified version of the same code 
 * is valuable when addressing the obfuscated/not-obfuscated
 * problem.   
 */
public class UglyCodeTest implements ITest{

	private static final int inflationFactor = 2;
	
	@Override
	public double getTestWeight() {
		return WeightType.VERY_HIGH.getWeight();
	}

	/**
	 * Algorithm main steps:
	 * 1. execute the JSbeautifier on source code.
	 * 2. compare the resulting code with source code.
	 * 3. pass the test if the length of the resulting
	 * code is significantly larger than the length of 
	 * source.
	 * 
	 * @param javascriptPath - path to JavaScript source file.
	 * @param javascriptCode - the input program (as string).
	 * @return true if the code has changed dramatically 
	 * after executing the beautifier, false otherwise.
	 */
	@Override
	public boolean isSuspicious(String javascriptPath, String javascriptCode) {
		
		JSBeautifier beautifier = new JSBeautifier();
		String result = beautifier.js_beautify(javascriptCode, null);
		
		double originalLineCounter = JSLineCounter.lineCounter(javascriptCode);
		double beautifiedLineCounter = JSLineCounter.lineCounter(result);
		
		/* 
		 * We consider suspicious JavaScript codes which their 
		 * beautified version inflate the code size by factor of 
		 * at least 'inflationFactor' value.
		 */
		if(javascriptCode.startsWith("/*")) beautifiedLineCounter++;
		return (beautifiedLineCounter >= inflationFactor * originalLineCounter);
	}

}
