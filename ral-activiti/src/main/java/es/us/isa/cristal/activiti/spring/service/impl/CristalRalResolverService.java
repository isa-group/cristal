package es.us.isa.cristal.activiti.spring.service.impl;

import java.io.File;
import java.io.IOException;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.util.StringLogger;
import org.springframework.stereotype.Service;

import es.us.isa.cristal.activiti.ActivitiBPEngine;
import es.us.isa.cristal.activiti.spring.service.RalResolverService;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;
import es.us.isa.cristal.organization.model.gson.Document;
import es.us.isa.cristal.organization.model.util.IOUtil;

@Service("cristalRalResolverService")
public class CristalRalResolverService implements RalResolverService {
	
	private Neo4JRalResolver resolver;
	private ActivitiBPEngine bpengine;
	private ExecutionEngine exengine;

	public CristalRalResolverService() {
		bpengine = new ActivitiBPEngine();
		
	}

	public String resolveRalExpression(String processId, String expression) {
		String url = bpengine.getOrganizationDefinitionUrl(processId);
		try {
			String content = IOUtil.getURLContent(url);
			
			Document doc = Document.importFromJson(content);
			String query = doc.getCypherCreateQuery();
			
			GraphDatabaseService graphDb = new GraphDatabaseFactory()
			.newEmbeddedDatabaseBuilder(System.getenv("TEMP") + File.separator + "neo4j")
			.setConfig(GraphDatabaseSettings.node_keys_indexable,"name, position, role, unit")
			.setConfig(GraphDatabaseSettings.node_auto_indexing, "true")
			.newGraphDatabase();
			exengine = new ExecutionEngine( graphDb,StringLogger.logger(new File(System.getenv("TEMP") + File.separator + "neo4j-log.log")) );
	        exengine.execute( query );
	        bpengine.getResourceExpression(processId, "Approval task");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "dummyRalUser";

	}

}
