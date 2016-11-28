package es.us.isa.cristal.ram2bpmn.processes;

import es.us.isa.bpmn.xmlClasses.bpmn20.*;
import es.us.isa.cristal.ram2bpmn.templates.IdGenerator;

import javax.xml.bind.JAXBElement;
import java.util.List;

public class CollaborationHandler {

	private TCollaboration collaboration;
	private ObjectFactory objectFactory;

	public CollaborationHandler(TDefinitions definitions) {
		collaboration = null;
		objectFactory = new ObjectFactory();
		
		List<JAXBElement<? extends TRootElement>> rootElements = definitions.getRootElement();
		
		for (JAXBElement<? extends TRootElement> elem: rootElements) {
			if (elem.getDeclaredType() == TCollaboration.class)
				collaboration = (TCollaboration) elem.getValue();
		}
		
		if (collaboration == null) {
			collaboration = new TCollaboration();
			collaboration.setId(IdGenerator.random());
			definitions.getRootElement().add(objectFactory.createCollaboration(collaboration));
		}
	}

	public void addParticipants(List<TParticipant> participants) {
		for (TParticipant p : participants) {
			collaboration.getParticipant().add(p);
		}
		
	}

	public void addMessageFlows(List<TMessageFlow> messageFlows) {
		for (TMessageFlow m: messageFlows) {
			collaboration.getMessageFlow().add(m);
		}
	}
	
	

}
