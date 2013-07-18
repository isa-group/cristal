package es.us.isa.cristal.owl.mappers.ral;

import es.us.isa.cristal.mappers.AbstractRalMapper;
import es.us.isa.cristal.model.constraints.Constraint;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 17:50
 */
public class OwlConstraintMapper extends AbstractRalMapper<Constraint, String, ConstraintMapper>  {
    protected IdMapper idMapper;

    public OwlConstraintMapper(IdMapper idMapper) {
        super();
        this.idMapper = idMapper;
    }

}
