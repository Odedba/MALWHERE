package main.project.svm.features;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import workshop.tools.ExpressionCounter;
import workshop.tools.JSStringExtractor;
import workshop.tools.svm.mal.js.detector.IFeature;

/*************************************************************************** 
 * The number of suspicious tag strings (This feature counts the appearance 
 * of: 'script', 'object', 'embed', and 'frame' inside JavaScript strings).
 ***************************************************************************/
public class SuspiciousTagNum implements IFeature{
	private static final HashSet<String> TAGS = new HashSet<String>(Arrays.asList(
			"script", "object", "embed", "frame"));
			
	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		List<String> strings = JSStringExtractor.extractFromFile(sourcePath);		
		return ExpressionCounter.count(TAGS, strings);
	}

}
