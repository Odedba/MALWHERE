package workshop.tools;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLExtractor {
	
	
	/**
	 * Extract all URLs from the text using regex and pattern matching.
	 * 
	 * @param text - The text to extract URLs from.
	 * @return A List of URLs.
	 */
	public static ArrayList<String> extract(String text) {
		ArrayList<String> links = new ArrayList<String>();

		String regex = "\\(?\\b(https?://|www[.]|ftp://)[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);

		while(m.find()) {
			String urlStr = m.group();

			if (urlStr.startsWith("(") && urlStr.endsWith(")")){
				urlStr = urlStr.substring(1, urlStr.length() - 1);
			}

			if(!urlStr.isEmpty()){
				links.add(urlStr);
			}
		}
		return links;
	}
	

}
