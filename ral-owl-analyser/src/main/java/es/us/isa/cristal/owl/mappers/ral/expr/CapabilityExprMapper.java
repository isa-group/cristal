package es.us.isa.cristal.owl.mappers.ral.expr;

import es.us.isa.cristal.model.expressions.CapabilityExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.mappers.ral.ExprMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlConstraintMapper;

/**
 * User: resinas
 * Date: 03/07/13
 * Time: 11:27
 */
public class CapabilityExprMapper implements ExprMapper {
    private OwlConstraintMapper constraintMapper;

    public CapabilityExprMapper(OwlConstraintMapper constraintMapper) {
        this.constraintMapper = constraintMapper;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return CapabilityExpr.class;
    }

    //TODO Capability processing is different than others ids. Some thought must be done regarding this.
    @Override
    public String map(RALExpr expr, Object pid) {
        CapabilityExpr e = (CapabilityExpr) expr;
        return Definitions.HASCAPABILITY + " some ({" + e.getCapabilityName() + "})";
    }
}
