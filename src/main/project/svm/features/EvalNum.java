package main.project.svm.features;

import java.util.List;

import workshop.tools.JSFunctionCallExtractor;
import workshop.tools.svm.mal.js.detector.IFeature;

/***
 * The number of calls to eval() function.
 */
public class EvalNum implements IFeature{

	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		List<String> calls = JSFunctionCallExtractor.extractFunctionCalls(sourcePath);
		double numOfCallsToEval = 0.0;
		for (String call : calls) {
			if(call.equals("eval"))
				numOfCallsToEval++;
		}
		return numOfCallsToEval;
	}
}
