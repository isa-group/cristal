package es.us.isa.cristal.model.expressions;

import es.us.isa.cristal.model.constraints.PersonConstraint;

/**
 * Hace referencia a personas por su identificador. Expresión RAL "IS personConstraint"
 * 
 * @author Edelia
 *
 */
public class PersonExpr extends RALExpr {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1864234869556354745L;

	/**
	 * Constructor de la clase
	 * 
	 * @param personConstraint Restricción que deben satisfacer las personas
	 */
	public PersonExpr(PersonConstraint personConstraint) {
		
		super();
		this.personConstraint = personConstraint;
	}
	
	// restricción de persona
	protected PersonConstraint personConstraint;
	
	/**
	 * Devuelve el valor de la propiedad: personConstraint
	 * restricción de persona
	 * 
	 * @return Valor de la propiedad
	 */
	public PersonConstraint getPersonConstraint() {
		
		return this.personConstraint;
	}

}
