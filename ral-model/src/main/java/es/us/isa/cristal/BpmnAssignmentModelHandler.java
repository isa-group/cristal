package es.us.isa.cristal;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.xmlClasses.bpmn20.*;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.parser.RALParser;

import javax.xml.bind.JAXBElement;
import java.io.Serializable;
import java.util.List;

/**
 * BpmnAssignmentModelHandler
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class BpmnAssignmentModelHandler {
    private Bpmn20ModelHandler bpmn;


    public BpmnAssignmentModelHandler(Bpmn20ModelHandler handler) {
        this.bpmn = handler;
    }

    public RALExpr getPotentialOwnerRalExpr(String activityId) {
        RALExpr expr = null;

        TTask t = bpmn.getTaskMap().get(activityId);
        if (t != null) {
            expr = getPotentialOwnerRalExpr(t);
        }

        return expr;
    }

    public RALExpr getPotentialOwnerRalExpr(TTask t) {
        RALExpr ralExpr = null;
        String expr = getPotentialOwnerRawExpr(t);

        if (expr != null) {
            ralExpr = RALParser.parse(expr);
        }

        return ralExpr;
    }

    public String getPotentialOwnerRawExpr(TTask t) {
        String expr = null;
        if (t.getResourceRole() != null && !t.getResourceRole().isEmpty()) {
            for (JAXBElement<? extends TResourceRole> el : t.getResourceRole()) {
                if (el.getName().getLocalPart().equalsIgnoreCase("potentialOwner")) {
                    TPotentialOwner owner = (TPotentialOwner) el.getValue();
                    List<Serializable> content = owner.getResourceAssignmentExpression().getExpression().getValue().getContent();
                    if (!content.isEmpty()) {
                        expr = content.get(0).toString();
                        break;
                    }

                }
            }
        }

        return expr;
    }

    public void updatePotentialOwners(RawResourceAssignment activityAssignmentMap) {
        for(TTask task: bpmn.getTaskMap().values()) {
            String assignment = activityAssignmentMap.get(task.getId(), TaskDuty.RESPONSIBLE);
            if (assignment != null) {
                updatePotentialOwner(task, assignment);
            }
        }
    }

    private void updatePotentialOwner(TTask task, String assignment) {
        ObjectFactory objectFactory = new ObjectFactory();

        TPotentialOwner resourceRole = objectFactory.createTPotentialOwner();
        TResourceAssignmentExpression rae = objectFactory.createTResourceAssignmentExpression();
        TFormalExpression exp = objectFactory.createTFormalExpression();

        exp.setLanguage("RAL");
        //QName assignQname = new QName(assignment);
        //exp.setEvaluatesToTypeRef(assignQname);
        exp.getContent().add(assignment);
//        QName formalExpQname = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "formalExpression", "");
        JAXBElement<? extends TExpression> expEl = objectFactory.createFormalExpression(exp);
        rae.setExpression(expEl);
        resourceRole.setResourceAssignmentExpression(rae);
//        QName roleQname = new QName("http://www.omg.org/spec/BPMN/20100524/MODEL", "potentialOwner", "");
//        JAXBElement<? extends TResourceRole> res = new JAXBElement<TPotentialOwner>(roleQname, TPotentialOwner.class, resourceRole);
        JAXBElement<? extends TResourceRole> res = objectFactory.createPotentialOwner(resourceRole);
        task.getResourceRole().add(res);
    }


}
