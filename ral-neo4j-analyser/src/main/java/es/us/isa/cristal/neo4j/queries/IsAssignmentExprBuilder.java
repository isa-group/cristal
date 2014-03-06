package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.model.expressions.IsAssignmentExpr;
import es.us.isa.cristal.model.expressions.RALExpr;

/**
 * User: resinas
 * Date: 04/03/13
 * Time: 09:58
 */
public class IsAssignmentExprBuilder implements ExprBuilder {

    private Neo4jQueryBuilder builder;
    public IsAssignmentExprBuilder(Neo4jQueryBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return IsAssignmentExpr.class;
    }

    @Override
    public Query build(RALExpr expr, Object processId) {
        IsAssignmentExpr assignmentExpr = (IsAssignmentExpr) expr;
        RALExpr activityExpr = builder.getBpEngine().getResourceExpression(processId,assignmentExpr.getActivityName());

        return builder.buildQuery(activityExpr, processId);
    }
}
