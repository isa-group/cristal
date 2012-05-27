package raci2bpmn;

import raci.RaciActivity;
import bpmn.TSubProcess;
import bpmn.TTask;

public class TaskToSubProcess {

	public TSubProcess transformTask(TTask task, RaciActivity raci) {
		RaciSubprocess subprocess = new RaciSubprocess(task);
		
		subprocess.buildSubprocess(raci);
		
		return subprocess.getSubprocess();
	}
	

	
	
}
