package es.us.isa.cristal.analyzer.rest;

import java.io.File;
import java.util.Set;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.util.StringLogger;
import org.neo4j.test.TargetDirectory;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.analyzer.rest.util.AnalyserBPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.analyzer.Neo4jRALAnalyser;
import es.us.isa.cristal.organization.model.gson.Document;
import es.us.isa.cristal.organization.model.util.IOUtil;

public class RestRalNeo4JAnalyzer {
	
	
	
	
	public String initialize(String processId, String bpmnModelUrl, String organizationModelUrl) throws Exception{
		String organization = IOUtil.getURLContent(organizationModelUrl);
		Document doc = Document.importFromJson(organization);
		
		ExecutionEngine engine;
        File storeDir = TargetDirectory.forTest(this.getClass()).directory( System.currentTimeMillis() + "-test" );

        GraphDatabaseService graphDb = new GraphDatabaseFactory().
                newEmbeddedDatabaseBuilder( storeDir.getAbsolutePath() ).
                setConfig( GraphDatabaseSettings.node_keys_indexable, "name, position, role, unit" ).
                setConfig( GraphDatabaseSettings.node_auto_indexing, "true" ).
                newGraphDatabase();

        engine = new ExecutionEngine( graphDb,
                StringLogger.logger(new File(storeDir.getAbsolutePath() + "/logger")) );
        engine.execute(doc.getCypherCreateQuery());
        
        BPEngine bpEngine = new AnalyserBPEngine(bpmnModelUrl);
		
		Neo4jRALAnalyser analizer = new Neo4jRALAnalyser(engine, bpEngine, processId);
		
		
	}
	


	public Set<String> potentialParticipants(String token, String activityName, TaskDuty duty) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> potentialActivities(String token, String personName, TaskDuty duty) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean basicConsistency(String token, String activity, TaskDuty duty) {
		// TODO Auto-generated method stub
		return false;
	}

	public Set<String> nonParticipants(String token, Iterable<String> activities,
			TaskDuty duty) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> permanentParticipants(String token, Iterable<String> activities,
			TaskDuty duty) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> criticalActivities(String token, Iterable<String> activities,
			TaskDuty duty) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> criticalParticipants(String token, Iterable<String> activities,
			TaskDuty duty) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> indispensableParticipants(String token, Iterable<String> activities,
			TaskDuty duty) {
		// TODO Auto-generated method stub
		return null;
	}

}
