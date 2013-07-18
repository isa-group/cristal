package es.us.isa.cristal.owl.mappers.ral.expr;

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
public class NegativeExprMapper implements ExprMapper {
    private OwlRalMapper mapper;

    public NegativeExprMapper(OwlRalMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return NegativeExpr.class;
    }

    @Override
    public String map(RALExpr expr, Object pid) {
        NegativeExpr e = (NegativeExpr) expr;

        return PERSON + " and ( not ( " + mapper.map(e.getExprObject(), pid) + "))";
    }
}
