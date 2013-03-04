package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.model.expressions.OrExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.resolver.ConstraintResolver;

/**
 * User: resinas
 * Date: 03/03/13
 * Time: 11:14
 */
public class OrExprBuilder implements ExprBuilder {

    private Neo4jQueryBuilder builder;

    public OrExprBuilder(Neo4jQueryBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return OrExpr.class;
    }

    @Override
    public Query build(RALExpr expr, ConstraintResolver resolver) {
        OrExpr and = (OrExpr) expr;
        Query left = builder.buildQuery(and.getObjectExprLeft(), resolver);
        Query right = builder.buildQuery(and.getObjectExprRight(), resolver);

        return Query.start(left.getStart(), right.getStart()).where("(" + left.getWhere() + ") OR (" + right.getWhere() + ")").build();

    }
}
