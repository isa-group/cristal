package es.us.isa.cristal.model.constraints;

/**
 * Indica que se hace referencia a la persona que ejecutó la actividad nombrada
 * 
 * @author Edelia
 *
 */
public class PersonWhoDidActivityConstraint extends PersonConstraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1530259330499545854L;

	/**
	 * Constructor de la clase
	 * 
	 * @param id Identificador de actividad
	 */
	public PersonWhoDidActivityConstraint(String id) {
		
		super(id);
	}
}
