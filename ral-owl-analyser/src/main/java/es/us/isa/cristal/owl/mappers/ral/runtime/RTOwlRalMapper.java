package es.us.isa.cristal.owl.mappers.ral.runtime;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.owl.mappers.ral.OwlConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;
import es.us.isa.cristal.owl.ontologyhandlers.LogOntologyHandler;
import es.us.isa.cristal.owl.mappers.ral.constraints.IdConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.constraints.PositionOfConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.runtime.constraints.RTActivityConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.runtime.constraints.RTDataConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.expr.*;
import es.us.isa.cristal.owl.mappers.ral.runtime.expr.RTNegativeExprMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 18:06
 */
public class RTOwlRalMapper extends OwlRalMapper {

    public RTOwlRalMapper(IdMapper mapper, BPEngine engine, LogOntologyHandler logOntologyHandler) {
        super(new RTConstraintMapper(mapper), engine);

        addMapper(new PersonExprMapper(constraintMapper));
        addMapper(new GroupResourceExprMapper(constraintMapper));
        addMapper(new CommonalityExprMapper(constraintMapper));
        addMapper(new CapabilityExprMapper(constraintMapper));
        addMapper(new IsAssignmentExprMapper(this, engine));
        addMapper(new ReportExprMapper(constraintMapper));
        addMapper(new DelegateExprMapper(constraintMapper));
        addMapper(new CompoundExprMapper(this));

        addMapper(new RTNegativeExprMapper(this, mapper, logOntologyHandler));
    }

    public static class RTConstraintMapper extends OwlConstraintMapper {
        public RTConstraintMapper(IdMapper idMapper) {
            super(idMapper);

            addMapper(new IdConstraintMapper(idMapper));
            addMapper(new PositionOfConstraintMapper(this));
            addMapper(new RTDataConstraintMapper());
            addMapper(new RTActivityConstraintMapper(idMapper));
        }
    }
}
