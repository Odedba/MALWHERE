package main.project.svm.features;

import java.util.List;

import workshop.tools.JSFunctionCallExtractor;
import workshop.tools.svm.mal.js.detector.IFeature;

/***
 * The number of calls to SetTimeout function
 */
public class SetTimeoutNum implements IFeature {

	@Override
	public double featureValue(String sourcePath, String javascriptCode) {

		List<String> calls = JSFunctionCallExtractor.extractFunctionCalls(sourcePath);
		double numOfCallsToSetTimeout = 0.0;
		for (String call : calls) {
			if (call.equals("SetTimeout"))
				numOfCallsToSetTimeout++;
		}
		return numOfCallsToSetTimeout;
	}

}
