package es.us.isa.cristal.owl.mappers.ral.expr;

import es.us.isa.cristal.model.expressions.AndExpr;
import es.us.isa.cristal.model.expressions.CompoundExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.mappers.ral.ExprMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;

/**
 * User: resinas
 * Date: 04/07/13
 * Time: 16:41
 */
public class CompoundExprMapper implements ExprMapper {
    private OwlRalMapper mapper;

    public CompoundExprMapper(OwlRalMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return CompoundExpr.class;
    }

    @Override
    public String map(RALExpr expr, Object pid) {
        CompoundExpr e = (CompoundExpr) expr;
        String composer = e instanceof AndExpr ? "and" : "or";
        return "(" + mapper.map(expr, pid) + ") " + composer + "(" + mapper.map(expr, pid) + ")";
    }
}
