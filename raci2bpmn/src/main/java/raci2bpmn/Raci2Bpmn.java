package raci2bpmn;

import java.util.List;

import raci.RaciActivity;
import raci.RaciMatrix;
import raci2bpmn.DiagramUpdater.ShapeUpdater;
import bpmn.BPMNShape;
import bpmn.TDefinitions;
import bpmn.TSubProcess;
import bpmn.TTask;

public class Raci2Bpmn {

	public void transformProcess(ModelHandler model, RaciMatrix matrix) {
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
