package es.us.isa.cristal.ram2bpmn;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDefinitions;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.bpmn.xmlClasses.bpmndi.BPMNShape;
import es.us.isa.cristal.ram.Activity;
import es.us.isa.cristal.ram.RAM;
import es.us.isa.cristal.ram2bpmn.processes.CollaborationHandler;
import es.us.isa.cristal.ram2bpmn.processes.DiagramUpdater;
import es.us.isa.cristal.ram2bpmn.processes.ProcessHandler;
import es.us.isa.cristal.ram2bpmn.templates.NonCompatibleTemplate;
import es.us.isa.cristal.ram2bpmn.templates.Template;
import es.us.isa.cristal.ram2bpmn.templates.TemplateAssignment;
import es.us.isa.cristal.ram2bpmn.templates.TemplateInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Ram2Bpmn {

    static final Logger LOG = LoggerFactory.getLogger(Ram2Bpmn.class);

	public void transformProcess(RAM matrix, Bpmn20ModelHandler model, TemplateAssignment templ) {

		TDefinitions definitions = model.getDefinitions();

		ProcessHandler handler = new ProcessHandler(definitions);
		DiagramUpdater diagramUpdater = new DiagramUpdater(definitions);
		CollaborationHandler collabHandler = new CollaborationHandler(definitions);

		List<TTask> tasks = handler.getTasks();

		for (TTask task : tasks) {
			LOG.debug("processing activity: {}", task.getName());
            Activity activity = Activity.fromBpmn(task);

            if (matrix.anyTD(activity)) {
                Template template = templ.getTemplate(activity);

                if (template != null) {
                    try {
                        TemplateInstance instance = template.instantiate(task, matrix);

                        TSubProcess subprocess = instance.getSubprocess();

                        handler.removeTask(task);
                        handler.addSubprocess(subprocess);

                        diagramUpdater.updateShape(subprocess, new DiagramUpdater.ShapeUpdater() {
                            @Override
                            public void update(BPMNShape shape) {
                                shape.setIsExpanded(false);
                            }
                        });

                        collabHandler.addParticipants(instance.getParticipants());
                        collabHandler.addMessageFlows(instance.getMessageFlows());
                    } catch (NonCompatibleTemplate exception) {
                        LOG.warn("Template not compatible for activity", exception);
                    }

                }
            }
        }
	}

}
