package es.us.isa.cristal.owl.mappers.ral.expr;

import es.us.isa.cristal.model.expressions.PersonExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.mappers.ral.ExprMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlConstraintMapper;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 18:15
 */
public class PersonExprMapper implements ExprMapper {

    private OwlConstraintMapper constraintMapper;

    public PersonExprMapper(OwlConstraintMapper constraintMapper) {
        this.constraintMapper = constraintMapper;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return PersonExpr.class;
    }

    @Override
    public String map(RALExpr expr, Object pid) {
        PersonExpr p = (PersonExpr) expr;
        return constraintMapper.map(p.getPersonConstraint(), pid);
    }
}
