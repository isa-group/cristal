package es.us.isa.cristal.neo4j.analyzer.util;

import java.util.ArrayList;
import java.util.List;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.BpmnAssignmentModelHandler;
import es.us.isa.cristal.RawResourceAssignment;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.constraints.PersonWhoDidActivityConstraint;
import es.us.isa.cristal.model.expressions.IsAssignmentExpr;
import es.us.isa.cristal.model.expressions.PersonExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.parser.RALParser;

/**
 *
 * It provides a BPEngine implementation for design-time analysis. It provides information for just one model (given
 * in the constructor) and it also allows overriding the assignments made in the BPMN model with a RawResourceAssignment
 *
 * @author Manuel Leon
 *
 */
public class DesignTimeAnalyserBPEngine implements BPEngine {

	Bpmn20ModelHandler bpmn = null;
	RawResourceAssignment assignMap = null;

    public DesignTimeAnalyserBPEngine(Bpmn20ModelHandler bpmn) {
        super();
        this.bpmn = bpmn;
    }

    public DesignTimeAnalyserBPEngine(Bpmn20ModelHandler bpmn, RawResourceAssignment assignMap) {
		super();
		this.bpmn = bpmn;
        this.assignMap = assignMap;
    }

	public Object getDataValue(Object pid, String dataObjectName, String fieldName) {
		throw new UnsupportedOperationException("Operation not supported at Design Time.");
	}

	public List<String> getActivityPerformer(Object pid, String activityId) {
		throw new UnsupportedOperationException("Operation not supported at Design Time.");
	}

	public RALExpr getResourceExpression(Object processId, String activityId) {
		return getResourceExpressionByProcessDefinitionId(processId, activityId);
	}

	public RALExpr getResourceExpressionByProcessDefinitionId(Object processDefinitionId, String activityId) {
		return getResourceExpressionByProcessDefinitionId(processDefinitionId, activityId, new ArrayList<String>());
	}
	
	private RALExpr getResourceExpressionByProcessDefinitionId(Object processDefinitionId, String activityId, List<String> previousTasks) {
		RALExpr expr = null;

		if (this.assignMap != null && assignMap.get(activityId, TaskDuty.RESPONSIBLE) != null) {
            expr = RALParser.parse(assignMap.get(activityId, TaskDuty.RESPONSIBLE));
        } else if (bpmn != null) {
            BpmnAssignmentModelHandler handler = new BpmnAssignmentModelHandler(bpmn);
            expr = handler.getPotentialOwnerRalExpr(activityId);
        }
		
		String newActivity = null;
		//if the expression is: IS ASSIGNMENT IN ACTIVITY || IS PERSON WHO DID ACTIVITY...
		if(expr instanceof IsAssignmentExpr){
			newActivity = ((IsAssignmentExpr) expr).getActivityName();
		}else if(expr instanceof PersonExpr && ((PersonExpr) expr).getPersonConstraint() instanceof PersonWhoDidActivityConstraint){
			newActivity = ((PersonWhoDidActivityConstraint) ((PersonExpr) expr).getPersonConstraint()).getActivityName();
		}
		
		if(newActivity!=null){
			if(previousTasks.contains(newActivity)){
				throw new RuntimeException("Cycle found analysing RAL Expressions for activities: " + previousTasks);
			}else{
				previousTasks.add(newActivity);
				expr = getResourceExpressionByProcessDefinitionId(processDefinitionId, newActivity, previousTasks);
			}
		}
		return expr;
	}


    public RALExpr getResourceExpressionByProcessInstanceId(Object processInstanceId, String activityId) {
		throw new UnsupportedOperationException("Operation not supported at Desing Time.");
	}

	public Bpmn20ModelHandler getBpmnModel(Object processId) {
		return bpmn;
	}

}
