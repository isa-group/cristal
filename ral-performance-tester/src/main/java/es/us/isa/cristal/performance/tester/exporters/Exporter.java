package es.us.isa.cristal.performance.tester.exporters;

import es.us.isa.cristal.organization.model.gson.OrganizationalModel;
import es.us.isa.cristal.performance.tester.data.ExecutionData;

/**
 * 
 * @author Manuel Leon
 *
 */
public interface Exporter {
	
	void export(String filename, ExecutionData edata, OrganizationalModel model);
	
	String getExtensionIdentifier();
	
}
