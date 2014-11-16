package main.project.obfuscation.tests;

/**
 * An interface for tests run by the JS obfuscation tester.
 * Every test in this package implements ITest.
 */
public interface ITest {
	
	/**
	 * The getTestWeight function returns one of the
	 * following values, included in the 'WeightType' enum:
	 * VERY_LOW (1.0)
	 * LOW (2.0)
	 * MEDIUM (3.0)
	 * HIGH (4.0)
	 * VERY_HIGH (5.0)
	 */
	public double getTestWeight();
	
	/**
	 * The implementation of the obfuscation test.
	 * isSuspicious is a boolean function that receives 
	 * the JavaScript source code and test for suspiciousness
	 * according to the test criteria. If the function returns 
	 * true (i.e test passed), the score returned from the
	 * getTestWeight function will be added to the total 
	 * obfuscation score of the code.
	 * 
	 * @param javascriptPath - path to JavaScript source file.
	 * @param javascriptCode - the JavaScript source code.
	 * @return true if the code passes the test, false otherwise.
	 */
	public boolean isSuspicious(String javascriptPath, String javascriptCode);

}
