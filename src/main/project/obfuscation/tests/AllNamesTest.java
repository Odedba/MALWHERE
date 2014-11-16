package main.project.obfuscation.tests;

import java.io.FileNotFoundException;
import java.util.List;

import workshop.tools.JSNameExtractor;

/**
 * This class implements an obfuscation test for script's names.
 * It extends the AbstractNames abstract class and inherits it's 
 * name analyzing functionality.
 * In this case, we extract all script's names (i.e names defined by 
 * the script's author, names of function parameters, names of loop
 * indexes, names of JavaScript built in functions etc.)
 * and compute the proportion of the non-legitimate ones.   
 */
public class AllNamesTest extends AbstractMeaningOfNames implements ITest{

	/* Thresholds for the test: low, medium, high */
	private static final double lowProportion = 0.5;
	private static final double mediumProportion = 0.65;
	private static final double highProportion = 0.75;
	
	/* Script's proportion of suspicious names */ 
	private static double proportion = 0.0;

	
	@Override
	public double getTestWeight() {
		if(proportion >= highProportion)
			return WeightType.HIGH.getWeight();
		
		if(proportion >= mediumProportion)
			return WeightType.MEDIUM.getWeight();
		
		return WeightType.LOW.getWeight();
	}

	/**
	 * Main steps of the algorithm:
	 * 1. Load the pull of words into memory, making sure it stays right 
	 * there until the end of the run (loading over and over a file contains
	 * 355,000 words is not recommended...)
	 * 2. Use an AST based tool to extract all names in script.
	 * 3. Break each name into parts according to CamelCase and under_score
	 * conventions. For example, stopTimer will be split to stop and timer, 
	 * and number_of_points will be split to number, of and points.    
	 * 4. check the obtained words against the pull, decide if the identifier 
	 * name is legitimate. 
	 * 5. compute the suspicious names ratio, and compare with threshold.
	 * 
	 * @param javascriptPath - path to JavaScript source file.
	 * @param javascriptCode - the input program (as string).
	 * @return true if we found a high ratio of suspicious names,
	 * and false otherwise.  
	 */
	@Override
	public boolean isSuspicious(String javascriptPath, String javascriptCode) {
		/* Load the pull of words into memory */
		try {
			loadPullOfWords();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		/* Extract all names from script */
		List<String> names = JSNameExtractor.extractNames(javascriptPath);
		if(names.isEmpty())
			return false;
		
		proportion = 0.0;
		double numOfSuspiciousIdentifiers = 0.0;
		int numOfNames = names.size();
		
		for (String name : names) {
			if(!isLegitimateName(name))
				numOfSuspiciousIdentifiers++;
		}
		/* We consider suspicious a script which half of it's names are not legitimate */
		proportion = numOfSuspiciousIdentifiers / numOfNames;
		return proportion >= lowProportion;
	}
}
