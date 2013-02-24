package es.us.isa.cristal.model.expressions;

import es.us.isa.cristal.model.GroupResourceType;
import es.us.isa.cristal.model.constraints.GroupResourceConstraint;

/**
 * Hace referencia a las personas que utilizan un recurso. Expresión RAL "HAS groupResourceType groupResourceConstraint"
 * 
 * @author Edelia
 *
 */
public class GroupResourceExpr extends RALExpr {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1282134935303833423L;

	/**
	 * Constructor de la clase
	 * 
	 * @param groupResourceType Tipo de recurso (posición, rol o unidad organizativa)
	 * @param groupResourceConstraint Restricción del recurso
	 */
	public GroupResourceExpr(GroupResourceType groupResourceType, GroupResourceConstraint groupResourceConstraint) {
		
		super();
		this.groupResourceType = groupResourceType;
		this.groupResourceConstraint = groupResourceConstraint;
	}

	// tipo de recurso
	protected GroupResourceType groupResourceType;
	// restricción del recurso
	protected GroupResourceConstraint groupResourceConstraint;
	
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
	 * Devuelve el valor de la propiedad: groupResourceConstraint
	 * restricción del recurso
	 * 
	 * @return Valor de la propiedad
	 */
	public GroupResourceConstraint getGroupResourceConstraint() {
		
		return this.groupResourceConstraint;
	}


}
