package es.us.isa.cristal.analyser.web.services.impl;

import java.io.IOException;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import es.us.isa.cristal.analyser.web.services.DataAccessService;
import es.us.isa.cristal.organization.model.util.IOUtil;

/**
 * 
 * @author Manuel Leon
 *
 */
@Service("cacheService")
public class DataAccessServiceImpl implements DataAccessService {

	@Cacheable(value = "defaultCache", key = "#url")
	public String getContentFromUrl(String url)  throws IOException {
		System.out.println("DENTRO DE GET CONTENT: " + url);
		return IOUtil.getURLContent(url);
	}

	

}
