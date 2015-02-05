package es.us.isa.cristal.ram2bpmn;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDefinitions;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.bpmn.xmlClasses.bpmndi.BPMNShape;
import es.us.isa.cristal.ram.RAM;
import es.us.isa.cristal.ram.RamActivity;
import es.us.isa.cristal.ram2bpmn.templates.NonCompatibleTemplate;
import es.us.isa.cristal.ram2bpmn.templates.ProcessTemplates;
import es.us.isa.cristal.ram2bpmn.templates.Template;
import es.us.isa.cristal.ram2bpmn.templates.TemplateInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import es.us.isa.cristal.ram2bpmn.DiagramUpdater.ShapeUpdater;

import java.util.List;

public class Ram2Bpmn {

    static final Logger LOG = LoggerFactory.getLogger(Ram2Bpmn.class);

	public void transformProcess(Bpmn20ModelHandler model, RAM matrix, ProcessTemplates templates) {
		// Obtenemos el elemento raiz a partir del JAXBElement
		TDefinitions definitions = model.getDefinitions();

		ProcessHandler handler = new ProcessHandler(definitions);
		DiagramUpdater diagramUpdater = new DiagramUpdater(definitions);
		CollaborationHandler collabHandler = new CollaborationHandler(definitions);

		List<TTask> tasks = handler.getTasks();

		for (TTask task : tasks) {
			RamActivity ramActivity = matrix.getActivityByName(task.getName());

			LOG.debug("processing activity: {}", ramActivity);

			if (ramActivity != null) {
                Template template = templates.getTemplate(task);

                try {
                    TemplateInstance instance = template.instantiate(task, ramActivity);

                    TSubProcess subprocess = instance.getSubprocess();

                    handler.removeTask(task);
                    handler.addSubprocess(subprocess);

                    diagramUpdater.updateShape(subprocess, new ShapeUpdater() {
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
