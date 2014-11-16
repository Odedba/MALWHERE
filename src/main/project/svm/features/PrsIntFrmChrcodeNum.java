package main.project.svm.features;

import java.util.List;
import workshop.tools.JSFunctionCallExtractor;
import workshop.tools.svm.mal.js.detector.IFeature;

/***
 * The number of calls to parseInt and fromCharCode functions
 */
public class PrsIntFrmChrcodeNum implements IFeature {

	@Override
	public double featureValue(String sourcePath, String javascriptCode) {

		List<String> calls = JSFunctionCallExtractor.extractFunctionCalls(sourcePath);
		double numOfCallsToFunctions = 0.0;
		for (String call : calls) {
			if(call.equals("parseInt") || call.equals("fromCharCode"))
				numOfCallsToFunctions++;
		}
		return numOfCallsToFunctions;	
	}

}
