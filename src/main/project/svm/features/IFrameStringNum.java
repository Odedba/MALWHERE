package main.project.svm.features;

import java.util.HashSet;
import java.util.List;
import workshop.tools.ExpressionCounter;
import workshop.tools.JSStringExtractor;
import workshop.tools.svm.mal.js.detector.IFeature;

/********************************************* 
 * The number of strings containing "iframe".
 *********************************************/
public class IFrameStringNum implements IFeature {


	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		List<String> strings = JSStringExtractor.extractFromFile(sourcePath);
		int count = 0;

		HashSet<String> iframeExp = new HashSet<String>();
		iframeExp.add("iframe");

		count = ExpressionCounter.count(iframeExp, strings);

		return count;
	}

}
