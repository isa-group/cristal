package es.us.isa.cristal.performance.tester;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.kernel.impl.util.StringLogger;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.neo4j.queries.Neo4jQueryBuilder;
import es.us.isa.cristal.organization.generator.ConfigurationFactory;
import es.us.isa.cristal.organization.generator.OrganizationGenerator;
import es.us.isa.cristal.organization.model.gson.Model;
import es.us.isa.cristal.organization.model.util.IOUtil;
import es.us.isa.cristal.parser.RALParser;
import es.us.isa.cristal.performance.tester.data.ExecutionData;
import es.us.isa.cristal.performance.tester.data.Query;
import es.us.isa.cristal.performance.tester.exporters.ExporterFactory;
import es.us.isa.cristal.performance.tester.meters.Meter;
import es.us.isa.cristal.performance.tester.meters.TimeMeter;
import es.us.isa.cristal.performance.tester.util.MockBPEngine;
import es.us.isa.cristal.performance.tester.util.QueryProcessor;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws FileNotFoundException
    {
        String path = "queryStack.json";
    	
        ExecutionData edata = ExecutionData.importFromJson(IOUtil.convertStreamToString(new FileInputStream(path)));
    	
    	ConfigurationFactory factory = new ConfigurationFactory();
		OrganizationGenerator generator = new OrganizationGenerator(factory.getDefaultConfiguration(edata.getModelWeight()));
		
		Model model = generator.generate();
		
		BPEngine engine = new MockBPEngine(model, edata.getActivityQueryMap());
    	
		
		String createQuery = model.getCypherCreateQuery();
		System.out.println(createQuery);
		GraphDatabaseService graphDb = new GraphDatabaseFactory()
		.newEmbeddedDatabaseBuilder(System.getenv("TEMP") + File.separator + "neo4j-performance")
		.setConfig(GraphDatabaseSettings.node_keys_indexable,"name, position, role, unit")
		.setConfig(GraphDatabaseSettings.node_auto_indexing, "true")
		.newGraphDatabase();
		
		ExecutionEngine exengine = new ExecutionEngine( graphDb,StringLogger.logger(new File(System.getenv("TEMP") + File.separator + "neo4j-log.log")) );
        exengine.execute( createQuery );
       
        Neo4jQueryBuilder builder = new Neo4jQueryBuilder(engine);
       
        QueryProcessor processor = new QueryProcessor();
        
        for(Query q: edata.getQueryList()){
        	
        	String expression = q.getQuery();
        	String finalExpression = processor.processQuery(expression, model);
        	
        	
        	for(int i=0; i<edata.getIterations();i++){
        		Meter<Long> meter = new TimeMeter();
        		if(edata.getProcessExpressionForEachIteration()){
        			finalExpression = processor.processQuery(expression, model);
            	}
        		meter.start();
            	RALExpr expr = RALParser.parse(finalExpression);
            	String query = builder.build(expr, 0L);
            	ExecutionResult result = exengine.execute(query);
            	meter.stop();
            	q.addExecution(finalExpression, getStringFromResult(result), meter.getResult());
            	
        	}
        	
        }
        
        ExporterFactory exporterFactory = new ExporterFactory();
        exporterFactory.getExporter(edata.getExport()).export(edata.getExport(), edata, model);
        
        
        
    }
    
    
    private static String getStringFromResult(ExecutionResult result) {
        String cad = "[";
        for(Object p: IteratorUtil.asIterable(result.javaColumnAs("person.name"))) {
            cad+=p.toString() + ",";
        }
        cad = cad.substring(0, cad.length()-1) + "]";
        return cad;
    }
    
    
}
