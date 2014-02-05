package es.us.isa.cristal.neo4j.analyzer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.Bpmn20ModelHandlerImpl;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;

public class BPEngineMock implements BPEngine{

	@Override
	public Object getDataValue(Object pid, String dataObjectName,
			String fieldName) {
		return "";
	}

	@Override
	public List<String> getActivityPerformer(Object pid, String activityId) {
		return Arrays.asList("");
	}

	@Override
	public RALExpr getResourceExpression(Object processId, String activityId) {
		return null;
	}

	@Override
	public RALExpr getResourceExpressionByProcessDefinitionId(
			Object processDefinitionId, String activityId) {
		return null;
	}

	@Override
	public RALExpr getResourceExpressionByProcessInstanceId(
			Object processInstanceId, String activityId) {
		return null;
	}

	@Override
	public Bpmn20ModelHandler getBpmnModel(Object processId){
		Bpmn20ModelHandler res  = new Bpmn20ModelHandlerImpl();
		try {
			InputStream is = new FileInputStream("test.bpmn20.xml");
			res.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}

}
