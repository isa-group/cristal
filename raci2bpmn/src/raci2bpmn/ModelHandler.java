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

	public void handleProcess() {
		// Obtenemos el elemento raiz a partir del JAXBElement
		TDefinitions definitions = (TDefinitions) bpmnModel.getValue();

		ProcessHandler handler = new ProcessHandler(definitions);
		DiagramUpdater diagramUpdater = new DiagramUpdater(definitions);
		TaskToSubProcess conversor = new TaskToSubProcess();

		List<TTask> tasks = handler.getTasks();

		for (TTask task : tasks) {
			TSubProcess subprocess = conversor.createSubProcess(task);
			handler.removeTask(task);
			handler.addSubprocess(subprocess);
			
			// Busca el BPMNShape que corresponde al flownode subprocess y lo
			// actualiza segœn indique ShapeUpdater.
			diagramUpdater.updateShape(subprocess, new ShapeUpdater() {				
				@Override
				public void update(BPMNShape shape) {
					shape.setIsExpanded(false);					
				}
			});
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
