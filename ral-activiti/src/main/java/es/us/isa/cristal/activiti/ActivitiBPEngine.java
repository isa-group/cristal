package es.us.isa.cristal.activiti;

import java.util.LinkedList;
import java.util.List;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricTaskInstance;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.activiti.util.RalExpressionUtil;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.parser.RALParser;

public class ActivitiBPEngine implements BPEngine{


	public List<String> getActivityPerformer(Object pid, String activityName) {
		List<HistoricTaskInstance> list = ProcessEngines.getDefaultProcessEngine().getHistoryService().createHistoricTaskInstanceQuery().processInstanceId((String) pid).taskName(activityName).list();
		if(list.isEmpty()){
			throw new RuntimeException("ActivitiBPEngine: No HistoricTaskInstance found for taskId: " + pid);
		}
		List<String> result = new LinkedList<String>();
		for(HistoricTaskInstance task: list){
			result.add(task.getAssignee());
		}
		return result;
		
	}

	public Object getDataValue(Object pid, String data, String property) {
		Object result = null;
		Object x = ProcessEngines.getDefaultProcessEngine().getRuntimeService().getVariable((String) pid, data);
		if(property!=null){
			 try {
				result = x.getClass().getField(property).get(x);
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				
				e.printStackTrace();
			} catch (SecurityException e) {
				
				e.printStackTrace();
			}
		}else{
			result = x;
		}
		return result;
	}

	public RALExpr getResourceExpression(Object processDefinitionId, String activityName) {
		RALExpr result = null;
		BpmnModel bp = ProcessEngines.getDefaultProcessEngine().getRepositoryService().getBpmnModel((String) processDefinitionId);
		
		//assumption: only one main process per bpmn20.xml file.
		org.activiti.bpmn.model.Process process = bp.getProcesses().get(0);
		
		for(FlowElement element: process.getFlowElements()){
			if(element.getName().equals(activityName)){
				//the list of users must have only 1 position: the RAL expression.
				//if you want to allow defining users combining RAL with other possibilities like
				//"RAL(expression), user1, user2", then you will have to find the RAL expression
				//inside the users list, and design a strategy to combine both possibilities.
				
				String users = ((UserTask) element).getCandidateUsers().get(0);
				if(RalExpressionUtil.isRALExpression(users)){
					String ralExp = RalExpressionUtil.extractRalExpression(users);
					result = RALParser.parse(ralExp);
				}
				//no need to continue iterating
				break;
			}
		}
		
		return result;
	
	}

	public String getOrganizationDefinitionUrl(Object processDefinitionId){
		String result = null;
		BpmnModel bp = ProcessEngines.getDefaultProcessEngine().getRepositoryService().getBpmnModel((String) processDefinitionId);
		
		//assumption: only one main process per bpmn20.xml file.
		org.activiti.bpmn.model.Process process = bp.getProcesses().get(0);
		
		result = "https://www.dropbox.com/s/scvrx6cmuh3bce8/testOrganizationModel.json";
		
		return result;
		
	}
	
	
}
