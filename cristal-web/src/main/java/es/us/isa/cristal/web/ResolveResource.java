package es.us.isa.cristal.web;

import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.neo4j.cypher.ExecutionEngine;

import es.isa.puri.Ranking;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;
import es.us.isa.cristal.parser.RALParser;
import es.us.isa.cristal.pba.PBAResolver;
import es.us.isa.cristal.pba.rankers.BPHistory;
import es.us.isa.cristal.pba.rankers.Person;
import es.us.isa.cristal.pba.rankers.TaskEngine;
import es.us.isa.cristal.resolver.RALResolver;
import es.us.isa.cristal.test.utils.bpEngine.MockBPEngine;
import es.us.isa.cristal.neo4j.test.utils.executionengine.ExecutionEngineUtil;
import es.us.isa.cristal.neo4j.test.utils.graph.GraphUtil;

/**
 * User: resinas
 * Date: 07/03/13
 * Time: 10:40
 */
@Path("/api/resolve")
public class ResolveResource {
    private static final Logger log = Logger.getLogger(ResolveResource.class
            .getName());

    

    private ExecutionEngine engine;
    private BPHistory history;
    private TaskEngine taskEngine;
    private BPEngine bpEngine;

    private RALResolver ralResolver;
    private PBAResolver pbaResolver;

    public ResolveResource() {
        
    	engine = ExecutionEngineUtil.getExecutionEngine(GraphUtil.THEOS_GRAPH);
        history = new BPHistoryMock();
        taskEngine = new TaskEngineMock();
        bpEngine = new MockBPEngine();

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

    
    

}
