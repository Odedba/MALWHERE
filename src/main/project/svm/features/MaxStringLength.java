package main.project.svm.features;

import java.util.List;

import workshop.tools.JSStringExtractor;
import workshop.tools.svm.mal.js.detector.IFeature;

/***
 * The maximum length of the script’s strings.
 */
public class MaxStringLength implements IFeature {

	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		List<String> strings = JSStringExtractor.extractFromFile(sourcePath);
		int max = 0;
		for (String string : strings) {
			max = (max > string.length() ? max : string.length());	
		}
		return max;
	}
	

}
