package raci2bpmn;

import javax.xml.namespace.QName;

import bpmn.TSubProcess;
import bpmn.TTask;

public class TaskToSubProcess {

	public TSubProcess createSubProcess(TTask task) {
		//TODO No est‡ completo. Falta por ejemplo lo relacionado con datos
		
		TSubProcess result = new TSubProcess();
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
}
