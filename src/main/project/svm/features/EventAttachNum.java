package main.project.svm.features;

import java.util.List;
import workshop.tools.JSFunctionCallExtractor;
import workshop.tools.svm.mal.js.detector.IFeature;

/***
 * The number of event attachments.
 */
public class EventAttachNum implements IFeature {

	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		double eventCounter = 0;
		
		/* All script's function calls */
		List<String> calls = JSFunctionCallExtractor
				.extractFunctionCalls(sourcePath);
		for (String call : calls) {
			if (call.equals("addEventListener") || call.equals("attachEvent")
					|| call.equals("dispatchEvent") || call.equals("fireEvent"))
				eventCounter++;
		}
		return eventCounter;
	}

}
