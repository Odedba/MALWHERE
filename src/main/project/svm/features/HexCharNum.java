package main.project.svm.features;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import workshop.tools.JSCommentRemover;
import workshop.tools.svm.mal.js.detector.IFeature;

/****************************** 
 * The number of chars in hex.
 ******************************/
public class HexCharNum implements IFeature {

	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		javascriptCode = JSCommentRemover.removeComments(javascriptCode);
		int counter = 0;
		
		String regex = ".*0[x|X][0-9|a-f|A-F]+.*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(javascriptCode);
		
		while(matcher.find()){
			counter++;
		}
				
		return counter;
	}

	
}
