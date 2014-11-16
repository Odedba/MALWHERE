package workshop.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class StringToFile {

	public static void write(String str, String outputPath){
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(outputPath));
			writer.write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if ( writer != null){
			try {
				writer.close( );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
