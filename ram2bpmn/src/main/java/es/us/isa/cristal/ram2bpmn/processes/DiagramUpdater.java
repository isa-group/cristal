package es.us.isa.cristal.ram2bpmn.processes;

import es.us.isa.bpmn.xmlClasses.bpmn20.TDefinitions;
import es.us.isa.bpmn.xmlClasses.bpmn20.TFlowNode;
import es.us.isa.bpmn.xmlClasses.bpmndi.BPMNDiagram;
import es.us.isa.bpmn.xmlClasses.bpmndi.BPMNPlane;
import es.us.isa.bpmn.xmlClasses.bpmndi.BPMNShape;
import es.us.isa.bpmn.xmlClasses.bpmndi.ObjectFactory;
import es.us.isa.bpmn.xmlClasses.omgdi.DiagramElement;

import javax.xml.bind.JAXBElement;
import java.util.List;


public class DiagramUpdater {

	public interface ShapeUpdater {
		public void update(BPMNShape shape);
	}
	
	
	List<BPMNDiagram> diagrams;
	
	
	public DiagramUpdater(TDefinitions definitions) {
		diagrams = definitions.getBPMNDiagram();
	}

    /**
     * Finds the BPMNShape that corresponds to bpmnFlowNode and updates it according to updater
     *
     * @param bpmnFlowNode
     * @param updater
     */
	public void updateShape(TFlowNode bpmnFlowNode, ShapeUpdater updater) {
		for (BPMNDiagram diagram : diagrams) {
			updatePlane(diagram.getBPMNPlane(), bpmnFlowNode, updater);
		}
		
	}
	
	public void addShape(BPMNShape newElem) {
		for (BPMNDiagram diagram : diagrams) {
			addShape(diagram.getBPMNPlane(), newElem);
		}
		
	}

	private void updatePlane(BPMNPlane plane, TFlowNode flowNode, ShapeUpdater updater) {
		List<JAXBElement<? extends DiagramElement>> diagramElements = plane
				.getDiagramElement();
		for (JAXBElement<? extends DiagramElement> elem : diagramElements) {
			DiagramElement de = elem.getValue();
			if (de instanceof BPMNPlane) {
				updatePlane((BPMNPlane) de, flowNode, updater);
			} else if (de instanceof BPMNShape) {
				updateShape((BPMNShape) de, flowNode, updater);
			}
		}
	}

	private void updateShape(BPMNShape shape, TFlowNode subprocess, ShapeUpdater updater) {
		if (shape.getBpmnElement().getLocalPart().equals(subprocess.getId())) {
			updater.update(shape);
		}

	}
	
	private void addShape(BPMNPlane plane, BPMNShape newElem) {
		List<JAXBElement<? extends DiagramElement>> diagramElements = plane.getDiagramElement();
		diagramElements.add(new ObjectFactory().createBPMNShape(newElem));
	}

}
