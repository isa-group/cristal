package es.us.isa.cristal.model.constraints;

/**
 * Superclase de las restricciones
 * 
 * @author Edelia
 *
 */
public abstract class IdConstraint implements Constraint {
	
	// identificador de la restricción
	private String id;
	
	/**
	 * Constructor de la clase
	 * 
	 * @param id Identificador de la restricción
	 */
	public IdConstraint(String id) {
		
		this.id = id;
	}
	
	/**
	 * Devuelve la propiedad: id
	 * identificador de la restricción. Su significado depende de la subclase
	 * 
	 * @return Valor de la propiedad
	 */
	public String getId() {
		
		return this.id;
	}
}
