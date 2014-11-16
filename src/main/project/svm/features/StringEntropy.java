package main.project.svm.features;

import java.util.List;

import workshop.tools.JSStringExtractor;
import workshop.tools.StringEntropyCalc;
import workshop.tools.svm.mal.js.detector.IFeature;

/***
 * The entropy of the strings declared in the script.
 * 
 */
public class StringEntropy implements IFeature {

	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		List<String> strings = JSStringExtractor.extractFromFile(sourcePath);
		int lenSum = 0;
		double entropy = 0;
		for (String s : strings) {
			lenSum+= s.length();
			entropy+= StringEntropyCalc.calc(s) * s.length();
		}
		return (lenSum == 0 ? 0 : entropy/(double)lenSum);
	}


}
