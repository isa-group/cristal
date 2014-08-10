package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.model.expressions.RALExpr;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 16:42
 */
public interface ExprBuilder {
    public Class<? extends RALExpr> getExprType();
    public Query build(RALExpr expr, Object processId);
}
