package es.us.isa.cristal.neo4j;

import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.resolver.ConstraintResolver;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 16:42
 */
public interface QueryBuilder {
    public Class<? extends RALExpr> getExprType();
    public String build(RALExpr expr, ConstraintResolver resolver);
}
