package es.us.isa.cristal.owl.mappers.ral.expr;

import es.us.isa.cristal.model.CommonalityAmount;
import es.us.isa.cristal.model.GroupResourceType;
import es.us.isa.cristal.model.expressions.CommonalityExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.mappers.ral.ExprMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlConstraintMapper;

import static es.us.isa.cristal.owl.Definitions.*;

/**
 * User: resinas
 * Date: 03/07/13
 * Time: 10:54
 */
public class CommonalityExprMapper implements ExprMapper {

    private OwlConstraintMapper constraintMapper;

    public CommonalityExprMapper(OwlConstraintMapper constraintMapper) {
        this.constraintMapper = constraintMapper;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return CommonalityExpr.class;
    }

    @Override
    public String map(RALExpr expr, Object pid) {
        CommonalityExpr e = (CommonalityExpr) expr;
        String mapc = constraintMapper.map(e.getPersonConstraint(), pid);
        String amount;

        if (e.getAmount() == CommonalityAmount.SOME) {
            amount = "some";
        } else {
            amount = "all";
        }

        return map(e, amount, mapc);
    }

    private String map(CommonalityExpr e, String amount, String mapc) {
        GroupResourceType type = e.getGroupResourceType();

        String query;

        if (type == GroupResourceType.POSITION) {
            query = OCCUPIES + " some (inverse(" + OCCUPIES + ") " + amount + " (" + mapc + "))";
        }
        else if (type == GroupResourceType.ROLE) {
            query = OCCUPIES + " some (" + PARTICIPATESIN + " some (inverse(" + PARTICIPATESIN + ") " + amount + " (inverse(" + OCCUPIES + ") some (" + mapc + "))))";
        }
        else if (type == GroupResourceType.UNIT) {
            query = OCCUPIES + " some (" + ISMEMBEROF + " some (inverse(" + ISMEMBEROF + ") " + amount + " (inverse(" + OCCUPIES + ") some (" + mapc + "))))";
        } else {
            throw new RuntimeException("Not valid type in group resource expression");
        }

        return query;
    }
}
