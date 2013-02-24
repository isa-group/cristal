package es.us.isa.cristal.model.expressions;

import es.us.isa.cristal.model.CommonalityAmount;
import es.us.isa.cristal.model.GroupResourceType;
import es.us.isa.cristal.model.constraints.PersonConstraint;

/**
 * Las personas que comparter todos o algunos recursos (posición, role o unidad) con otras. 
 * Expresión RAL "SHARES amount groupResourceType WITH personConstraint"
 * 
 * @author Edelia
 *
 */
public class CommonalityExpr extends RALExpr {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4343355596851360750L;

	/**
	 * Constructor de la clase
	 * 
	 * @param amount Si se comparten todos o algunos recursos
	 * @param groupResourceType Tipo de recurso
	 * @param personConstraint Restricción de persona
	 */
	public CommonalityExpr(CommonalityAmount amount, GroupResourceType groupResourceType, PersonConstraint personConstraint) {
		
		super();
		this.amount = amount;
		this.groupResourceType = groupResourceType;
		this.personConstraint = personConstraint;
	}

	// si se comparten todos o algunos recursos
	protected CommonalityAmount amount;
	// tipo de recurso
	protected GroupResourceType groupResourceType;
	// restricción de persona
	protected PersonConstraint personConstraint;
	
	/**
	 * Devuelve el valor de la propiedad: amount
	 * si se comparten todos o algunos recursos
	 * 
	 * @return Valor de la propiedad
	 */
	public CommonalityAmount getAmount() {
		
		return this.amount;
	}

	/**
	 * Devuelve el valor de la propiedad: groupResourceType
	 * tipo de recurso
	 * 
	 * @return Valor de la propiedad
	 */
	public GroupResourceType getGroupResourceType() {
		
		return this.groupResourceType;
	}

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
