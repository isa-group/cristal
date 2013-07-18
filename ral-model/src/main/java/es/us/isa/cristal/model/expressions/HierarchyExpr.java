package es.us.isa.cristal.model.expressions;

import es.us.isa.cristal.model.HierarchyDirection;
import es.us.isa.cristal.model.constraints.Constraint;
import es.us.isa.cristal.model.constraints.PositionConstraint;
import es.us.isa.cristal.model.constraints.RuntimeConstraint;

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

    @Override
    public boolean hasRuntimeConstraint() {
        return positionConstraint instanceof RuntimeConstraint;
    }

    @Override
    public Constraint[] getConstraints() {
        return new Constraint[]{positionConstraint};
    }
}
