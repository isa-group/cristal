package es.us.isa.cristal.neo4j.analyzer.util;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.JAXBElement;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.Bpmn20ModelHandlerImpl;
import es.us.isa.bpmn.xmlClasses.bpmn20.TPotentialOwner;
import es.us.isa.bpmn.xmlClasses.bpmn20.TResourceRole;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.parser.RALParser;

/**
 * 
 * @author Manuel Leon
 *
 */
public class DesignTimeAnalyserBPEngine implements BPEngine {

	Bpmn20ModelHandler bpmn;
	String expression;

	public DesignTimeAnalyserBPEngine(String bpmn, String ralExp) throws Exception {
		super();
		this.bpmn = new Bpmn20ModelHandlerImpl();
		this.bpmn.load(new ByteArrayInputStream(bpmn.getBytes()));
		this.expression = ralExp;
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
		RALExpr expr = null;
		if (this.expression != null) {
			expr = RALParser.parse(expression);
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
		return expr;
	}

	public RALExpr getResourceExpressionByProcessInstanceId(Object processInstanceId, String activityId) {
		throw new UnsupportedOperationException("Operation not supported at Desing Time.");
	}

	public Bpmn20ModelHandler getBpmnModel(Object processId) {
		return bpmn;
	}

}
