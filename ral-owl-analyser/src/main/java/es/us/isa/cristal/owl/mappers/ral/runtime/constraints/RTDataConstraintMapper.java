package es.us.isa.cristal.owl.mappers.ral.runtime.constraints;

import es.us.isa.cristal.model.GroupResourceType;
import es.us.isa.cristal.model.constraints.Constraint;
import es.us.isa.cristal.model.constraints.DataConstraint;
import es.us.isa.cristal.model.constraints.GroupResourceInDataFieldConstraint;
import es.us.isa.cristal.model.constraints.PersonInDataFieldConstraint;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.mappers.ral.ConstraintMapper;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 18:11
 */
public class RTDataConstraintMapper implements ConstraintMapper {

    @Override
    public Class<? extends Constraint> getExprType() {
        return DataConstraint.class;
    }

    @Override
    public String map(Constraint expr, Object pid) {
        DataConstraint c = (DataConstraint) expr;
        String mapped;

        if (c instanceof PersonInDataFieldConstraint)
            mapped = Definitions.PERSON;
        else if (c instanceof GroupResourceInDataFieldConstraint) {
            mapped = getMapForDataConstraint(((GroupResourceInDataFieldConstraint) c).getGroupResourceType());
        }
        else {
            throw new RuntimeException("Invalid type of data constraint");
        }

        return mapped;
    }

    private String getMapForDataConstraint(GroupResourceType type) {
        String mapc;

        if (type == GroupResourceType.POSITION)
            mapc = Definitions.POSITION;
        else if (type == GroupResourceType.UNIT) {
            mapc = Definitions.UNIT;
        }
        else if (type == GroupResourceType.ROLE) {
            mapc = Definitions.ROLE;
        } else {
            throw new RuntimeException("Not valid type in group resource expression");
        }

        return mapc;
    }

}
