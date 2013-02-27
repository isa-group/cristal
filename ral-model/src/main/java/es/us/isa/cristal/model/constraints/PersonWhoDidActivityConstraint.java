package es.us.isa.cristal.model.constraints;

/**
 * Indica que se hace referencia a la persona que ejecutó la actividad nombrada
 * 
 * @author Edelia
 *
 */
public class PersonWhoDidActivityConstraint extends ActivityConstraint implements PersonConstraint {

	/**
	 * Constructor de la clase
	 * 
	 * @param id Identificador de actividad
	 */
	public PersonWhoDidActivityConstraint(String id) {
		
		super(id);
	}
}
