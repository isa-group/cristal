package es.us.isa.cristal.model.expressions;

import es.us.isa.cristal.model.constraints.Constraint;

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
	protected String processDefinitionId;
	/**
	 * Constructor de la clase
	 * 
	 * @param activityName Identificador de la actividad
	 */
	public IsAssignmentExpr(String processDefinitionId, String activityName) {
		super();
		this.activityName = activityName;
		this.processDefinitionId = processDefinitionId;
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
	
    public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	@Override
    public boolean hasRuntimeConstraint() {
        return false;
    }

    @Override
    public Constraint[] getConstraints() {
        return new Constraint[]{};
    }
}
