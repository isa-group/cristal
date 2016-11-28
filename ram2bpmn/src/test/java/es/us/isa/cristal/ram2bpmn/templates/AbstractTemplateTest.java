package es.us.isa.cristal.ram2bpmn.templates;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.Bpmn20ModelHandlerImpl;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.bpmn.xmlClasses.bpmndi.BPMNShape;
import es.us.isa.cristal.ram.Activity;
import es.us.isa.cristal.ram.BoundedRole;
import es.us.isa.cristal.ram.Responsibility;
import es.us.isa.cristal.ram.Role;
import es.us.isa.cristal.ram2bpmn.processes.DiagramUpdater;
import es.us.isa.cristal.ram2bpmn.processes.ProcessHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * AbstractTemplateTest
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class AbstractTemplateTest {
    protected String loadTemplateFile(String name) {
        return new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(name)))
                    .lines().collect(Collectors.joining("\n"));
    }

    protected TTask getTargetTask(Bpmn20ModelHandler handler) {
        return handler.getTaskMap().get("sid-6102E183-669A-424F-B098-0AF0FA5F4666");
    }

    protected Bpmn20ModelHandler loadSourceModel() throws Exception {
        Bpmn20ModelHandler handler = new Bpmn20ModelHandlerImpl();
        handler.load(getClass().getResourceAsStream("/initialBP.bpmn"));
        return handler;
    }

    protected BoundedRole boundedRole(String activity, String role, Responsibility responsibility) {
        return new BoundedRole(new Activity(activity), responsibility, new Role(role));
    }

    protected void updateHandler(Bpmn20ModelHandler handler, TTask sourceTask, TemplateInstance instance) {
        TSubProcess subprocess = instance.getSubprocess();
        ProcessHandler processHandler = new ProcessHandler(handler.getDefinitions());
        DiagramUpdater diagramUpdater = new DiagramUpdater(handler.getDefinitions());

        processHandler.removeTask(sourceTask);
        processHandler.addSubprocess(subprocess);

        diagramUpdater.updateShape(subprocess, new DiagramUpdater.ShapeUpdater() {
            @Override
            public void update(BPMNShape shape) {
                shape.setIsExpanded(false);
            }
        });
    }
}
