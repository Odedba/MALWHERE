package main.project.svm.features;

import java.util.List;

import workshop.tools.JSStringExtractor;
import workshop.tools.StringEntropyCalc;
import workshop.tools.svm.mal.js.detector.IFeature;

/***
 * The maximum entropy of all the script’s strings.
 */
public class MaxStringEntropy implements IFeature {

	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		List<String> strings = JSStringExtractor.extractFromFile(sourcePath);
		double maxEntropy = 0;

		for (String s : strings) {
			double entropy = StringEntropyCalc.calc(s);
			maxEntropy = (maxEntropy > entropy ? maxEntropy : entropy);
		}
		return maxEntropy;
	}

}
