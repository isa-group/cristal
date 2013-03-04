package es.us.isa.cristal.model.expressions;

import es.us.isa.cristal.model.HierarchyDirection;
import es.us.isa.cristal.model.constraints.PositionConstraint;

/**
 * User: resinas
 * Date: 04/03/13
 * Time: 09:46
 */
public abstract class HierarchyExpr extends RALExpr {

    protected PositionConstraint positionConstraint;
    protected HierarchyDirection direction;

    public HierarchyExpr(HierarchyDirection direction, PositionConstraint positionConstraint) {
        this.direction = direction;
        this.positionConstraint = positionConstraint;
    }

    public HierarchyDirection getDirection() {
        return direction;
    }

	public PositionConstraint getPositionConstraint() {

		return this.positionConstraint;
	}
}
