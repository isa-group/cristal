package es.us.isa.cristal.performance.tester.exporters;

import es.us.isa.cristal.organization.model.gson.OrganizationalModel;
import es.us.isa.cristal.performance.tester.data.ExperimentConfiguration;
import es.us.isa.cristal.performance.tester.data.OperationExecutionAggregator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Manuel Leon
 *
 */
public interface Exporter {
	
	void export(String filename, ExperimentConfiguration edata, OrganizationalModel model);
    void export(OutputStream outputStream, List<OperationExecutionAggregator> executions, Map<String, String> parameters) throws IOException;
	
	String getExtensionIdentifier();
}
