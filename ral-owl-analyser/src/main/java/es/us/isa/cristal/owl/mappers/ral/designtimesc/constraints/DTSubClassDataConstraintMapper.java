package es.us.isa.cristal.owl.mappers.ral.designtimesc.constraints;

import es.us.isa.cristal.model.constraints.Constraint;
import es.us.isa.cristal.model.constraints.DataConstraint;
import es.us.isa.cristal.owl.mappers.ral.ConstraintMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.LogMapper;

import static es.us.isa.cristal.owl.Definitions.HASDATAOBJECTINSTANCE;
import static es.us.isa.cristal.owl.Definitions.ISOFTYPE;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 18:11
 */
public class DTSubClassDataConstraintMapper implements ConstraintMapper {

    private IdMapper idMapper;

    public DTSubClassDataConstraintMapper(IdMapper idMapper) {
        this.idMapper = idMapper;
    }

    @Override
    public Class<? extends Constraint> getExprType() {
        return DataConstraint.class;
    }

    @Override
    public String map(Constraint expr, Object pid) {
        DataConstraint c = (DataConstraint) expr;
        String field = idMapper.mapActivity(c.getField());
        String data = idMapper.mapActivity(c.getData());

        String mapped = "inverse("+field+") some ("+ ISOFTYPE + " value " + data + ")";

        return mapped;
    }

}
