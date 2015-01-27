package raci2bpmn;


import es.us.isa.bpmn.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import raci.RaciActivity;

public class TaskToSubProcess {

	public TSubProcess transformTask(TTask task, RaciActivity raci) {
		RaciSubprocess subprocess = new RaciSubprocess(task);
		
		subprocess.buildSubprocess(raci);
		
		return subprocess.getSubprocess();
	}
	

	
	
}
