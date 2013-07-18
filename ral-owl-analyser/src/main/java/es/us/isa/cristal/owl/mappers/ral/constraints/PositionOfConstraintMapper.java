package es.us.isa.cristal.owl.mappers.ral.constraints;

import es.us.isa.cristal.model.constraints.Constraint;
import es.us.isa.cristal.model.constraints.PositionOfConstraint;
import es.us.isa.cristal.owl.mappers.ral.ConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlConstraintMapper;

import static es.us.isa.cristal.owl.Definitions.OCCUPIES;

/**
 * User: resinas
 * Date: 04/07/13
 * Time: 11:45
 */
public class PositionOfConstraintMapper implements ConstraintMapper {
    private OwlConstraintMapper mapper;

    public PositionOfConstraintMapper(OwlConstraintMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Class<? extends Constraint> getExprType() {
        return PositionOfConstraint.class;
    }

    @Override
    public String map(Constraint expr, Object pid) {
        PositionOfConstraint c = (PositionOfConstraint) expr;
        return "inverse(" + OCCUPIES + ") some (" + mapper.map(c.getPersonConstraint(), pid) + ")";
    }
}
