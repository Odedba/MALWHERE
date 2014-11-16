package main.project.svm.features;

import workshop.tools.WhitespaceCounter;
import workshop.tools.svm.mal.js.detector.IFeature;

/***
 * The script’s whitespace percentage.
 */
public class WhitespacePercentage implements IFeature {

	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		int len = javascriptCode.length();
		if(len == 0) return 0;
		double whitespaceNum = WhitespaceCounter.countWhitespaces(javascriptCode);
		return (whitespaceNum/(double)len) * 100.0;
	}
}
