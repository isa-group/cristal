package es.us.isa.cristal.model.constraints;

/**
 * Indica que se hace referencia a la persona cuyo identificador es el valor de una propiedad de un dataobject
 * 
 * @author Edelia
 *
 */
public class PersonInDataFieldConstraint extends PersonConstraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3952814915336556004L;
	// identificador de campo de dataobject
	private String field;
	
	/**
	 * Constructor de la clase
	 * 
	 * @param id Identificador de dataobject
	 * @param field Identificador de propiedad
	 */
	public PersonInDataFieldConstraint(String id, String field) {

		super(id);
		this.field = field;
	}
	
	/**
	 * Devuelve la propiedad: field
	 * identificador de campo de dataobject
	 * 
	 * @return Valor de la propiedad
	 */
	public String getField() {
		
		return this.field;
	}
}
