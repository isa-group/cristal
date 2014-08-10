package es.us.isa.cristal.owl.mappers.ral.designtimesc.constraints;

import es.us.isa.cristal.model.constraints.ActivityConstraint;
import es.us.isa.cristal.model.constraints.Constraint;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.mappers.ral.ConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.ActivityMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 18:11
 */
public class DTSubClassActivityConstraintMapper implements ConstraintMapper {

    private IdMapper idMapper;
    private ActivityMapper activityMapper;

    public DTSubClassActivityConstraintMapper(IdMapper idMapper, ActivityMapper activityMapper) {
        this.idMapper = idMapper;
        this.activityMapper = activityMapper;
    }


    @Override
    public Class<? extends Constraint> getExprType() {
        return ActivityConstraint.class;
    }

    @Override
    public String map(Constraint expr, Object pid) {
        ActivityConstraint c = (ActivityConstraint) expr;

        return "inverse("+Definitions.HASRESPONSIBLE + ") some (" + activityMapper.mapActivity(c.getActivityName()) + ")";
        //Definitions.ISOFTYPE + " value " + idMapper.mapActivity(c.getActivityName()) + ")";
//        return "inverse("+Definitions.HASRESPONSIBLE + ") value  " + activityMapper.mapActivity(c.getActivityName());
    }
}
