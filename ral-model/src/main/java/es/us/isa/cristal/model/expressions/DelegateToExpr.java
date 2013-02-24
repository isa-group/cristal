package es.us.isa.cristal.model.expressions;

import es.us.isa.cristal.model.constraints.PositionConstraint;

/**
 * Hace referencia a las personas que pueden delegar a una posición. Expresión RAL "CAN DELEGATE WORK TO positionConstraint"
 * 
 * @author Edelia
 *
 */
public class DelegateToExpr extends RALExpr {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1322014175986802417L;

	/**
	 * Constructor de la clase
	 * 
	 * @param positionConstraint Restricción de posición
	 */
	public DelegateToExpr(PositionConstraint positionConstraint) {
		
		super();
		this.positionConstraint = positionConstraint;
	}

	// restricción de posición
	protected PositionConstraint positionConstraint;
	
	/**
	 * Devuelve la propiedad: positionConstraint
	 * restricción de posición
	 * 
	 * @return Valor de la propiedad
	 */
	public PositionConstraint getPositionConstraint() {
		
		return this.positionConstraint;
	}


}
