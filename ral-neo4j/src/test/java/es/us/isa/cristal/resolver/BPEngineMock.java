package es.us.isa.cristal.resolver;

import java.util.LinkedList;
import java.util.List;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.parser.RALParser;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 20:22
 */
public class BPEngineMock implements BPEngine {
    @Override
    public String getDataValue(Object pid, String data, String property) {
        return "Anna";
    }

    @Override
    public List<String> getActivityPerformer(Object pid, String activityName) {
        List<String> result = new LinkedList<String>();
    	result.add("Charles");
        return result;
    }

    

	@Override
	public RALExpr getResourceExpression(Object processDefinitionId,
			String activityId) {
		return RALParser.parse("HAS ROLE Account Administrator");
	}

	@Override
	public RALExpr getResourceExpressionByProcessDefinitionId(
			Object processDefinitionId, String activityId) {
		return RALParser.parse("HAS ROLE Account Administrator");
	}

	@Override
	public RALExpr getResourceExpressionByProcessInstanceId(
			Object processInstanceId, String activityId) {
		return RALParser.parse("HAS ROLE Account Administrator");
	}

	@Override
	public Bpmn20ModelHandler getBpmnModel(Object processId) {
		return null;
	}
}
