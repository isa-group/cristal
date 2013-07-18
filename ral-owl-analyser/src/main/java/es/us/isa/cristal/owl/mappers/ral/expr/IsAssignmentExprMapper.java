package es.us.isa.cristal.owl.mappers.ral.expr;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.IsAssignmentExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.mappers.ral.ExprMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;

/**
 * User: resinas
 * Date: 03/07/13
 * Time: 11:45
 */
public class IsAssignmentExprMapper implements ExprMapper {
    private OwlRalMapper ralMapper;
    private BPEngine engine;

    public IsAssignmentExprMapper(OwlRalMapper ralMapper, BPEngine engine) {
        this.ralMapper = ralMapper;
        this.engine = engine;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return IsAssignmentExpr.class;
    }

    @Override
    public String map(RALExpr expr, Object pid) {
        IsAssignmentExpr e = (IsAssignmentExpr) expr;

        RALExpr activityExpr = engine.getResourceExpression(e.getActivityName());

        return ralMapper.map(activityExpr, pid);
    }
}
