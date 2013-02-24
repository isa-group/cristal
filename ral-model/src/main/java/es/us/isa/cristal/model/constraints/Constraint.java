package es.us.isa.cristal.model.constraints;

import java.io.Serializable;

/**
 * Superclase de las restricciones
 * 
 * @author Edelia
 *
 */
public class Constraint implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8846014169968874882L;
	// identificador de la restricción
	private String id;
	
	/**
	 * Constructor de la clase
	 * 
	 * @param id Identificador de la restricción
	 */
	public Constraint(String id) {
		
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
