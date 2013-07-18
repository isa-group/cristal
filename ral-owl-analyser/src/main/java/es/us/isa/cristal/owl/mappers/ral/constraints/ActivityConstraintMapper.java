package es.us.isa.cristal.owl.mappers.ral.constraints;

import es.us.isa.cristal.model.constraints.ActivityConstraint;
import es.us.isa.cristal.model.constraints.Constraint;
import es.us.isa.cristal.owl.mappers.ral.ConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.LogMapper;

import static es.us.isa.cristal.owl.Definitions.*;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 18:11
 */
public class ActivityConstraintMapper implements ConstraintMapper {

    private IdMapper idMapper;

    public ActivityConstraintMapper(IdMapper idMapper) {
        this.idMapper = idMapper;
    }


    @Override
    public Class<? extends Constraint> getExprType() {
        return ActivityConstraint.class;
    }

    @Override
    public String map(Constraint expr, Object pid) {
        ActivityConstraint c = (ActivityConstraint) expr;
        String currentInstance = new LogMapper().map(pid.toString());
        String activity = idMapper.mapActivity(c.getActivityName());

        return "inverse("+HASRESPONSIBLE + ") some ( inverse("+HASACTIVITYINSTANCE+") value "+ currentInstance + " AND " + ISOFTYPE + " value " + activity + " AND "+ HASSTATE +" some " + AFTERALLOCATION + ")";
    }
}
