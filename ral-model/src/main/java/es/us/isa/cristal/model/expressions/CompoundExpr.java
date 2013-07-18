package es.us.isa.cristal.model.expressions;

import es.us.isa.cristal.model.constraints.Constraint;

import java.util.Arrays;
import java.util.List;

public abstract class CompoundExpr extends RALExpr {

	/**
	 * 
	 */
	private static final long serialVersionUID = -449943396525938032L;

	public CompoundExpr(RALExpr objectExprLeft, RALExpr objectExprRight) {
		
		super();
		this.objectExprLeft = objectExprLeft;
		this.objectExprRight = objectExprRight;
	}
	
	protected RALExpr objectExprLeft;
	protected RALExpr objectExprRight;
	
	public RALExpr getObjectExprLeft() {
		return objectExprLeft;
	}
	public RALExpr getObjectExprRight() {
		return objectExprRight;
	}

    @Override
    public boolean hasRuntimeConstraint() {
        return objectExprLeft.hasRuntimeConstraint() || objectExprRight.hasRuntimeConstraint();
    }

    @Override
    public Constraint[] getConstraints() {
        List<Constraint> constraints = Arrays.asList(objectExprLeft.getConstraints());
        constraints.addAll(Arrays.asList(objectExprRight.getConstraints()));

        return constraints.toArray(new Constraint[0]);
    }
}
