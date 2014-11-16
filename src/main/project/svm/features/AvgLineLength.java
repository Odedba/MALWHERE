package main.project.svm.features;

import workshop.tools.JSLineCounter;
import workshop.tools.svm.mal.js.detector.IFeature;

/***
 * The average script line length.
 */
public class AvgLineLength implements IFeature {

	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		double numOfLines = JSLineCounter.lineCounter(javascriptCode);
		return(numOfLines == 0 ? 0 : javascriptCode.length()/numOfLines);
	}
}
