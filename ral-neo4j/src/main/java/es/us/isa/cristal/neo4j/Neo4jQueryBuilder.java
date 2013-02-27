package es.us.isa.cristal.neo4j;

import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.resolver.ConstraintResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 16:36
 */
public class Neo4jQueryBuilder {

    private Map<Class<? extends RALExpr>, QueryBuilder> builderMap;

    public Neo4jQueryBuilder() {
        builderMap = new HashMap<Class<? extends RALExpr>, QueryBuilder>();

        addQueryBuilder(new PersonExprBuilder());
        addQueryBuilder(new GroupResourceExprBuilder());
        addQueryBuilder(new NegativeExprBuilder(this));
    }

    private void addQueryBuilder(QueryBuilder b) {
        builderMap.put(b.getExprType(), b);
    }


    public String build(RALExpr expr, ConstraintResolver resolver) {
        return builderMap.get(expr.getClass()).build(expr, resolver);
    }
}
