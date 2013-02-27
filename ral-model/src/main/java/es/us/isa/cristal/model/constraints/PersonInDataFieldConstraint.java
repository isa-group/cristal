package es.us.isa.cristal.model.constraints;

/**
 * Indica que se hace referencia a la persona cuyo identificador es el valor de una propiedad de un dataobject
 * 
 * @author Edelia
 *
 */
public class PersonInDataFieldConstraint extends DataConstraint implements PersonConstraint {


	/**
	 * Constructor de la clase
	 * 
	 * @param id Identificador de dataobject
	 * @param field Identificador de propiedad
	 */
	public PersonInDataFieldConstraint(String id, String field) {

		super(id, field);
	}
	
}
