package main.project;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class MinifiedTester {
	

	/***
	 * Single line valid comment search.
	 * 
	 * @param str - The string to inspect.
	 * @return True if a comment is found in a single line of JavaScript, 
	 * 		   False otherwise.
	 */
	private static boolean hasComment(String str){
		Stack<Character> quoteStack = new Stack<Character>();

		for (int i = 0; i < str.length()-1; i++) {
			Character c1 = str.charAt(i);
			Character c2 = str.charAt(i+1);

			if(c1 == '\'' || c1 == '\"'){
				if(quoteStack.isEmpty()){
					quoteStack.push(c1);
				}
				else if(c1 == quoteStack.peek()){
					quoteStack.pop();
				}
			}
			else if(c1 == '/' && (c2 == '/' || c2 == '*')){
				if(quoteStack.isEmpty()){
					return true;
				}
			}
		}
		return false;
	}

	
	/**
	 * Checks for redundant whitespace characters.
	 * 
	 * @param javascriptCode - The code to inspect.
	 * @return - True if has redundant whitespace characters, False otherwise.
	 */
	private static boolean hasRedundantWhitespace(String javascriptCode){
		Stack<Character> quoteStack = new Stack<Character>();

		/*checks for leading whitespace*/
		if (!javascriptCode.isEmpty() && Character.isWhitespace(javascriptCode.charAt(0))){
			return true;
		}
		/*checks for sequential whitespace characters or tabs*/
		if (javascriptCode.contains("  ") || javascriptCode.contains("\t")){
			return true;
		}
		char[] arr = javascriptCode.toCharArray();
		/*set of chars to identify whitespace around*/
		Set<Character> charSet = new HashSet<Character>(Arrays.asList
				('{','}','(',')',';',',','=','+','-','/','*','%','|','&','^','!',':','?','<','>','~'));
		/*main loop to find whitespace*/
		for (int i = 0; i < arr.length; i++) {	
			Character c = arr[i];
			if(c == '\'' || c == '\"'){
				if(quoteStack.isEmpty()){
					quoteStack.push(c);
				}
				else if(c == quoteStack.peek()){
					quoteStack.pop();
				}
			}

			if (charSet.contains(arr[i])){
				/*check for whitespace indices prior to chars*/
				if (i > 0 && Character.isWhitespace(arr[i-1])){
					if(quoteStack.isEmpty()){
						return true;
					}
				}
				/*check for whitespace indices after chars*/
				if (i < arr.length-1 && Character.isWhitespace(arr[i+1])){
					if(quoteStack.isEmpty()){
						return true;
					}
				}
			}
		}
		return false;
	}
	

	/**
	 * Checks if the given JavaScript code is minified.
	 * 
	 * @param javascriptCode - The code to inspect.
	 * @return True if the code is minified, False otherwise.
	 */
	public static boolean isMinified(String javascriptCode){
		/*test 1 - more than one line*/
		if (javascriptCode.contains("\n")){
			System.out.println("Test 1 failed - more than one line");
			return false;
		}

		/*removing escaped quotation marks*/
		javascriptCode = javascriptCode.replaceAll("\\\\'", "xx");
		javascriptCode = javascriptCode.replaceAll("\\\\\"", "xx");


		/*test 2 - contains comments*/
		if(hasComment(javascriptCode)){
			System.out.println("Test 2 failed - contains comments");
			return false;
		}
		/*test 3 - contains a redundant whitespace between valid expressions*/
		if (hasRedundantWhitespace(javascriptCode)){
			System.out.println("Test 3 failed - contains a redundant whitespace between valid expressions");
			return false;
		}
		return true;
	}
}
