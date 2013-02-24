package es.us.isa.cristal.model.expressions;

import es.us.isa.cristal.model.constraints.PositionConstraint;

/**
 * Las personas que reciben reportes. Expresión RAL "IS depth REPORTED BY positionConstraint"
 * 
 * @author Edelia
 *
 */
public class IsReportedByExpr extends RALExpr {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2530354425980110540L;

	/**
	 * Constructor de la clase
	 * 
	 * @param depth Indica si se reporta directamente o no
	 * @param positionConstraint Restricción de posición
	 */
	public IsReportedByExpr(boolean depth, PositionConstraint positionConstraint) {
		
		super();
		this.positionConstraint = positionConstraint;
		this.depth = depth;
	}

	// indica si se reporta directamente o no
	protected boolean depth;
	// restricción de posición
	protected PositionConstraint positionConstraint;

	/**
	 * Devuelve el valor de la propiedad: depth
	 * indica si se reporta directamente o no
	 * 
	 * @return Valor de la propiedad
	 */
	public boolean getDepth() {
		return depth;
	}

	/**
	 * Devuelve el valor de la propiedad: positionConstraint
	 * restricción de posición
	 * 
	 * @return Valor de la propiedad
	 */
	public PositionConstraint getPositionConstraint() {
		return positionConstraint;
	}
	

}
