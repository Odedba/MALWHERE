package main.project.svm.features;

import java.util.List;
import java.util.regex.PatternSyntaxException;
import workshop.tools.JSStringExtractor;
import workshop.tools.svm.mal.js.detector.IFeature;


/****************************************************** 
 * The probability of the script to contain ShellCode.
 ******************************************************/
public class ShellcodeProbability implements IFeature {
	private static final int LONG_STRING = 40; 

	/***
	 * Counts the number of Non-Printable characters ( < 0x20 or > 0x7E ).
	 * 
	 * @param str - The String to inspect.
	 * @return Number of Non-Printable characters.
	 */
	private int countNonPrintables(String str){
		int counter = 0;
		for (int i = 0; i < str.length(); i++) {
			int cVal = str.charAt(i);
			if(cVal < 32 || cVal > 126){
				counter++;
			}
		}
		return counter;

	}

	/***
	 * Checks if the string is a consecutive block of characters in the ranges 
	 * a-f, A-F, 0-9.
	 * 
	 * @param str - The String to inspect.
	 * @return True if is a consecutive block of characters in said ranges, False otherwise.
	 */
	private boolean onlyHexChars(String str){
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if(c < '0' || (c > '9' && c < 'A') || (c > 'F' && c < 'a') || c > 'f'){
				return false;
			}
		}
		return true;
	}

	/***
	 * Checks if certain characters are repeated at regular intervals in the string.
	 * 
	 * @param str - The String to inspect.
	 * @return True if the characters are repeated, False otherwise.
	 */
	private boolean hasRepeatingChars(String str){
		for (int i = 32; i < 127; i++) {
			String regex = Character.toString ((char) i);
			String[] strSegments = new String[0];

			try{
				strSegments = str.split(regex);
			}
			catch(PatternSyntaxException e){
				strSegments = str.split("\\"+regex);
			}

			int countRepeat = 0;
			for (int j = 0; j < strSegments.length-1; j++) {
				if(strSegments[j].length() == strSegments[j+1].length()){
					countRepeat++;
				}
			}

			if(countRepeat == strSegments.length-1 && countRepeat > 1){
				return true;
			}
		}
		return false;
	}


	@Override
	public double featureValue(String sourcePath, String javascriptCode) {
		List<String> strings = JSStringExtractor.extractFromFile(sourcePath);
		double result = 0.0;
		for (String str : strings) {
			if(str.length() > LONG_STRING){
				double m1Result = countNonPrintables(str)/(double)str.length();
				double m2Result = (onlyHexChars(str) ? 1.0 : 0.0);
				double m3Result = (hasRepeatingChars(str) ? 1.0 : 0.0);
				double thisResult = (m1Result+m2Result+m3Result) / 3.0;
				result = Math.max(result, thisResult);
			}
		}

		return result;
	}
	

}
