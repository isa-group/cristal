package raci2bpmn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import raci.RaciActivity;
import raci.RaciMatrix;
import raci2bpmn.DiagramUpdater.ShapeUpdater;
import bpmn.BPMNShape;
import bpmn.TDefinitions;
import bpmn.TSubProcess;
import bpmn.TTask;

public class ModelHandler {

	private JAXBElement<?> bpmnModel;
	private JAXBContext jc;

	public ModelHandler() {
		// Obtenemos un contexto para jaxb seleccionando el paquete donde est‡n
		// las clases generadas por xjc
		try {
			jc = JAXBContext.newInstance("bpmn");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void transformProcess(RaciMatrix matrix) {
		// Obtenemos el elemento raiz a partir del JAXBElement
		TDefinitions definitions = (TDefinitions) bpmnModel.getValue();

		ProcessHandler handler = new ProcessHandler(definitions);
		DiagramUpdater diagramUpdater = new DiagramUpdater(definitions);
		CollaborationHandler collabHandler = new CollaborationHandler(definitions);
		//TaskToSubProcess conversor = new TaskToSubProcess();

		List<TTask> tasks = handler.getTasks();

		for (TTask task : tasks) {
			RaciActivity raciActivity = matrix.getActivityByName(task.getName());

			if (raciActivity != null) {
				RaciSubprocess conversor = new RaciSubprocess(task);
				conversor.buildSubprocess(raciActivity);
				
				TSubProcess subprocess = conversor.getSubprocess(); 

				handler.removeTask(task);
				handler.addSubprocess(subprocess);

				// Busca el BPMNShape que corresponde al flownode subprocess y
				// lo actualiza segœn indique ShapeUpdater.
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

	public void loadModel(String bpmnFile) {

		try {
			// Creamos un unmarshaller
			Unmarshaller u = jc.createUnmarshaller();

			// Leemos el XML obteniendo el elemento raiz como un JAXBElement
			bpmnModel = (JAXBElement<?>) u.unmarshal(new FileInputStream(
					bpmnFile));
		} catch (JAXBException je) {
			je.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	public void saveModel(String filename) {
		try {
			// Creamos un marshaller
			Marshaller m = jc.createMarshaller();

			// Guardamos el modelo en un XML
			m.marshal(bpmnModel, new FileOutputStream(filename));
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
