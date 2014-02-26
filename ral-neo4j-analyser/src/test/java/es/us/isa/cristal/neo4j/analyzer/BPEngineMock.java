package es.us.isa.cristal.neo4j.analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.Bpmn20ModelHandlerImpl;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.parser.RALParser;

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
		return RALParser.parse("HAS ROLE Analyst");
	}

	@Override
	public RALExpr getResourceExpressionByProcessDefinitionId(
			Object processDefinitionId, String activityId) {
		return RALParser.parse("HAS ROLE Analyst");
	}

	@Override
	public RALExpr getResourceExpressionByProcessInstanceId(
			Object processInstanceId, String activityId) {
		return RALParser.parse("HAS ROLE Analyst");
	}

	@Override
	public Bpmn20ModelHandler getBpmnModel(Object processId){
		Bpmn20ModelHandler res  = new Bpmn20ModelHandlerImpl();
		try {
			URI uri = this.getClass().getResource("/bpmntest/" + processId.toString()).toURI();
			
			InputStream is = new FileInputStream(new File(uri));
			res.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}

}
