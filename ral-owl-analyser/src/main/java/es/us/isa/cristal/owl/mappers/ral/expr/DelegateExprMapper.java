package es.us.isa.cristal.owl.mappers.ral.expr;

import es.us.isa.cristal.model.HierarchyDirection;
import es.us.isa.cristal.model.expressions.DelegateExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.mappers.ral.ExprMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlConstraintMapper;

import static es.us.isa.cristal.owl.Definitions.CANDELEGATEWORKTO;
import static es.us.isa.cristal.owl.Definitions.OCCUPIES;

/**
 * User: resinas
 * Date: 04/07/13
 * Time: 12:12
 */
public class DelegateExprMapper implements ExprMapper {
    private OwlConstraintMapper constraintMapper;

    public DelegateExprMapper(OwlConstraintMapper constraintMapper) {
        this.constraintMapper = constraintMapper;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return DelegateExpr.class;
    }

    @Override
    public String map(RALExpr expr, Object pid) {
        DelegateExpr e = (DelegateExpr) expr;
        String mapc = constraintMapper.map(e.getPositionConstraint(), pid);
        String inv = e.getDirection() == HierarchyDirection.INVERSE ? "inverse(" + CANDELEGATEWORKTO + ")" : CANDELEGATEWORKTO;

        return OCCUPIES + " some (" + inv + " some (" + mapc + "))";
    }
}
