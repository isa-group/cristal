package es.us.isa.cristal.activiti.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class IOUtil {
	
	
	public static String getURLContent(String url) throws IOException{
		URL urlo = new URL(url);
		InputStream is = urlo.openStream();
		String result = IOUtil.convertStreamToString(is).trim();
		return result;
	}
	
	public static String convertStreamToString(InputStream is) {
	    
		Scanner sc = new Scanner(is);
		sc.useDelimiter("\\A");
	    return sc.hasNext() ? sc.next() : "";
	}
	
}
