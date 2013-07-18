package es.us.isa.cristal.owl.mappers.ral.expr;

import es.us.isa.cristal.model.HierarchyDirection;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.model.expressions.ReportExpr;
import es.us.isa.cristal.owl.mappers.ral.ExprMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlConstraintMapper;

import static es.us.isa.cristal.owl.Definitions.*;

/**
 * User: resinas
 * Date: 04/07/13
 * Time: 11:37
 */
public class ReportExprMapper implements ExprMapper {
    private OwlConstraintMapper constraintMapper;

    public ReportExprMapper(OwlConstraintMapper constraintMapper) {
        this.constraintMapper = constraintMapper;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return ReportExpr.class;
    }

    @Override
    public String map(RALExpr expr, Object pid) {
        ReportExpr e = (ReportExpr) expr;

        String mapConstraint = constraintMapper.map(e.getPositionConstraint(), pid);
        String reportsTo = e.getDirectly() ? REPORTSTO : EXTENDEDREPORTSTO;
        String inv = e.getDirection() == HierarchyDirection.INVERSE ? "inverse(" + reportsTo + ")" : reportsTo;

        return OCCUPIES + " some (" + inv + " some (" + mapConstraint + "))";
    }
}
