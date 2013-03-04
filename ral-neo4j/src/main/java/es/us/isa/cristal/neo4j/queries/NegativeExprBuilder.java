package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.model.expressions.NegativeExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.resolver.ConstraintResolver;

/**
 * User: resinas
 * Date: 27/02/13
 * Time: 10:26
 */
public class NegativeExprBuilder implements ExprBuilder {

    private Neo4jQueryBuilder builder;

    public NegativeExprBuilder(Neo4jQueryBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return NegativeExpr.class;
    }

    @Override
    public Query build(RALExpr expr, ConstraintResolver resolver) {
        NegativeExpr negativeExpr = (NegativeExpr) expr;
        Query q = builder.buildQuery(negativeExpr.getExprObject(), resolver);

        return Query.start(q.getStart()).where("NOT (" + q.getWhere() + ")").build();
    }
}
