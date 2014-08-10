package es.us.isa.cristal.neo4j.test.utils.executionengine;

import java.io.File;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.util.StringLogger;

/**
 * 
 * @author Manuel Leon
 *
 */
public class ExecutionEngineUtil {

	public static ExecutionEngine getExecutionEngine(String query){
	        // given
	    	ExecutionEngine engine;
	    	String dirName = System.getenv("TEMP") + File.separator + "neo4j-" + System.currentTimeMillis();
			GraphDatabaseService graphDb = new GraphDatabaseFactory()
			.newEmbeddedDatabaseBuilder(dirName)
			.setConfig(GraphDatabaseSettings.node_keys_indexable,"name, position, role, unit")
			.setConfig(GraphDatabaseSettings.node_auto_indexing, "true")
			.newGraphDatabase();
			engine = new ExecutionEngine( graphDb,StringLogger.logger(new File(dirName + File.separator + "log.log")) );
			engine.execute(query);
	        
	        return engine;
	}
	
}
