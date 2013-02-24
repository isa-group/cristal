package es.us.isa.cristal.model.expressions;

import es.us.isa.cristal.model.constraints.PositionConstraint;

/**
 * Las personas que reportan a otras. Expresión RAL "REPORTS TO positionConstraint depth"
 * 
 * @author Edelia
 *
 */
public class ReportsToExpr extends RALExpr {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3240367517581811320L;

	/**
	 * Constructor de la clase
	 * 
	 * @param depth Indica si se reporta directamente o no
	 * @param positionConstraint Restricción de posición
	 */
	public ReportsToExpr(boolean depth, PositionConstraint positionConstraint) {
		
		super();
		this.depth = depth;
		this.positionConstraint = positionConstraint;
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
		return this.depth;
	}

	/**
	 * Devuelve el valor de la propiedad: positionConstraint
	 * restricción de posición
	 * 
	 * @return Valor de la propiedad
	 */
	public PositionConstraint getPositionConstraint() {
		return this.positionConstraint;
	}
}
