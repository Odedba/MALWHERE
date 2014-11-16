package workshop.tools;

/**
 *  Count the number of lines in code
 */
public class JSLineCounter {
	
	public static double lineCounter(String code) {
		int counter = 0;
		if(code.equals(""))
			return 0;
		for (int i = 0; i < code.length(); i++) {
			if(code.charAt(i) == '\n' || code.charAt(i) == '\r')
				counter++;
		}
		if(!code.equals("") && counter == 0)
			return 1;
		return counter + 1;	
	}
}
