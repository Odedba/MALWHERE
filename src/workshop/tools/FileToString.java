package workshop.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileToString {

	/**
	 * Extracts all text from the given File and returns it in US-ASCII encoding. 
	 * 
	 * @param file - the source file
	 * @return text from file in US-ASCII encoding
	 * @throws IOException on fail of reading file
	 */
	public static String read(File file) throws IOException{
		String sourceFileStr = "";
		FileInputStream is = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(is, "US-ASCII");
		BufferedReader buffReader = new BufferedReader(isr);
		String line = null;
		String lastLine = null;
		while((line = buffReader.readLine()) != null){
			if(lastLine != null){
				sourceFileStr+= lastLine+"\n";
			}
			lastLine = line;
		}

		if(lastLine != null){
			sourceFileStr+= lastLine;
		}
		buffReader.close();
		return sourceFileStr;
	}
	

}
