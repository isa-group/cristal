package es.us.isa.cristal.web;

import com.google.common.io.Files;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;
import es.us.isa.cristal.parser.RALParser;
import es.us.isa.cristal.resolver.BPEngine;
import es.us.isa.cristal.resolver.RALResolver;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.util.StringLogger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.File;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * User: resinas
 * Date: 07/03/13
 * Time: 10:40
 */
@Path("/api/resolve")
public class ResolveResource {
    private static final Logger log = Logger.getLogger(Raci2BpmnResource.class
            .getName());

    private static final String GRAPH_DESCRIPTION = "CREATE anthony = { name : 'Anthony', degree: 'PhD' }\n" +
            "CREATE betty = { name : 'Betty' }\n" +
            "CREATE daniel = { name : 'Daniel' }\n" +
            "CREATE anna = { name : 'Anna' }\n" +
            "CREATE charles = { name : 'Charles', degree: 'PhD' }\n" +
            "CREATE christine = { name : 'Christine', degree: 'MsC' }\n" +
            "CREATE adele = { name : 'Adele' }\n" +
            "\n" +
            "CREATE theos = { unit : 'Theos' }\n" +
            "\n" +
            "CREATE theos_coordinator = {position : 'THEOS Project Coordinator' }\n" +
            "CREATE theos_account = {position : 'THEOS Account Delegate'}\n" +
            "CREATE theos_technician = { position : 'THEOS Technician'}\n" +
            "CREATE theos_assistant = {position : 'THEOS Administrative Assistant'}\n" +
            "CREATE theos_responsible = {position : 'THEOS Responsible for Work Package'}\n" +
            "CREATE theos_student = {position : 'THEOS PhD Student'}\n" +
            "\n" +
            "CREATE coordinator = {role : 'Project Coordinator'}\n" +
            "CREATE administrator = {role: 'Account Administrator'}\n" +
            "CREATE resourcemanager = {role: 'Resource Manager'}\n" +
            "CREATE responsible = {role: 'Responsible for Work Package'}\n"+
            "CREATE researcher = {role: 'Researcher'}\n"+
            "CREATE student = {role: 'PhD Student'}\n" +
            "CREATE clerk = {role: 'Clerk'}\n" +
            "CREATE technician = {role: 'Technician'}\n" +
            "\n" +
            "CREATE anthony-[:POSITION]->theos_coordinator \n" +
            "CREATE anthony-[:POSITION]->theos_responsible \n" +
            "CREATE betty-[:POSITION]->theos_account \n" +
            "CREATE daniel-[:POSITION]->theos_technician \n" +
            "CREATE anna-[:POSITION]->theos_assistant \n"+
            "CREATE christine-[:POSITION]->theos_student \n"+
            "CREATE adele-[:POSITION]->theos_student \n" +
            "CREATE charles-[:POSITION]->theos_responsible \n" +
            "\n" +
            "CREATE theos_coordinator-[:UNIT]->theos \n"+
            "CREATE theos_account-[:UNIT]->theos \n"+
            "CREATE theos_technician-[:UNIT]->theos \n"+
            "CREATE theos_assistant-[:UNIT]->theos \n"+
            "CREATE theos_responsible-[:UNIT]->theos \n"+
            "CREATE theos_student-[:UNIT]->theos \n"+
            "\n" +
            "CREATE theos_coordinator-[:ROLE]->coordinator \n"+
            "CREATE theos_coordinator-[:ROLE]->administrator \n"+
            "CREATE theos_coordinator-[:ROLE]->resourcemanager \n"+
            "CREATE theos_responsible-[:ROLE]->responsible \n"+
            "CREATE theos_responsible-[:ROLE]->researcher \n"+
            "CREATE theos_student-[:ROLE]->researcher \n"+
            "CREATE theos_student-[:ROLE]->student \n"+
            "CREATE theos_account-[:ROLE]->administrator \n"+
            "CREATE theos_technician-[:ROLE]->technician \n"+
            "CREATE theos_assistant-[:ROLE]->clerk \n"+
            "\n" +
            "CREATE theos_coordinator-[:DELEGATES]->theos_account \n"+
            "CREATE theos_coordinator-[:DELEGATES]->theos_technician \n"+
            "CREATE theos_coordinator-[:DELEGATES]->theos_assistant \n" +
            "CREATE theos_coordinator-[:DELEGATES]->theos_responsible \n" +
            "CREATE theos_responsible-[:DELEGATES]->theos_student \n " +
            "CREATE theos_coordinator<-[:REPORTS]-theos_account \n"+
            "CREATE theos_coordinator<-[:REPORTS]-theos_technician \n"+
            "CREATE theos_coordinator<-[:REPORTS]-theos_assistant \n" +
            "CREATE theos_coordinator<-[:REPORTS]-theos_responsible \n" +
            "CREATE theos_responsible<-[:REPORTS]-theos_student";

    private ExecutionEngine engine;

    public ResolveResource() {
        File storeDir = null;
        log.info("Creating temp file...");
        storeDir = Files.createTempDir();
        log.info("Temp file created at: "+storeDir.getAbsolutePath());

        log.info("Creating database");
        GraphDatabaseService graphDb = new GraphDatabaseFactory().
                newEmbeddedDatabaseBuilder( storeDir.getAbsolutePath() ).
                setConfig( GraphDatabaseSettings.node_keys_indexable, "name, position, role, unit" ).
                setConfig( GraphDatabaseSettings.node_auto_indexing, "true" ).
                newGraphDatabase();

        engine = new ExecutionEngine( graphDb,
                StringLogger.logger(new File(storeDir.getAbsolutePath() + "/logger")) );
        engine.execute( GRAPH_DESCRIPTION );

        log.info("Database created");
    }

    @POST
    public Collection<String> resolve(String expr){
        log.info("Expression: "+expr);

        RALResolver resolver = new Neo4JRalResolver(new BPEngineMock(), engine);
        Collection<String> result = resolver.resolve(RALParser.parse(expr), 0);

        log.info("Result: " +result);

        return result;
    }

    private class BPEngineMock implements BPEngine {
        @Override
        public String getDataValue(Object pid, String data, String property) {
            return "Anna";
        }

        @Override
        public String getActivityPerformer(Object pid, String activityName) {
            return "Charles";
        }

        @Override
        public RALExpr getResourceExpression(String activityName) {
            return RALParser.parse("HAS ROLE Account Administrator");
        }
    }

}
