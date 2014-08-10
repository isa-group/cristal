package es.us.isa.cristal.owl.mappers.ral.designtime;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.owl.mappers.ral.OwlConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.constraints.IdConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.constraints.PositionOfConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.designtime.constraints.DTActivityConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.designtime.constraints.DTDataConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.expr.*;
import es.us.isa.cristal.owl.mappers.ral.designtime.expr.DTNegativeExprMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.ActivityMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 18:06
 */
public class DTClassicOwlRalMapper extends OwlRalMapper {

    public DTClassicOwlRalMapper(IdMapper mapper, BPEngine engine, ActivityMapper activityMapper) {
        super(new DTConstraintMapper(mapper, activityMapper), engine);

        addMapper(new PersonExprMapper(constraintMapper));
        addMapper(new GroupResourceExprMapper(constraintMapper));
        addMapper(new CommonalityExprMapper(constraintMapper));
        addMapper(new CapabilityExprMapper(constraintMapper));
        addMapper(new IsAssignmentExprMapper(this, engine));
        addMapper(new ReportExprMapper(constraintMapper));
        addMapper(new DelegateExprMapper(constraintMapper));
        addMapper(new CompoundExprMapper(this));

        addMapper(new DTNegativeExprMapper(this, engine));
    }

    public static class DTConstraintMapper extends OwlConstraintMapper {
        public DTConstraintMapper(IdMapper idMapper, ActivityMapper activityMapper) {
            super(idMapper);

            addMapper(new IdConstraintMapper(idMapper));
            addMapper(new PositionOfConstraintMapper(this));
            addMapper(new DTDataConstraintMapper());
            addMapper(new DTActivityConstraintMapper(idMapper, activityMapper));
        }
    }
}
