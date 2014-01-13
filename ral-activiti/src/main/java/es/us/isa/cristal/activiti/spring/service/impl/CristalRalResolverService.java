package es.us.isa.cristal.activiti.spring.service.impl;

import java.io.File;
import java.io.IOException;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.util.StringLogger;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import es.us.isa.cristal.activiti.ActivitiBPEngine;
import es.us.isa.cristal.activiti.model.gson.Document;
import es.us.isa.cristal.activiti.spring.service.RalResolverService;
import es.us.isa.cristal.activiti.util.IOUtil;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;

@Service("cristalRalResolverService")
public class CristalRalResolverService implements RalResolverService {
	
	private Neo4JRalResolver resolver;
	private ActivitiBPEngine bpengine;
	private ExecutionEngine exengine;

	public CristalRalResolverService() {
		bpengine = new ActivitiBPEngine();
		String url = bpengine.getOrganizationDefinitionUrl("TestRALProcess:1:1108");
		try {
			String content = IOUtil.getURLContent(url);
			Gson gson = new Gson();
			Document doc = gson.fromJson(content, Document.class);
			String query = doc.getCypherCreateQuery();
			
			GraphDatabaseService graphDb = new GraphDatabaseFactory()
			.newEmbeddedDatabaseBuilder(System.getenv("TEMP") + File.separator + "neo4j")
			.setConfig(GraphDatabaseSettings.node_keys_indexable,"name, position, role, unit")
			.setConfig(GraphDatabaseSettings.node_auto_indexing, "true")
			.newGraphDatabase();
			exengine = new ExecutionEngine( graphDb,StringLogger.logger(new File(System.getenv("TEMP") + File.separator + "neo4j-log.log")) );
	        exengine.execute( query );

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String resolveRalExpression(String expression) {
		System.out.println("### CristalRalResolverService invoked: "
				+ expression);
		bpengine.getResourceExpression("TestRALProcess:1:1108", "Approval task");
		return "dummyRalUser";

	}

}
