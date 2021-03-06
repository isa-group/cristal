package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.model.expressions.OrExpr;
import es.us.isa.cristal.model.expressions.RALExpr;

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
    public Query build(RALExpr expr, Object processId) {
        OrExpr and = (OrExpr) expr;
        Query left = builder.buildQuery(and.getObjectExprLeft(), processId);
        Query right = builder.buildQuery(and.getObjectExprRight(), processId);

        return Query.start(left.getStart(), right.getStart())
                .match(left.getMatch(), right.getMatch())
                .with(left.getWith(), right.getWith())
                .where("(" + left.getWhere() + ") OR (" + right.getWhere() + ")").build();

    }
}
