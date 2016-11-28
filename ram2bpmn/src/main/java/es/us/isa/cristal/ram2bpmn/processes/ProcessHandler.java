package es.us.isa.cristal.ram2bpmn.processes;

import es.us.isa.bpmn.xmlClasses.bpmn20.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

public class ProcessHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessHandler.class);

    private TProcess process;
    private List<TParticipant> participants = new ArrayList<>();
    private List<TMessageFlow> messageFlows = new ArrayList<>();

	public ProcessHandler (TDefinitions definitions) {
		List<JAXBElement<? extends TRootElement>> rootElements = definitions.getRootElement();
		
		for (JAXBElement<? extends TRootElement> elem: rootElements) {
			if (elem.getDeclaredType() == TProcess.class) {
                if (process == null) {
                    process = (TProcess) elem.getValue();
                } else {
                    LOG.warn("Process {} not handled: Only one process per diagram is allowed", elem.getName());
                }
            } else if (elem.getDeclaredType() == TCollaboration.class) {
                TCollaboration collaboration = (TCollaboration) elem.getValue();
                participants.addAll(collaboration.getParticipant());
                messageFlows.addAll(collaboration.getMessageFlow());
            }
		}


	}


    public TProcess getProcess() {
        return process;
    }

    public List<TParticipant> getParticipants() {
        return participants;
    }

    public List<TMessageFlow> getMessageFlows() {
        return messageFlows;
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
			//System.out.println(elem.getDeclaredType().getName());
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
			//System.out.println(elem.getDeclaredType().getName());
			TFlowElement flowElement = elem.getValue();
			if (flowElement instanceof TUserTask) {
				result.add((TUserTask) flowElement);
			}
		}
		return result;
	}
}
