package es.us.isa.cristal.model.constraints;

/**
 * Indica que se hace referencia justamente al recurso nombrado
 * 
 * @author Edelia
 *
 */
public class GroupResourceConstraint extends Constraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2833563239789523715L;

	/**
	 * Constructor de la clase
	 * 
	 * @param id Identificador del recurso
	 */
	public GroupResourceConstraint(String id) {

		super(id);
	}
}
