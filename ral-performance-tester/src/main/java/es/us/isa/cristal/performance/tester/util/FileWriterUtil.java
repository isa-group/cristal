package es.us.isa.cristal.performance.tester.util;

import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * 
 * @author Manuel Leon
 *
 */
public class FileWriterUtil {

	public static void writeStringToFile(String filename, String text) {
		FileOutputStream stream = null;
		PrintStream out = null;
		try {
			
			stream = new FileOutputStream(filename);
			out = new PrintStream(stream);
			out.print(text); 
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (stream != null)
					stream.close();
				if (out != null)
					out.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
