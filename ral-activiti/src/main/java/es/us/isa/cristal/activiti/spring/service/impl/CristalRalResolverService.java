package es.us.isa.cristal.activiti.spring.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.kernel.impl.util.StringLogger;
import org.springframework.stereotype.Service;

import es.us.isa.cristal.activiti.ActivitiBPEngine;
import es.us.isa.cristal.activiti.spring.service.RalResolverService;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.neo4j.queries.Neo4jQueryBuilder;
import es.us.isa.cristal.organization.model.gson.Document;
import es.us.isa.cristal.organization.model.util.IOUtil;
import es.us.isa.cristal.parser.RALParser;
import es.us.isa.cristal.resolver.ConstraintResolver;

@Service("cristalRalResolverService")
public class CristalRalResolverService implements RalResolverService {
	
	
	private ActivitiBPEngine bpengine;
    private Neo4jQueryBuilder builder;
	private Map<String, ExecutionEngine> execEngines;
	
	public CristalRalResolverService() {
		bpengine = new ActivitiBPEngine();
		execEngines = new HashMap<String, ExecutionEngine>();
		builder = new Neo4jQueryBuilder(bpengine, new ConstraintResolver(bpengine));
	}

	public String resolveRalExpression(String processId, String expression) {
		
		String result = "";
		String url = bpengine.getOrganizationDefinitionUrl(processId);
		try {
			ExecutionEngine exengine;
			if(execEngines.containsKey(url)){
				exengine = execEngines.get(url);
			}else{
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
		        execEngines.put(url, exengine);
			}
	        //bpengine.getResourceExpression(processId, "Approval task");
	        RALExpr expr = RALParser.parse(expression);
        	String query = builder.build(expr, 0L);
        	ExecutionResult er = exengine.execute(query);
        	result = buildStringFromResult(er);
        	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;

	}

	
	private static String buildStringFromResult(ExecutionResult result) {
        String cad = "";
        for(Object p: IteratorUtil.asIterable(result.javaColumnAs("person.name"))) {
            if(!cad.isEmpty()){
            	cad+=",";
            }
        	cad+=p.toString();
        }
        return cad;
    }
}
