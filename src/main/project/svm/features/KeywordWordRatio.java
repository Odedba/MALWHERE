package main.project.svm.features;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import workshop.tools.JSCommentRemover;
import workshop.tools.JSKeywordExtractor;
import workshop.tools.JSStringExtractor;
import workshop.tools.RhinoASTBuilder;
import workshop.tools.svm.mal.js.detector.IFeature;

/***
 * The ratio between JS keywords and words in script.
 */
public class KeywordWordRatio implements IFeature {

	@Override
	public double featureValue(String sourcePath, String javascriptCode) {

		List<String> keywords = JSKeywordExtractor.extractKeywords(sourcePath);
		double numOfKeywords = keywords.size();
		
		for (String keyword : keywords) {
			if(keyword.equals("do")){
				numOfKeywords++;
			}
		}

		javascriptCode = JSStringExtractor.removeStringContents(sourcePath, javascriptCode);
		javascriptCode = JSCommentRemover.removeComments(javascriptCode);
		javascriptCode = javascriptCode.replace('\n', ' ');
				
		String regex = "[\\s]+(default)[\\s]*:[\\s]+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(javascriptCode);

		while(matcher.find()){
			numOfKeywords++;
		}
		
		regex = "}[\\s]*(else|else[\\s]*if[\\s]*\\(.*\\))[\\s]*\\{";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(javascriptCode);
		
		while(matcher.find()){
			numOfKeywords++;
		}
		
		regex = "}[\\s]*(finally)[\\s]*\\{";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(javascriptCode);

		while(matcher.find()){
			numOfKeywords++;
		}
		
		double numOfAbstractSyntaxTreeNodes = RhinoASTBuilder.getAST(sourcePath).size();
		return(numOfAbstractSyntaxTreeNodes == 0 ? 0 : numOfKeywords/numOfAbstractSyntaxTreeNodes);
	}


}


