package es.us.isa.cristal.owl.mappers.ral;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.mappers.AbstractRalMapper;
import es.us.isa.cristal.model.expressions.RALExpr;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 18:06
 */
public class OwlRalMapper extends AbstractRalMapper<RALExpr, String, ExprMapper> {
    protected OwlConstraintMapper constraintMapper;

    public OwlRalMapper(OwlConstraintMapper constraintMapper, BPEngine engine) {
        super();
        this.constraintMapper = constraintMapper;
    }
}
