package es.us.isa.cristal.model.expressions;

import es.us.isa.cristal.model.HierarchyDirection;
import es.us.isa.cristal.model.constraints.PositionConstraint;

/**
 * Las personas que reportan a otras. Expresión RAL "REPORTS TO positionConstraint directly"
 * 
 * @author Edelia
 *
 */
public class ReportExpr extends HierarchyExpr {

    private boolean directly;

    public ReportExpr(boolean directly, PositionConstraint positionConstraint, HierarchyDirection direction) {
        super(direction, positionConstraint);
        this.directly = directly;
    }

    /**
	 * Devuelve el valor de la propiedad: directly
	 * indica si se reporta directamente o no
	 * 
	 * @return Valor de la propiedad
	 */
	public boolean getDirectly() {
		return this.directly;
	}

}
