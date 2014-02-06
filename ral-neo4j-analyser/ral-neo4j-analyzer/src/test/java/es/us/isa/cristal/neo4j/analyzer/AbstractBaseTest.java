package es.us.isa.cristal.neo4j.analyzer;

import java.io.File;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.util.StringLogger;
import org.neo4j.test.TargetDirectory;

/**
 * User: resinas
 * Date: 26/02/13
 * Time: 21:09
 */
public abstract class AbstractBaseTest {

    private static final String GRAPH_QUERY = "CREATE peter = { name : 'Peter' } \n" + 
    		"CREATE john = { name : 'John' } \n" + 
    		"CREATE mary = { name : 'Mary' } \n" +
    		"CREATE cathy = { name : 'Cathy' } \n" +
    		"CREATE albert = { name : 'Albert' } \n" +
    		"CREATE programmer = {role : 'Programmer'} \n" +
    		"CREATE analyst = {role : 'Analyst'} \n" +
    		"CREATE product_owner = {role : 'Product Owner'} \n" +
    		"CREATE scrum_master = {role : 'Scrum Master'} \n" +
    		"CREATE designer = {role : 'Designer'} \n" +
    		"CREATE development = {unit : 'Development'} \n" +
    		"CREATE junior_software_developer = {position : 'Junior Software Developer' } \n" +
    		"CREATE senior_software_developer = {position : 'Senior Software Developer' } \n" +
    		"CREATE team_leader = {position : 'Team Leader' } \n" +
    		"CREATE project_director = {position : 'Project Director' } \n" +
    		"CREATE junior_software_developer-[:UNIT]->development \n" +
    		"CREATE peter-[:POSITION]->junior_software_developer \n" +
    		"CREATE junior_software_developer-[:ROLE]->programmer \n" +
    		"CREATE senior_software_developer-[:UNIT]->development \n" +
    		"CREATE mary-[:POSITION]->senior_software_developer \n" +
    		"CREATE albert-[:POSITION]->senior_software_developer \n" +
    		"CREATE senior_software_developer-[:ROLE]->programmer \n" +
    		"CREATE senior_software_developer-[:ROLE]->analyst \n" +
    		"CREATE team_leader-[:UNIT]->development \n" +
    		"CREATE cathy-[:POSITION]->team_leader \n" +
    		"CREATE team_leader-[:ROLE]->scrum_master \n" +
    		"CREATE team_leader-[:ROLE]->analyst \n" +
    		"CREATE team_leader-[:ROLE]->programmer \n" +
    		"CREATE project_director-[:UNIT]->development \n" +
    		"CREATE john-[:POSITION]->project_director \n" +
    		"CREATE project_director-[:ROLE]->product_owner";
    		

   

    
    protected ExecutionEngine getExecutionEngine() {
        // given
    	ExecutionEngine engine;
        File storeDir = TargetDirectory.forTest(this.getClass()).directory( System.currentTimeMillis() + "-test" );

        GraphDatabaseService graphDb = new GraphDatabaseFactory().
                newEmbeddedDatabaseBuilder( storeDir.getAbsolutePath() ).
                setConfig( GraphDatabaseSettings.node_keys_indexable, "name, position, role, unit" ).
                setConfig( GraphDatabaseSettings.node_auto_indexing, "true" ).
                newGraphDatabase();

        engine = new ExecutionEngine( graphDb,
                StringLogger.logger(new File(storeDir.getAbsolutePath() + "/logger")) );
        engine.execute( GRAPH_QUERY);
        
        return engine;
    }

   

    
}
