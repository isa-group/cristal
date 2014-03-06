package es.us.isa.cristal.neo4j.analyzer.util;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.Bpmn20ModelHandlerImpl;
import es.us.isa.bpmn.xmlClasses.bpmn20.TPotentialOwner;
import es.us.isa.bpmn.xmlClasses.bpmn20.TResourceRole;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.constraints.PersonWhoDidActivityConstraint;
import es.us.isa.cristal.model.expressions.IsAssignmentExpr;
import es.us.isa.cristal.model.expressions.PersonExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.parser.RALParser;

/**
 * 
 * @author Manuel Leon
 *
 */
public class DesignTimeAnalyserBPEngine implements BPEngine {

	Bpmn20ModelHandler bpmn;
	Map<String,String> assignMap;

	public DesignTimeAnalyserBPEngine(String bpmn, Map<String,String> assignMap) throws Exception {
		super();
		this.bpmn = new Bpmn20ModelHandlerImpl();
		this.bpmn.load(new ByteArrayInputStream(bpmn.getBytes()));
		this.assignMap = assignMap;
	}

	public Object getDataValue(Object pid, String dataObjectName, String fieldName) {
		throw new UnsupportedOperationException("Operation not supported at Desing Time.");
	}

	public List<String> getActivityPerformer(Object pid, String activityId) {
		throw new UnsupportedOperationException("Operation not supported at Desing Time.");
	}

	public RALExpr getResourceExpression(Object processId, String activityId) {
		return getResourceExpressionByProcessDefinitionId(processId, activityId);
	}

	public RALExpr getResourceExpressionByProcessDefinitionId(Object processDefinitionId, String activityId) {
		return getResourceExpressionByProcessDefinitionId(processDefinitionId, activityId, new ArrayList<String>());
	}
	
	private RALExpr getResourceExpressionByProcessDefinitionId(Object processDefinitionId, String activityId, List<String> previousTasks) {
		
		RALExpr expr = null;
		if (this.assignMap != null && assignMap.containsKey(activityId)) {
			expr = RALParser.parse(assignMap.get(activityId));
		} else {
			for (TTask t : bpmn.getTaskMap().values()) {
				if (t.getName().equalsIgnoreCase(activityId) && t.getResourceRole() != null && !t.getResourceRole().isEmpty()) {
					for (JAXBElement<? extends TResourceRole> el : t.getResourceRole()) {
						if (el.getName().getLocalPart().equalsIgnoreCase("potentialOwner")) {
							TPotentialOwner owner = (TPotentialOwner) el.getValue();
							List<Serializable> content = owner.getResourceAssignmentExpression().getExpression().getValue().getContent();
							if (!content.isEmpty()) {
								
								expr = RALParser.parse(content.get(0).toString());
								break;
							}

						}
					}
				}
			}
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
