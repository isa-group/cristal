package es.us.isa.cristal.model.expressions;

public class OrExpr extends CompoundExpr {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5258542249828708949L;

	public OrExpr(RALExpr objectExprLeft, RALExpr objectExprRight) {
		
		super(objectExprLeft, objectExprRight);
	}

}
