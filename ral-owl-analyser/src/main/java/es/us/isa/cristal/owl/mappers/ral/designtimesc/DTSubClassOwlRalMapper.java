package es.us.isa.cristal.owl.mappers.ral.designtimesc;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.owl.mappers.ral.OwlConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.constraints.IdConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.constraints.PositionOfConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.designtime.constraints.DTActivityConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.designtime.constraints.DTDataConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.designtime.expr.DTNegativeExprMapper;
import es.us.isa.cristal.owl.mappers.ral.designtimesc.constraints.DTSubClassActivityConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.designtimesc.constraints.DTSubClassDataConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.designtimesc.expr.DTSubClassNegativeExprMapper;
import es.us.isa.cristal.owl.mappers.ral.expr.*;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 18:06
 */
public class DTSubClassOwlRalMapper extends OwlRalMapper {

    public DTSubClassOwlRalMapper(IdMapper mapper, BPEngine engine, DTSubClassAssignmentOntology.ActivityMapper activityMapper) {
        super(new DTConstraintMapper(mapper, activityMapper), engine);

        addMapper(new PersonExprMapper(constraintMapper));
        addMapper(new GroupResourceExprMapper(constraintMapper));
        addMapper(new CommonalityExprMapper(constraintMapper));
        addMapper(new CapabilityExprMapper(constraintMapper));
        addMapper(new IsAssignmentExprMapper(this, engine));
        addMapper(new ReportExprMapper(constraintMapper));
        addMapper(new DelegateExprMapper(constraintMapper));
        addMapper(new CompoundExprMapper(this));

        addMapper(new DTSubClassNegativeExprMapper(this, engine));
    }

    public static class DTConstraintMapper extends OwlConstraintMapper {
        public DTConstraintMapper(IdMapper idMapper, DTSubClassAssignmentOntology.ActivityMapper activityMapper) {
            super(idMapper);

            addMapper(new IdConstraintMapper(idMapper));
            addMapper(new PositionOfConstraintMapper(this));
            addMapper(new DTSubClassDataConstraintMapper(idMapper));
            addMapper(new DTSubClassActivityConstraintMapper(idMapper, activityMapper));
        }
    }
}
