package es.us.isa.cristal.model.expressions;

/**
 * Hace referencia a las personas que están asignadas a una actividad. Expresión RAL "IS ASSIGNMENT IN ACTIVITY activityName"
 * 
 * @author Edelia
 *
 */
public class IsAssignmentExpr extends RALExpr {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2957284638582593646L;
	// identificador de la actividad
	protected String activityName;

	/**
	 * Constructor de la clase
	 * 
	 * @param activityName Identificador de la actividad
	 */
	public IsAssignmentExpr(String activityName) {
		
		super();
		
		this.activityName = activityName;
	}
	
	/**
	 * Devuelve el valor de la propiedad: activityName
	 * identificador de la actividad
	 * 
	 * @return Valor de la propiedad
	 */
	public String getActivityName() {
		
		return this.activityName;
	}
}
