package es.us.isa.cristal.performance.tester.exporters;

import es.us.isa.cristal.organization.model.gson.Model;
import es.us.isa.cristal.performance.tester.data.ExecutionData;

public interface Exporter {
	
	void export(String filename, ExecutionData edata, Model model);
	
	String getExtensionIdentifier();
	
}
