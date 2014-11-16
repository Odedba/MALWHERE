package main.project.svm.features;

import java.util.List;

import workshop.tools.JSStringExtractor;
import workshop.tools.svm.mal.js.detector.IFeature;

/***
 * The average length of the strings used in the script.
 */
public class AvgStringLength implements IFeature {

	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		List<String> strings = JSStringExtractor.extractFromFile(sourcePath);
		int sum = 0;
		int count = 0;
		for (String string : strings) {
			sum+= string.length();
			count++;
		}
		return (count == 0 ? 0 : sum/(double)count);
	}
	

}
