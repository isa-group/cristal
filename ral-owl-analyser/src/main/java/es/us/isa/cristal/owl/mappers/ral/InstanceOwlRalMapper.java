package es.us.isa.cristal.owl.mappers.ral;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.owl.mappers.ral.constraints.ActivityConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.constraints.DataConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.constraints.IdConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.constraints.PositionOfConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.expr.*;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 18:06
 */
public class InstanceOwlRalMapper extends OwlRalMapper {

    public InstanceOwlRalMapper(IdMapper mapper, BPEngine engine) {
        super(new InstanceConstraintMapper(mapper), engine);

        addMapper(new PersonExprMapper(constraintMapper));
        addMapper(new GroupResourceExprMapper(constraintMapper));
        addMapper(new CommonalityExprMapper(constraintMapper));
        addMapper(new CapabilityExprMapper(constraintMapper));
        addMapper(new IsAssignmentExprMapper(this, engine));
        addMapper(new ReportExprMapper(constraintMapper));
        addMapper(new DelegateExprMapper(constraintMapper));
        addMapper(new CompoundExprMapper(this));

        addMapper(new NegativeExprMapper(this));
    }

    public static class InstanceConstraintMapper extends OwlConstraintMapper  {
        public InstanceConstraintMapper(IdMapper idMapper) {
            super(idMapper);

            addMapper(new IdConstraintMapper(idMapper));
            addMapper(new PositionOfConstraintMapper(this));
            addMapper(new DataConstraintMapper(idMapper));
            addMapper(new ActivityConstraintMapper(idMapper));
        }
    }
}
