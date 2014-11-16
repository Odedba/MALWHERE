package main.project.svm.features;

import java.util.List;

import workshop.tools.JSStringExtractor;
import workshop.tools.svm.mal.js.detector.IFeature;


/***
 * The number of long strings (>40).
 */
public class LongStringNum implements IFeature {
	
	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		List<String> strings = JSStringExtractor.extractFromFile(sourcePath);
		int counter = 0;
		for (String string : strings) {
			if(string.length() > 40){
				counter++;
			}
		}
		return counter;	
	}
	
	
}
