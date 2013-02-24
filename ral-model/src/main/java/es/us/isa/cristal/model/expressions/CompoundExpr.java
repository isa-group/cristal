package es.us.isa.cristal.model.expressions;

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
	
}
