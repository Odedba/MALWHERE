package main.project.svm.features;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import workshop.tools.JSFunctionCallExtractor;
import workshop.tools.svm.mal.js.detector.IFeature;

/***
 * The number of calls to string modification functions.
 */
public class StringModFuncNum implements IFeature{

	/* Functions for string manipulation */
	private static final Set<String> stringManipulationFunctions = new HashSet<String>(
			Arrays.asList("concat", "replace", "slice", "split", "substr",
					"substring", "trim", "toLowerCase", "toUpperCase"));
	
	@Override
	public double featureValue(String sourcePath, String javascriptCode) {

		List<String> calls = JSFunctionCallExtractor.extractFunctionCalls(sourcePath);
		double numOfCallsToFunctions = 0.0;
		for (String call : calls) {
			if(stringManipulationFunctions.contains(call))
				numOfCallsToFunctions++;
		}
		return numOfCallsToFunctions;
	}
	

}
