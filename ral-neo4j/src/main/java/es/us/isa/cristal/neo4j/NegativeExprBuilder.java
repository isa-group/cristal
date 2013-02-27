package es.us.isa.cristal.neo4j;

import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.resolver.ConstraintResolver;

/**
 * User: resinas
 * Date: 27/02/13
 * Time: 10:26
 */
public class NegativeExprBuilder implements QueryBuilder {

    private Neo4jQueryBuilder builder;

    public NegativeExprBuilder(Neo4jQueryBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String build(RALExpr expr, ConstraintResolver resolver) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
