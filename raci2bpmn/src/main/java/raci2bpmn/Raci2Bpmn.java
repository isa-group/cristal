package raci2bpmn;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDefinitions;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.bpmn.xmlClasses.bpmndi.BPMNShape;
import raci.RaciActivity;
import raci.RaciMatrix;
import raci2bpmn.DiagramUpdater.ShapeUpdater;

import java.util.List;

public class Raci2Bpmn {

	public void transformProcess(Bpmn20ModelHandler model, RaciMatrix matrix) {
		// Obtenemos el elemento raiz a partir del JAXBElement
		TDefinitions definitions = model.getDefinitions();

		ProcessHandler handler = new ProcessHandler(definitions);
		DiagramUpdater diagramUpdater = new DiagramUpdater(definitions);
		CollaborationHandler collabHandler = new CollaborationHandler(
				definitions);
		// TaskToSubProcess conversor = new TaskToSubProcess();

		List<TTask> tasks = handler.getTasks();

		for (TTask task : tasks) {
			RaciActivity raciActivity = matrix
					.getActivityByName(task.getName());

			System.out.println("ra: "+raciActivity);
			if (raciActivity != null) {
				RaciSubprocess conversor = new RaciSubprocess(task);
				conversor.buildSubprocess(raciActivity);

				TSubProcess subprocess = conversor.getSubprocess();

				handler.removeTask(task);
				handler.addSubprocess(subprocess);

				// Busca el BPMNShape que corresponde al flownode subprocess y
				// lo actualiza segun indique ShapeUpdater.
				diagramUpdater.updateShape(subprocess, new ShapeUpdater() {
					@Override
					public void update(BPMNShape shape) {
						shape.setIsExpanded(false);
					}
				});

				collabHandler.addParticipants(conversor.getParticipants());
				collabHandler.addMessageFlows(conversor.getMessageFlows());
			}
		}
	}

}
