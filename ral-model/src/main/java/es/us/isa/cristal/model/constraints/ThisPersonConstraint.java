package es.us.isa.cristal.model.constraints;

/**
 * Indica que se hace referencia justamente a la persona nombrada
 * 
 * @author Edelia
 *
 */
public class ThisPersonConstraint extends PersonConstraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5301007767054017982L;

	/**
	 * Constructor de la clase
	 * 
	 * @param id Identificador de persona
	 */
	public ThisPersonConstraint(String id) {

		super(id);
	}
}
