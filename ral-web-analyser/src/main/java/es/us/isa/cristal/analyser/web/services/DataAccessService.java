package es.us.isa.cristal.analyser.web.services;

import java.io.IOException;

/**
 * 
 * @author Manuel Leon
 *
 */
public interface DataAccessService {
	
	String getContentFromUrl(String url, String key)  throws IOException;
	
}
