package es.us.isa.cristal.activiti;

import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricTaskInstance;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.Bpmn20ModelHandlerImpl;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.activiti.util.RalExpressionUtil;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.parser.RALParser;

/**
 * 
 * @author Manuel Leon
 *
 */
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

	

	public String getOrganizationDefinitionUrl(Object processDefinitionId){
		
		String result = null;
		org.activiti.bpmn.model.Process process = getProcessFromDefinitionKey(processDefinitionId);
		
		if(!process.getAttributes().containsKey("organization")){
			throw new RuntimeException("Organization attribute not found. Check the process has the organization attribute setted. Example: <process id=\"processId\" ral:organization=\"type URL here\"");
		}
		for(ExtensionAttribute att: process.getAttributes().get("organization")){
			if(att.getName().equals("organization")){
				result = att.getValue();
			}
		}
		
		return result;
		
	}

	public RALExpr getResourceExpressionByProcessDefinitionId(
			Object processDefinitionId, String activityName) {
		RALExpr result = null;
		
		//assumption: only one main process per bpmn20.xml file.
		org.activiti.bpmn.model.Process process = getProcessFromDefinitionKey(processDefinitionId);
		
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

	public RALExpr getResourceExpressionByProcessInstanceId(Object processInstanceId, String activityName) {
		String defKey = this.extractDefinitionKeyFromInstanceId((String) processInstanceId);
		return getResourceExpressionByProcessDefinitionId(defKey,activityName);
	}
	
	
	private org.activiti.bpmn.model.Process getProcessFromDefinitionKey(Object processDefinitionId){
		
		String processDeploymentId = getDeploymentId((String) processDefinitionId);
		
		BpmnModel bp = ProcessEngines.getDefaultProcessEngine().getRepositoryService().getBpmnModel(processDeploymentId);
		//assumption: only one main process per bpmn20.xml file.
		org.activiti.bpmn.model.Process process = bp.getProcesses().get(0);
		return process;
	}

	public RALExpr getResourceExpression(Object processId, String activityId) {
		if(isInstanceId((String) processId)) {
			return getResourceExpressionByProcessInstanceId(processId, activityId);
		}else{
			return getResourceExpressionByProcessDefinitionId(processId, activityId);
		}
	}
	
	private boolean isInstanceId(String processId){
		boolean res = false;
    	List<String> parts = Arrays.asList(processId.split(":",2));
    	if(parts.size()==2){
    		res = parts.get(1).matches("[0-9]+:[0-9]+");
    	}
		return res;
	}
	
	private String extractDefinitionKeyFromInstanceId(String processId){
		String result = processId;
		if(isInstanceId(processId)){
			result = processId.split(":",2)[0];
		}
		return result;
    	
	}

	private String getDeploymentId(String definitionKey){
		return ProcessEngines.getDefaultProcessEngine().getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(definitionKey).orderByDeploymentId().desc().list().get(0).getId();
	}
	
	@Override
	public Bpmn20ModelHandler getBpmnModel(Object processId) {
		String pid;
		if(isInstanceId((String) processId)) {
			pid=this.extractDefinitionKeyFromInstanceId((String) processId);
		}else{
			pid = (String) processId;
		}

		InputStream is = ProcessEngines.getDefaultProcessEngine().getRepositoryService().getProcessModel(pid);
		
		Bpmn20ModelHandler bpmn = new Bpmn20ModelHandlerImpl();
		try {
			bpmn.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bpmn;
	}
	
	
	
	
}
