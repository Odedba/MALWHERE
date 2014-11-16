package main.project.obfuscation.tests;

import java.io.FileNotFoundException;
import java.util.List;
import workshop.tools.JSDefinedNamesExtractor;

/**
 * This class implements an obfuscation test for script's names.
 * It extends the AbstractNames abstract class and inherits it's 
 * name analyzing functionality.
 * In this case, we extract all names defined by the author of the script
 * (i.e names defined using the patterns: <var> name / <function> name), 
 * and compute the proportion of the non-legitimate ones.   
 */
public class DefinedNamesTest extends AbstractMeaningOfNames implements ITest{

	/*Test passing threshold */
	private static double scriptThreshold = 0.5;
	
	@Override
	public double getTestWeight() {
		return WeightType.MEDIUM.getWeight();
	}

	/**
	 * Main steps of the algorithm:
	 * 1. Load the pull of words into memory, making sure it stays right 
	 * there until the end of the run (loading over and over a file contains
	 * 355,000 words is not recommended...)
	 * 2. Use an AST based tool to extract all identifier definitions.
	 * 3. Break each name into parts according to CamelCase and under_score
	 * conventions. For example, stopTimer will be split to stop and timer, 
	 * and number_of_points will be split to number, of and points.    
	 * 4. check the obtained words against the pull, decide if the identifier 
	 * name is legitimate. 
	 * 5. compute the suspicious names ratio, comparing it with threshold.
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
		/* Extract the defined identifiers */
		List<String> definedNames = JSDefinedNamesExtractor.extractDefinedNames(javascriptPath);
		if(definedNames.isEmpty())
			return false;
		
		double numOfSuspiciousIdentifiers = 0.0;
		int numOfIdentifiersInScript = definedNames.size();
		
		for (String name : definedNames) {
			if(!isLegitimateName(name))
				numOfSuspiciousIdentifiers++;
		}
		/* We consider suspicious a script which half of it's defined names are not legitimate */
		return (numOfSuspiciousIdentifiers / numOfIdentifiersInScript > scriptThreshold);
	}
}
