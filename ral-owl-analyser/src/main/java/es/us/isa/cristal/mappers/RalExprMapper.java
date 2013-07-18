package es.us.isa.cristal.mappers;

import es.us.isa.cristal.model.expressions.RALExpr;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 17:41
 */
public interface RalExprMapper<MapResult, ConstraintMapper> {
    public Class<? extends RALExpr> getExprType();
    public MapResult map(RALExpr expr, ConstraintMapper mapper);

}
