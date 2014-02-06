package es.us.isa.cristal.performance.tester.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.organization.generator.selectors.RandomSelector;
import es.us.isa.cristal.organization.generator.selectors.Selector;
import es.us.isa.cristal.organization.model.gson.Model;
import es.us.isa.cristal.organization.model.gson.Person;
import es.us.isa.cristal.parser.RALParser;
import es.us.isa.cristal.performance.tester.data.Query;

public class MockBPEngine implements BPEngine{

	private Model model;
	
	private Selector<Person> selector;
	
	private Map<String, Query> map;
	
	public MockBPEngine(Model model, Map<String,Query> queryMap) {
		super();
		this.model = model;
		selector = new RandomSelector<Person>();
	}

	public Object getDataValue(Object pid, String dataObjectName,
			String fieldName) {
		return getActivityPerformer(pid,"");
	}

	public List<String> getActivityPerformer(Object pid, String activityId) {
		return Arrays.asList(selector.getIndividual(model.getPersons()).getName());
	}

	public RALExpr getResourceExpression(Object processDefinitionId,
			String activityId) {
		return RALParser.parse(map.get(activityId).getQuery());
	}

	@Override
	public RALExpr getResourceExpressionByProcessDefinitionId(
			Object processDefinitionId, String activityId) {
		
		return getResourceExpression(processDefinitionId, activityId);
	}

	@Override
	public RALExpr getResourceExpressionByProcessInstanceId(
			Object processInstanceId, String activityId) {
		return getResourceExpression(processInstanceId, activityId);
	}

	@Override
	public Bpmn20ModelHandler getBpmnModel(Object processId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
