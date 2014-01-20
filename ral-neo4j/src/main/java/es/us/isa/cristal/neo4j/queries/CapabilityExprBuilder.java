package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.model.expressions.CapabilityExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.resolver.ConstraintResolver;

/**
 * User: resinas
 * Date: 03/03/13
 * Time: 12:47
 */
public class CapabilityExprBuilder implements ExprBuilder {
    @Override
    public Class<? extends RALExpr> getExprType() {
        return CapabilityExpr.class;
    }

    @Override
    public Query build(RALExpr expr, ConstraintResolver resolver, Object processId) {
        CapabilityExpr cap = (CapabilityExpr) expr;
        return Query.start().where("has(person." + cap.getCapabilityName() + ")").build();
    }
}
