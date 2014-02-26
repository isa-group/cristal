package es.us.isa.cristal.owl.mappers.ral.designtime.constraints;

import es.us.isa.cristal.model.constraints.ActivityConstraint;
import es.us.isa.cristal.model.constraints.Constraint;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.mappers.ral.ConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 18:11
 */
public class DTActivityConstraintMapper implements ConstraintMapper {

    private IdMapper idMapper;

    public DTActivityConstraintMapper(IdMapper idMapper) {
        this.idMapper = idMapper;
    }


    @Override
    public Class<? extends Constraint> getExprType() {
        return ActivityConstraint.class;
    }

    @Override
    public String map(Constraint expr, Object pid) {
        ActivityConstraint c = (ActivityConstraint) expr;

        return Definitions.ISPOTENTIALRESPONSIBLE + " value " + idMapper.mapActivity(c.getActivityName());
    }
}
