package es.us.isa.cristal.owl.mappers.ral.constraints;

import es.us.isa.cristal.model.constraints.Constraint;
import es.us.isa.cristal.model.constraints.IdConstraint;
import es.us.isa.cristal.model.constraints.ThisPersonConstraint;
import es.us.isa.cristal.owl.mappers.ral.ConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 18:11
 */
public class IdConstraintMapper implements ConstraintMapper {

    private IdMapper idMapper;

    public IdConstraintMapper(IdMapper idMapper) {
        this.idMapper = idMapper;
    }

    @Override
    public Class<? extends Constraint> getExprType() {
        return IdConstraint.class;
    }

    @Override
    public String map(Constraint expr, Object pid) {
        IdConstraint c = (IdConstraint) expr;

        String mappedId;

        if (c instanceof ThisPersonConstraint)
            mappedId = idMapper.mapPerson(c.getId());
        else
            mappedId = idMapper.mapGroup(c.getId());

        return "{" + mappedId + "}";
    }
}
