package es.us.isa.cristal.owl.mappers.ral.constraints;

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
public class DataConstraintMapper implements ConstraintMapper {

    private IdMapper idMapper;

    public DataConstraintMapper(IdMapper idMapper) {
        this.idMapper = idMapper;
    }

    @Override
    public Class<? extends Constraint> getExprType() {
        return DataConstraint.class;
    }

    @Override
    public String map(Constraint expr, Object pid) {
        DataConstraint c = (DataConstraint) expr;
        String currentInstance = new LogMapper().map(pid.toString());
        String field = idMapper.mapActivity(c.getField());
        String data = idMapper.mapActivity(c.getData());

        String mapped = "inverse("+field+") some ("+ ISOFTYPE + " value " + data + " AND inverse(" + HASDATAOBJECTINSTANCE + ") value " + currentInstance ;

        return mapped;
    }

}
