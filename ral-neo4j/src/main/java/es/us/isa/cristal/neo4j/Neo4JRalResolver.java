package es.us.isa.cristal.neo4j;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.neo4j.queries.Neo4jQueryBuilder;
import es.us.isa.cristal.resolver.RALResolver;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.helpers.collection.IteratorUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: resinas
 * Date: 04/03/13
 * Time: 10:10
 */
public class Neo4JRalResolver implements RALResolver {

    private BPEngine bpEngine;
    private ExecutionEngine neo4jEngine;


    public Neo4JRalResolver(BPEngine bpEngine, ExecutionEngine executionEngine) {
        this.bpEngine = bpEngine;
        this.neo4jEngine = executionEngine;
    }

    @Override
    public Collection<String> resolve(RALExpr expr, Object pid) {
        Neo4jQueryBuilder builder = new Neo4jQueryBuilder(bpEngine);
        String query = builder.build(expr, pid);
        ExecutionResult result = neo4jEngine.execute(query);

        return getSetFromResult(result, builder.getColumnName());
    }

    protected <T> Set<T> getSetFromResult(ExecutionResult result, String columnName) {
        Set<T> l = new HashSet<T>();
        for(Object p: IteratorUtil.asIterable(result.javaColumnAs(columnName))) {
            l.add((T)p);
        }
        return l;
    }
}
