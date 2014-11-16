package main.project.svm.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import workshop.tools.ExpressionCounter;
import workshop.tools.JSStringExtractor;
import workshop.tools.svm.mal.js.detector.IFeature;


/**************************************************** 
 * The number of Strings containing suspicious words.
 ****************************************************/
public class SuspiciousStringNum implements IFeature {
	private static final HashSet<String> SUSPICIOUS_WORDS = new HashSet<String>(Arrays.asList(
			"payload", "bumb", "spray", "memory", "evil", "shell", "crypt", 
			"SocialGraphManager", "wow", "iframer", "MakeFrameEx"));


	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		ArrayList<String> strings = JSStringExtractor.extractFromFile(sourcePath);
		return ExpressionCounter.count(SUSPICIOUS_WORDS, strings);
	}

}
