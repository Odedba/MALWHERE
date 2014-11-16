package workshop.tools;

import java.io.File;
import java.util.Stack;

public class JSCommentRemover {


	/***
	 * Removes all comments from the source code.
	 * 
	 * @param sourceCode - the source code
	 * @return source code without comments
	 */
	public static String removeComments(String sourceCode){
		//initialization
		String result = "";
		char[] charCode = sourceCode.toCharArray();
		Stack<Character> quoteStack = new Stack<Character>();

		//rebuilding code while omitting comments
		for (int i = 0; i < charCode.length; i++) {
			char prev = (i-1 > 0 ? charCode[i-1] : '\0');
			char current = charCode[i];
			char next = (i+1 < charCode.length ? charCode[i+1] : '\0');


			//found quotation marks
			if(current == '\'' || current == '"'){

				//not escaped quotation marks
				if(prev != '\\'){

					//opening quotation
					if(quoteStack.isEmpty()){
						quoteStack.push(current);
					}

					//closing previously opened quotation
					else if(current == quoteStack.peek()){
						quoteStack.pop();
					}

				}
			}

			//found start of comment
			else if(current == '/' && quoteStack.isEmpty()){

				//handle single-line comment
				if(next == '/'){
					while(i+1 < charCode.length && charCode[i+1] != '\n'){
						i++;
					}
					continue;
				}

				//handle comment block
				else if(next == '*'){
					while(i+1 < charCode.length && !(charCode[i] == '*' && charCode[i+1] == '/')){
						i++;
					}
					result+= " ";
					
					//skipping '/' at index i+1
					i++;
					continue;
				}
			}

			result+= current; 
		}

		return result;
	}


	public static void main(String[] args) {
		String str;
		String sourceCode;
		File folder;
		File[] files;
		try{

			folder = new File(".\\examples\\benign_training_set");
			files = folder.listFiles();
			for (File file : files) {
				if(file.getPath().endsWith(".tmp")){
					continue;
				}
				sourceCode = FileToString.read(file);
				str = removeComments(sourceCode);
				StringToFile.write(str, file.getPath()+".tmp");
			}

			folder = new File(".\\examples\\suspicious_training_set");
			files = folder.listFiles();
			for (File file : files) {
				if(file.getPath().endsWith(".tmp")){
					continue;
				}
				sourceCode = FileToString.read(file);
				str = removeComments(sourceCode);
				StringToFile.write(str, file.getPath()+".tmp");
			}

			folder = new File(".\\examples\\malicious_training_set");
			files = folder.listFiles();
			for (File file : files) {
				if(file.getPath().endsWith(".tmp")){
					continue;
				}
				sourceCode = FileToString.read(file);
				str = removeComments(sourceCode);
				StringToFile.write(str, file.getPath()+".tmp");
			}


		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
