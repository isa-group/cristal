package es.us.isa.cristal.team;

import java.util.Arrays;
import java.util.List;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.parser.RALParser;

/**
 * BPEngineMock
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class BPEngineMock implements BPEngine {
    @Override
    public String getDataValue(Object pid, String data, String property) {
        return "Anna";
    }

    @Override
    public List<String> getActivityPerformer(Object pid, String activityName) {
        return Arrays.asList("Charles");
    }

   

	@Override
	public RALExpr getResourceExpression(Object processId, String activityId) {
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
