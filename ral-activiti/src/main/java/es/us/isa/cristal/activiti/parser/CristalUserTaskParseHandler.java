package es.us.isa.cristal.activiti.parser;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.UserTaskParseHandler;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.form.DefaultTaskFormHandler;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.apache.commons.lang3.StringUtils;

import es.us.isa.cristal.activiti.util.RalExpressionUtil;

public class CristalUserTaskParseHandler extends UserTaskParseHandler{
	
	private String ralResolverServiceName;
	
	private String ralResolverMethodName;
	
	
	
	public CristalUserTaskParseHandler(String ralResolverServiceName, String ralResolverMethodName){
		this.ralResolverServiceName = ralResolverServiceName;
		this.ralResolverMethodName = ralResolverMethodName;
	}
	
	private String calculateFinalExpression(String processId, String name){
		String result = name;
		if(RalExpressionUtil.isRALExpression(name)){
			String ralExp = RalExpressionUtil.extractRalExpression(name);
			result = "${" + ralResolverServiceName + "." + ralResolverMethodName + "('" + processId + "','" + ralExp + "')}";
		}
		return result;
	}

	
	
	@Override
	public TaskDefinition parseTaskDefinition(BpmnParse bpmnParse, UserTask userTask, String taskDefinitionKey, ProcessDefinitionEntity processDefinition) {
	    TaskFormHandler taskFormHandler = new DefaultTaskFormHandler();
	    taskFormHandler.parseConfiguration(userTask.getFormProperties(), userTask.getFormKey(), bpmnParse.getDeployment(), processDefinition);
	    
	    TaskDefinition taskDefinition = new TaskDefinition(taskFormHandler);

	    taskDefinition.setKey(taskDefinitionKey);
	    processDefinition.getTaskDefinitions().put(taskDefinitionKey, taskDefinition);
	    ExpressionManager expressionManager = bpmnParse.getExpressionManager();

	    if (StringUtils.isNotEmpty(userTask.getName())) {
	      taskDefinition.setNameExpression(expressionManager.createExpression(userTask.getName()));
	    }

	    if (StringUtils.isNotEmpty(userTask.getDocumentation())) {
	      taskDefinition.setDescriptionExpression(expressionManager.createExpression(userTask.getDocumentation()));
	    }

	    if (StringUtils.isNotEmpty(userTask.getAssignee())) {
	      taskDefinition.setAssigneeExpression(expressionManager.createExpression(calculateFinalExpression(processDefinition.getKey(), userTask.getAssignee())));
	    }
	    
	    
	    
	    if (StringUtils.isNotEmpty(userTask.getOwner())) {
	      taskDefinition.setOwnerExpression(expressionManager.createExpression(userTask.getOwner()));
	    }
	    for (String candidateUser : userTask.getCandidateUsers()) {
	      taskDefinition.addCandidateUserIdExpression(expressionManager.createExpression(calculateFinalExpression(processDefinition.getKey(),candidateUser)));
	    }
	    for (String candidateGroup : userTask.getCandidateGroups()) {
	      taskDefinition.addCandidateGroupIdExpression(expressionManager.createExpression(calculateFinalExpression(processDefinition.getKey(),candidateGroup)));
	    }
	    
	    // Activiti custom extension
	    
	    // Task listeners
	    for (ActivitiListener taskListener : userTask.getTaskListeners()) {
	      taskDefinition.addTaskListener(taskListener.getEvent(), createTaskListener(bpmnParse, taskListener, userTask.getId()));
	    }

	    // Due date
	    if (StringUtils.isNotEmpty(userTask.getDueDate())) {
	      taskDefinition.setDueDateExpression(expressionManager.createExpression(userTask.getDueDate()));
	    }
	    
	    // Priority
	    if (StringUtils.isNotEmpty(userTask.getPriority())) {
	      taskDefinition.setPriorityExpression(expressionManager.createExpression(userTask.getPriority()));
	    }

	    return taskDefinition;
	  }
	
	
	
}
