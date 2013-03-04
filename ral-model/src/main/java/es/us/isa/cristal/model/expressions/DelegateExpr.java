package es.us.isa.cristal.model.expressions;

import es.us.isa.cristal.model.HierarchyDirection;
import es.us.isa.cristal.model.constraints.PositionConstraint;

/**
 * Hace referencia a las personas que pueden delegar a una posición. Expresión RAL "CAN DELEGATE WORK TO positionConstraint"
 * 
 * @author Edelia
 *
 */
public class DelegateExpr extends HierarchyExpr {

    public DelegateExpr(PositionConstraint positionConstraint, HierarchyDirection direction) {
        super(direction, positionConstraint);
    }


}
