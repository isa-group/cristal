package es.us.isa.cristal.owl.mappers.ral.designtimesc.expr;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.constraints.Constraint;
import es.us.isa.cristal.model.constraints.DataConstraint;
import es.us.isa.cristal.model.expressions.IsAssignmentExpr;
import es.us.isa.cristal.model.expressions.NegativeExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.mappers.ral.ExprMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;

import static es.us.isa.cristal.owl.Definitions.PERSON;


/**
 * User: resinas
 * Date: 04/07/13
 * Time: 16:35
 */
public class DTSubClassNegativeExprMapper implements ExprMapper {
    private OwlRalMapper mapper;
    private BPEngine bpEngine;

    public DTSubClassNegativeExprMapper(OwlRalMapper mapper, BPEngine bpEngine) {
        this.mapper = mapper;
        this.bpEngine = bpEngine;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return NegativeExpr.class;
    }

    @Override
    public String map(RALExpr expr, Object pid) {
        NegativeExpr e = (NegativeExpr) expr;
        String map;

        if (hasData(e)){
            map = PERSON;
        }
        else{
            map = PERSON + " and ( not ( " + mapper.map(e.getExprObject(), pid) + "))";

            if (e.getExprObject() instanceof IsAssignmentExpr) {
                IsAssignmentExpr isa = (IsAssignmentExpr) e.getExprObject();
                RALExpr isaExpr = bpEngine.getResourceExpression(isa.getActivityName());
                if (hasData(isaExpr)) {
                    map = PERSON;
                }
            }
        }

        return map;
    }

    private boolean hasData(RALExpr e) {
        boolean hasData = false;

        for (Constraint c : e.getConstraints()) {
            if (c instanceof DataConstraint) {
                hasData = true;
                break;
            }
        }
        return hasData;
    }
}
