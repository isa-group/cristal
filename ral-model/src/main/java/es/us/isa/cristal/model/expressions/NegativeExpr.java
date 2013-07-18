package es.us.isa.cristal.model.expressions;

import es.us.isa.cristal.model.constraints.Constraint;

public class NegativeExpr extends RALExpr {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6052680040949535718L;

	public NegativeExpr(RALExpr exprObject) {
		
		super();
		this.exprObject = exprObject;
	}

	protected RALExpr exprObject;
	
	public RALExpr getExprObject() {
		
		return this.exprObject;
	}

    @Override
    public boolean hasRuntimeConstraint() {
        return exprObject.hasRuntimeConstraint();
    }

    @Override
    public Constraint[] getConstraints() {
        return exprObject.getConstraints();
    }
}
