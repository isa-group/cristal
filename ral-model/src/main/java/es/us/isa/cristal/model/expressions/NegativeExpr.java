package es.us.isa.cristal.model.expressions;

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

}
