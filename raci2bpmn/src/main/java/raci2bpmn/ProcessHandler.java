package raci2bpmn;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import bpmn.ObjectFactory;
import bpmn.TDefinitions;
import bpmn.TFlowElement;
import bpmn.TProcess;
import bpmn.TRootElement;
import bpmn.TSubProcess;
import bpmn.TTask;
import bpmn.TUserTask;

public class ProcessHandler {
	
	private TProcess process;

	public ProcessHandler (TDefinitions definitions) {
		List<JAXBElement<? extends TRootElement>> rootElements = definitions.getRootElement();
		
		for (JAXBElement<? extends TRootElement> elem: rootElements) {
			if (elem.getDeclaredType() == bpmn.TProcess.class)
				process = (TProcess) elem.getValue();
		}
	}
	

	
	public void addSubprocess(TSubProcess subprocess) {
		JAXBElement<TSubProcess> elem = (new ObjectFactory()).createSubProcess(subprocess);
		process.getFlowElement().add(elem);
		
	}

	public void removeTask(TTask task) {
		List<JAXBElement<? extends TFlowElement>> flowElements = process.getFlowElement();
		JAXBElement<? extends TFlowElement> toRemove = null;
		for (JAXBElement<? extends TFlowElement> elem: flowElements) {
			if (elem.getValue().getId().equals (task.getId())) {
				toRemove = elem;
				break;
			}
		}
		
		if (toRemove != null) 
			process.getFlowElement().remove(toRemove);		
	}

	public List<TTask> getTasks() {
		List<TTask> result = new ArrayList<TTask>();
		List<JAXBElement<? extends TFlowElement>> flowElements = process.getFlowElement();
		for (JAXBElement<? extends TFlowElement> elem: flowElements) {
			System.out.println(elem.getDeclaredType().getName());
			TFlowElement flowElement = elem.getValue();
			if (flowElement instanceof TTask) {
				result.add((TTask) flowElement);
			}
		}
		return result;
	}

	public List<TUserTask> getUserTasks() {
		List<TUserTask> result = new ArrayList<TUserTask>();
		List<JAXBElement<? extends TFlowElement>> flowElements = process.getFlowElement();
		for (JAXBElement<? extends TFlowElement> elem: flowElements) {
			System.out.println(elem.getDeclaredType().getName());
			TFlowElement flowElement = elem.getValue();
			if (flowElement instanceof TUserTask) {
				result.add((TUserTask) flowElement);
			}
		}
		return result;
	}
}
