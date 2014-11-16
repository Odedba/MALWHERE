package workshop.tools;

import java.util.Hashtable;
import java.util.Map;

public class StringEntropyCalc {
	
	/***
	 * <summary>
	 * Returns the metric entropy of bits represented in a given string, per:
	 * <p><i>http://en.wikipedia.org/wiki/Entropy_(information_theory)</i> 
	 * 
	 * @param str - The String to inspect.
	 * @return The metric entropy
	 */
	public static double calc(String str){
		Map<Character, Integer> map = new Hashtable<Character, Integer>();
		for (int i = 0; i < str.length(); i++) {
			Character c = str.charAt(i);
			if(map.get(c) == null){
				map.put(c, 1);
			}
			else{
				map.put(c, map.get(c)+1);
			}
		}
		double result = 0.0;
		int len = str.length();
		for (Character c : map.keySet()) {
			double frequency = (double)map.get(c) / len;
			result-= frequency * (Math.log(frequency) / Math.log(2));
		}
		return (result != 0 ? result / len : result);
	}

}
