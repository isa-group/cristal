package es.us.isa.cristal.web;

import com.google.common.io.Files;

import es.isa.puri.Ranking;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;
import es.us.isa.cristal.parser.RALParser;
import es.us.isa.cristal.pba.PBAResolver;
import es.us.isa.cristal.pba.rankers.BPHistory;
import es.us.isa.cristal.pba.rankers.Person;
import es.us.isa.cristal.pba.rankers.TaskEngine;
import es.us.isa.cristal.resolver.RALResolver;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.util.StringLogger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import java.io.File;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * User: resinas
 * Date: 07/03/13
 * Time: 10:40
 */
@Path("/api/resolve")
public class ResolveResource {
    private static final Logger log = Logger.getLogger(ResolveResource.class
            .getName());

    private static final String GRAPH_DESCRIPTION = "CREATE anthony = { name : 'Anthony', degree: 'PhD', cost: 10}\n" +
            "CREATE betty = { name : 'Betty', cost: 3 }\n" +
            "CREATE daniel = { name : 'Daniel', cost: 5 }\n" +
            "CREATE anna = { name : 'Anna', cost: 4 }\n" +
            "CREATE charles = { name : 'Charles', degree: 'PhD', cost: 8 }\n" +
            "CREATE christine = { name : 'Christine', degree: 'MsC', cost: 7 }\n" +
            "CREATE adele = { name : 'Adele', cost: 7 }\n" +
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
    private BPHistory history;
    private TaskEngine taskEngine;
    private BPEngine bpEngine;

    private RALResolver ralResolver;
    private PBAResolver pbaResolver;

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

        history = new BPHistoryMock();
        taskEngine = new TaskEngineMock();
        bpEngine = new BPEngineMock();

        ralResolver = new Neo4JRalResolver(bpEngine, engine);
        pbaResolver = new PBAResolver(history, taskEngine, ralResolver, engine);

        log.info("Database created");
    }

    @POST
    @Path("/ral")
    public Collection<String> resolve(String expr){
        log.info("Expression: "+expr);

        Collection<String> result = ralResolver.resolve(RALParser.parse(expr), 0);

        log.info("Result: " +result);

        return result;
    }

    @POST
    @Path("/pba")
    public List<String> prioritize(PriorityAllocation allocation) {
        log.info("Preferences: "+allocation.getPreference());
        log.info("Potential performers: " + allocation.getPotentialPerformers());

        Ranking<Person> ranking = pbaResolver.resolve(new StringReader(allocation.getPreference()),
                Person.fromNames(allocation.listPotentialPerformers()));

        List<String> result = Person.toStrings(ranking.getResultsAsList());
        log.info("Result: " + result);
        return result;
    }

    private class TaskEngineMock implements TaskEngine {
        @Override
        public long countTasks(String user) {
            long count = 0;

            if ("Anthony".equals(user)) count =  7;
            else if ("Charles".equals(user)) count = 8;
            else if ("Christine".equals(user)) count =  4;
            else if ("Adele".equals(user)) count =  4;
            else if ("Betty".equals(user)) count = 3;
            else if ("Daniel".equals(user)) count = 10;
            else if ("Anna".equals(user)) count = 5;

            return count;
        }
    }

    private class BPHistoryMock implements BPHistory {
        @Override
        public long countActivityByPerson(String user, String activity, String processId) {
            long count = 0;

            if ("Anthony".equals(user)) count =  15;
            else if ("Charles".equals(user)) count = 8;
            else if ("Christine".equals(user)) count =  10;
            else if ("Adele".equals(user)) count =  6;
            else if ("Betty".equals(user)) count = 4;
            else if ("Daniel".equals(user)) count = 10;
            else if ("Anna".equals(user)) count = 5;

            return count;

        }
    }

    private class BPEngineMock implements BPEngine {
        @Override
        public String getDataValue(Object pid, String data, String property) {
            return "Anna";
        }

        @Override
        public List<String> getActivityPerformer(Object pid, String activityName) {
            return Arrays.asList("Charles");
        }

       

		@Override
		public RALExpr getResourceExpression(Object processId, String activityId) {
			return RALParser.parse("HAS ROLE Account Administrator");
		}

		@Override
		public RALExpr getResourceExpressionByProcessDefinitionId(
				Object processDefinitionId, String activityId) {
			return RALParser.parse("HAS ROLE Account Administrator");
		}

		@Override
		public RALExpr getResourceExpressionByProcessInstanceId(
				Object processInstanceId, String activityId) {
			return RALParser.parse("HAS ROLE Account Administrator");
		}
    }

}
