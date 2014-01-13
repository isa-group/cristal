package es.us.isa.cristal;

import java.util.List;

import es.us.isa.cristal.model.expressions.RALExpr;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 19:53
 */
public interface BPEngine {
	/**
	 * 
	 * @param pid Process Instance Identifier
	 * @param dataObjectName The name of the object which contains the datafield
	 * @param fieldName The name of the field which will be read. Use null to return the dataObject
	 * @return the value of the data
	 * 
	 * 
	 */
    public Object getDataValue(Object pid, String dataObjectName, String fieldName);
    /**
     * 
     * @param pid Process Instance Identifier
     * @param activityId Activity Identifier
     * @return the name of the person who completed the activity.
     */
    public List<String> getActivityPerformer(Object pid, String activityId);
    
    /**
     * 
     * @param processDefinitionId The id defined in the process
     * @param activityId Activity Identifier
     * @return the RAL expression used to calculate candidates to perform the activity
     */
    RALExpr getResourceExpression(Object processDefinitionId, String activityId);
}
