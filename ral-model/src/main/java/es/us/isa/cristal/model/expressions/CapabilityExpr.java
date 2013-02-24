package es.us.isa.cristal.model.expressions;

/**
 * Hace referencia a las personas que tienen una capacidad. Expresión RAL "HAS CAPABILITY capabilityConstraint"
 * 
 * @author Edelia
 *
 */
public class CapabilityExpr extends RALExpr {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5488770735441699579L;

	/**
	 * Constructor de la clase
	 * 
	 * @param capabilityName Identificador de la capacidad
	 */
	public CapabilityExpr(String capabilityName) {
		
		super();
		this.capabilityName = capabilityName;
	}

	// identificador de la capacidad
	protected String capabilityName;

	/**
	 * Devuelve la propiedad: capabilityName
	 * identificador de la capacidad
	 * 
	 * @return Valor de la propiedad
	 */
	public String getCapabilityName() {
		
		return this.capabilityName;
	}

}
