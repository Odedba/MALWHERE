package main.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import main.project.obfuscation.tests.AllNamesTest;
import main.project.obfuscation.tests.AverageLineTest;
import main.project.obfuscation.tests.CommasPercentageTest;
import main.project.obfuscation.tests.DefinedNamesTest;
import main.project.obfuscation.tests.DigitsPercentageTest;
import main.project.obfuscation.tests.EncodedCharTest;
import main.project.obfuscation.tests.ExistenceOfReplaceTest;
import main.project.obfuscation.tests.ExistenceOfSuspiciousSplitTest;
import main.project.obfuscation.tests.GibberishTest;
import main.project.obfuscation.tests.HexaDecimalVariableTest;
import main.project.obfuscation.tests.ITest;
import main.project.obfuscation.tests.LongStringsTest;
import main.project.obfuscation.tests.StringEvaluationTest;
import main.project.obfuscation.tests.UglyCodeTest;
import main.project.obfuscation.tests.UppercasePercentageTest;
import main.project.obfuscation.tests.WhitespacePercentageTest;


/**
 * This class tests whether a given JS code is obfuscated or not 
 */
public class ObfuscationTester {

	/* Thresholds for test scores above which the code is considered obfuscated */
	private static final double defaultThreshold = 0.25;
	private static final double veryShortCodeThreshold = 0.1;
	private static final double shortCodeThreshold = 0.15;
	private static final double longCodeThreshold = 0.35;
	
	/* Thresholds for test scores above which the code is considered obfuscated */
	private static final int veryShortCode = 50;
	private static final int shortCode = 600;
	private static final int veryLongCode = 15000;
	
	/* List of tests to run */
	private static List<ITest> tests = new ArrayList<ITest>();
	/* Flag for default tests */
	private static boolean testsLoaded = false;


	/**
	 * Runs default obfuscation tests and calculates the score.
	 * 
	 * @param javascriptCode
	 * @return double score of the obfuscation test results
	 */
	public static double getScore(String javascriptPath, String javascriptCode){
		if(!testsLoaded){
			tests.add(new AllNamesTest());
			tests.add(new AverageLineTest());
			tests.add(new CommasPercentageTest());
			tests.add(new DefinedNamesTest());
			tests.add(new DigitsPercentageTest());
			tests.add(new EncodedCharTest());
			tests.add(new ExistenceOfReplaceTest());
			tests.add(new ExistenceOfSuspiciousSplitTest());
			tests.add(new GibberishTest());
			tests.add(new HexaDecimalVariableTest());
			tests.add(new LongStringsTest());
			tests.add(new StringEvaluationTest());
			tests.add(new UglyCodeTest());
			tests.add(new UppercasePercentageTest());
			tests.add(new WhitespacePercentageTest());
			
			testsLoaded = true;
		}
		return getScore(javascriptPath, javascriptCode, null);
	}


	/**
	 * Runs given obfuscation tests and calculates the score.
	 * 
	 * @param javascriptCode
	 * @param testCollection
	 * @return double score of the obfuscation test results
	 */
	public static double getScore(String javascriptPath, String javascriptCode, Collection<ITest> testCollection){
		double score = 0;
		double maxScore = 0;
		if(testCollection != null){
			testCollection.addAll(testCollection);
		}
		for (ITest test : tests) {
			maxScore+= test.getTestWeight();
			if(test.isSuspicious(javascriptPath, javascriptCode)){
				score+= test.getTestWeight(); 
			}
		}
		System.out.println("Obfscation Score: "+score+" out of "+maxScore);
		return (score == 0 ? 0 : score/maxScore);
	}

	/**
	 * Determines whether the JS code is obfuscated according to the
	 * default threshold.
	 * 
	 * @param javascriptPath
	 * @param javascriptCode
	 * @return true if code is obfuscated, false otherwise
	 */
	public static boolean isObfuscated(String javascriptPath, String javascriptCode){
		return isObfuscated(javascriptPath, javascriptCode, defaultThreshold);
	}
	
	/***
	 * Clear loaded tests.
	 */
	public static void clearTests(){
		tests.clear();
		testsLoaded = false;
	}


	/**
	 * Determines whether the JS code is obfuscated according to 
	 * the given threshold.
	 * 
	 * <p>IMPORTANT NOTE - in order for the test to be meaningful, the threshold
	 * value must be consistent with the weights of the tests.
	 * 
	 * @param javascriptCode
	 * @param threshold
	 * @return true if code is obfuscated, false otherwise
	 */
	public static boolean isObfuscated(String javascriptPath, String javascriptCode, double threshold){
		if(javascriptCode.length() < shortCode)
			threshold = shortCodeThreshold;
		if(javascriptCode.length() < veryShortCode)
			threshold = veryShortCodeThreshold;
		if(javascriptCode.length() > veryLongCode)
			threshold = longCodeThreshold;
		
		double score = getScore(javascriptPath, javascriptCode);
		if (score >= threshold){
			return true;
		}
		return false;
	}

}
