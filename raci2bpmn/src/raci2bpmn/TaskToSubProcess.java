package raci2bpmn;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import bpmn.BPMNDiagram;
import bpmn.BPMNShape;
import bpmn.Diagram;
import bpmn.DiagramElement;
import bpmn.ObjectFactory;
import bpmn.TDefinitions;
import bpmn.TExtensionElements;
import bpmn.TStartEvent;
import bpmn.TSubProcess;
import bpmn.TTask;

public class TaskToSubProcess {

	TSubProcess result;
	
	public TSubProcess getResult() {
		return result;
	}

	public void setResult(TSubProcess result) {
		this.result = result;
	}


	public TSubProcess createSubProcess(TTask task) {
		//TODO No est‡ completo. Falta por ejemplo lo relacionado con datos
		
		result = new TSubProcess();
		result.setId(task.getId());
		result.setName(task.getName());
		for (QName incoming: task.getIncoming()) {
			result.getIncoming().add(incoming);
		}
		for (QName outgoing: task.getOutgoing()) {
			result.getOutgoing().add(outgoing);
		}
		result.setExtensionElements(task.getExtensionElements());
		
		return result;
	}
	
	
	
	// A–adimos evento de inicio al subproceso
	public TSubProcess insertRASCI(DiagramUpdater diagramUpdater) {

		// creamos un diagrama
		//BPMNDiagram diagram = new BPMNDiagram();
		//JAXBElement<Diagram> subprocess = (new ObjectFactory()).createDiagram(diagram);
		
		// a–adimos un evento de inicio al diagrama
		TStartEvent se = new TStartEvent();
		se.setName("Sub-process Start");
		se.setId("id1");
		se.setExtensionElements(new TExtensionElements ());
		JAXBElement<TStartEvent> start = (new ObjectFactory().createStartEvent(se));
		result.getFlowElement().add(start);
		
		BPMNShape s = new BPMNShape();
		s.setBpmnElement(new QName(se.getId()));
		diagramUpdater.addShape(s);
		
		return result;
	}
}
