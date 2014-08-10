package es.us.isa.cristal.neo4j.queries;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.helpers.collection.IteratorUtil;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.resolver.ConstraintResolver;
import es.us.isa.cristal.test.utils.bpEngine.MockBPEngine;
import es.us.isa.cristal.neo4j.test.utils.executionengine.ExecutionEngineUtil;
import es.us.isa.cristal.neo4j.test.utils.graph.GraphUtil;

/**
 * User: resinas
 * Date: 26/02/13
 * Time: 21:09
 */
public abstract class AbstractBaseTest {

    

    protected ExecutionEngine engine;

    @Before
    public void setUpNeo4jDB() {
        // given
       engine = ExecutionEngineUtil.getExecutionEngine(GraphUtil.THEOS_GRAPH);
    }

    protected <T> Set<T> getSetFromResult(ExecutionResult result) {
        Set<T> l = new HashSet<T>();
        for(Object p: IteratorUtil.asIterable(result.javaColumnAs("person.name"))) {
            l.add((T)p);
        }
        return l;
    }

    protected ExecutionResult doQuery(RALExpr expr) {
    	BPEngine mock = new MockBPEngine();
        Neo4jQueryBuilder builder = new Neo4jQueryBuilder(mock, new ConstraintResolver(mock));
        String query = builder.build(expr, 0L);
        System.out.println(query);
        return engine.execute(query);
    }
}
