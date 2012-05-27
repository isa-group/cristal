package raci2bpmn;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import raci.BoundedRole;
import raci.RaciActivity;
import bpmn.ObjectFactory;
import bpmn.TEndEvent;
import bpmn.TExclusiveGateway;
import bpmn.TExtensionElements;
import bpmn.TFlowElement;
import bpmn.TFormalExpression;
import bpmn.TGatewayDirection;
import bpmn.TMessageFlow;
import bpmn.TParallelGateway;
import bpmn.TParticipant;
import bpmn.TPotentialOwner;
import bpmn.TResourceAssignmentExpression;
import bpmn.TResourceRole;
import bpmn.TSendTask;
import bpmn.TSequenceFlow;
import bpmn.TStartEvent;
import bpmn.TSubProcess;
import bpmn.TTask;
import bpmn.TUserTask;

public class RaciSubprocess {

	private TSubProcess subprocess;
	private ObjectFactory objectFactory;
	private TFlowElement lastElement;
	private List<TParticipant> participants;
	private List<TMessageFlow> messageFlows;
	
	public RaciSubprocess(TTask task) {
		participants = new ArrayList<TParticipant>();
		messageFlows = new ArrayList<TMessageFlow>();
		objectFactory = new ObjectFactory();
		subprocess = new TSubProcess();
		copyFromTask(task);
	}

	public List<TParticipant> getParticipants() {
		return participants;
	}

	public List<TMessageFlow> getMessageFlows() {
		return messageFlows;
	}

	private void copyFromTask(TTask task) {
		subprocess.setId(task.getId());
		subprocess.setName(task.getName());
		for (QName incoming: task.getIncoming()) {
			subprocess.getIncoming().add(incoming);
		}
		for (QName outgoing: task.getOutgoing()) {
			subprocess.getOutgoing().add(outgoing);
		}
		subprocess.setExtensionElements(task.getExtensionElements());
	}
	
	public TSubProcess getSubprocess() {
		return subprocess;
	}
	
	public void buildSubprocess(RaciActivity raci) {
		lastElement = addStartEvent();
		TExclusiveGateway accountableGateway = null;
		TTask responsible = null;
		
		
		if (raci.shouldIncludeAccountable()) {
			accountableGateway = addAccountableGateway();
			lastElement = accountableGateway;			
		}
		
		if (raci.shouldIncludeConsult() || raci.shouldIncludeSupport()) {
			lastElement = addOpenParallelGateway();
		}
		
		responsible = addResponsibleFragment(raci.getResponsible());

		if (raci.shouldIncludeConsult() || raci.shouldIncludeSupport()) {
			List<TFlowElement> support = null;
			List<TFlowElement> consult = null;

			if (raci.shouldIncludeSupport())
				support = addSupportFragment(raci.getSupport(), responsible);
			
			if (raci.shouldIncludeConsult()) 
				consult = addConsultFragment(raci.getConsulted(), responsible);
			
			TFlowElement closeParallel = addCloseParallelGateway(responsible, support, consult);
			lastElement = closeParallel;
		}
		else {
			lastElement = responsible;
		}
		
		if (raci.shouldIncludeAccountable()) {
			lastElement = addAcountableFragment(raci.getAccountable(), accountableGateway);
		}
		
		if (raci.shouldIncludeInformed()) {
			lastElement = addInformedFragment(raci.getInformed(), responsible);
		}
		
		addEndEvent();
		
	}
	

	
	private List<TFlowElement> addConsultFragment(List<BoundedRole> consulted,
			TTask responsible) {
		return addHelpFragment(consulted, responsible, "information");
	}

	private List<TFlowElement> addSupportFragment(List<BoundedRole> support,
			TTask responsible) {		
		return addHelpFragment(support, responsible, "support");
	}

	private List<TFlowElement> addHelpFragment(List<BoundedRole> supportRole, TTask responsibleTask, String helpType) {
		List<TFlowElement> result = new ArrayList<TFlowElement>();
		for (BoundedRole r: supportRole) {
			result.add(addHelpFragment(r, responsibleTask, helpType));
		}
		
		return result;
	}
	
	private TFlowElement addHelpFragment(BoundedRole role, TTask responsibleTask, String helpType) {
		TExclusiveGateway backGateway = new TExclusiveGateway();
		backGateway.setId(IdGenerator.createId());
		backGateway.setGatewayDirection(TGatewayDirection.CONVERGING);
		subprocess.getFlowElement().add(objectFactory.createExclusiveGateway(backGateway));
		
		TUserTask decideSupport = new TUserTask();
		decideSupport.setId(IdGenerator.createId());
		decideSupport.setName("Decide if "+helpType +" from "+role.getRole()+" is required for task " + subprocess.getName());
		addAssignmentExpression(decideSupport, "IS PERSON WHO DID "+responsibleTask.getName());
		subprocess.getFlowElement().add(objectFactory.createUserTask(decideSupport));
		
		TExclusiveGateway decisionGateway = new TExclusiveGateway();
		decisionGateway.setId(IdGenerator.createId());
		decisionGateway.setGatewayDirection(TGatewayDirection.DIVERGING);
		decisionGateway.setName("Is "+helpType+" required?");
		subprocess.getFlowElement().add(objectFactory.createExclusiveGateway(decisionGateway));
		
		TUserTask provideSupport = new TUserTask();
		provideSupport.setId(IdGenerator.createId());
		provideSupport.setName("Provide "+helpType +" for task "+subprocess.getName() + " by "+role.getRole());
		addAssignmentExpression(provideSupport, role);
		subprocess.getFlowElement().add(objectFactory.createUserTask(provideSupport));
		
		TUserTask assessSupport = new TUserTask();
		assessSupport.setId(IdGenerator.createId());
		assessSupport.setName("Assess "+helpType+" for task " + subprocess.getName() + " by " + role.getRole());
		addAssignmentExpression(assessSupport, "IS PERSON WHO DID "+responsibleTask.getName());
		subprocess.getFlowElement().add(objectFactory.createUserTask(assessSupport));
		
		addSequenceFlow(lastElement, backGateway);
		addSequenceFlow(backGateway, decideSupport);
		addSequenceFlow(decideSupport, decisionGateway);
		addSequenceFlow(decisionGateway, provideSupport);
		addSequenceFlow(provideSupport, assessSupport);
		addSequenceFlow(assessSupport, backGateway);
		
		return decisionGateway;
	}
	
	private TFlowElement addInformedFragment(List<BoundedRole> informedRole, TTask responsibleTask) {
		TFlowElement result;
		if (informedRole == null || informedRole.size() == 0)
			result = lastElement;
		
		else if (informedRole.size() == 1) {
			result = addInformedFragment(informedRole.get(0), responsibleTask);
			addSequenceFlow(lastElement, result);
		}
		else {
			TParallelGateway open = new TParallelGateway();
			open.setId(IdGenerator.createId());
			open.setGatewayDirection(TGatewayDirection.DIVERGING);
			subprocess.getFlowElement().add(objectFactory.createParallelGateway(open));
			
			addSequenceFlow(lastElement, open);
						
			TParallelGateway close = new TParallelGateway();
			close.setId(IdGenerator.createId());
			close.setGatewayDirection(TGatewayDirection.CONVERGING);			
			subprocess.getFlowElement().add(objectFactory.createParallelGateway(close));
			
			for (BoundedRole r: informedRole) {
				TFlowElement inform = addInformedFragment(r, responsibleTask);
				addSequenceFlow(open, inform);
				addSequenceFlow(inform,  close);
			}
			
			result = close;		

		}
		
		return result;
		
	}
	
	private TFlowElement addInformedFragment(BoundedRole informedRole, TTask responsibleTask) {
		TSendTask inform = new TSendTask();
		inform.setId(IdGenerator.createId());
		inform.setName("Inform role "+ informedRole.getRole() +" about task " + subprocess.getName());
		addAssignmentExpression(inform, "IS PERSON WHO DID "+responsibleTask.getName());
		subprocess.getFlowElement().add(objectFactory.createSendTask(inform));
		
		TParticipant participant = new TParticipant();
		participant.setId(IdGenerator.createId());
		participant.setName(informedRole.getAssignmentExpression());
		participants.add(participant);
		
		TMessageFlow flow = new TMessageFlow();
		
		flow.setSourceRef(new QName("http://www.signavio.com/bpmn20",inform.getId()));
		flow.setTargetRef(new QName("http://www.signavio.com/bpmn20",participant.getId()));
		messageFlows.add(flow);		
		
		return inform;
	}

	private TFlowElement addAcountableFragment(BoundedRole accountable,
			TExclusiveGateway accountableGateway) {
		TUserTask task = new TUserTask();
		task.setId(IdGenerator.createId());		
		task.setName("Approve task "+subprocess.getName());
		addAssignmentExpression(task, accountable);
				
		subprocess.getFlowElement().add(objectFactory.createUserTask(task));
		addSequenceFlow(lastElement, task);
		
		TExclusiveGateway gateway = new TExclusiveGateway();
		gateway.setId(IdGenerator.createId());
		gateway.setGatewayDirection(TGatewayDirection.DIVERGING);
		subprocess.getFlowElement().add(objectFactory.createExclusiveGateway(gateway));
		addSequenceFlow(task, gateway);
		addSequenceFlow(gateway, accountableGateway);
		
		return gateway;
	}

	private TTask addResponsibleFragment(BoundedRole responsible) {
		TTask task = new TTask();
		task.setId(IdGenerator.createId());		
		task.setName("Perform task "+subprocess.getName());
		
		addAssignmentExpression(task, responsible);
		
		subprocess.getFlowElement().add(objectFactory.createTask(task));
		addSequenceFlow(lastElement, task);
		
		return task;
	}

	private TResourceRole buildAssignmentExpression(String ralExpression) {
		TResourceRole resourceRole = new TPotentialOwner();
		TResourceAssignmentExpression resourceAssignment = new TResourceAssignmentExpression();
		TFormalExpression expression = new TFormalExpression();
		expression.setLanguage("RAL");
		expression.getContent().add(ralExpression);
		resourceAssignment.setExpression(objectFactory.createFormalExpression(expression));
		resourceRole.setResourceAssignmentExpression(resourceAssignment);
		return resourceRole;
	}
	
	private void addAssignmentExpression(TTask task, BoundedRole role) {
		TResourceRole resourceRole = buildAssignmentExpression(role.getAssignmentExpression());		
		task.getResourceRole().add(objectFactory.createPotentialOwner((TPotentialOwner)resourceRole));
	}
	
	private void addAssignmentExpression(TTask task, String ralExpression) {
		TResourceRole resourceRole = buildAssignmentExpression(ralExpression);		
		task.getResourceRole().add(objectFactory.createResourceRole(resourceRole));		
	}

	private TFlowElement addCloseParallelGateway(TFlowElement responsible,
			List<TFlowElement> support, List<TFlowElement> consult) {
		
		TParallelGateway pg = new TParallelGateway();
		pg.setId(IdGenerator.createId());
		pg.setGatewayDirection(TGatewayDirection.CONVERGING);
		subprocess.getFlowElement().add(objectFactory.createParallelGateway(pg));
		
		addSequenceFlow(responsible, pg);
		if (support != null) { 
			for (TFlowElement s: support)
				addSequenceFlow(s, pg);
		}
		if (consult != null) {
			for (TFlowElement c: consult) 
				addSequenceFlow(c, pg);			
		}
			
		return pg;
	}

	private TFlowElement addOpenParallelGateway() {
		TParallelGateway pg = new TParallelGateway();
		pg.setId(IdGenerator.createId());
		pg.setGatewayDirection(TGatewayDirection.DIVERGING);
		subprocess.getFlowElement().add(objectFactory.createParallelGateway(pg));
		
		addSequenceFlow(lastElement, pg);
		
		return pg;
	}

	private TExclusiveGateway addAccountableGateway() {
		TExclusiveGateway ag = new TExclusiveGateway();
		ag.setId(IdGenerator.createId());
		ag.setGatewayDirection(TGatewayDirection.CONVERGING);
		JAXBElement<TExclusiveGateway> start = objectFactory.createExclusiveGateway(ag);
		subprocess.getFlowElement().add(start);
		
		addSequenceFlow(lastElement, ag);
		
		return ag;
	}

	private void addSequenceFlow(TFlowElement origin, TFlowElement dest) {
		TSequenceFlow seq = new TSequenceFlow();
		seq.setSourceRef(origin);
		seq.setTargetRef(dest);
		subprocess.getFlowElement().add(objectFactory.createSequenceFlow(seq));
	}
	
	// A–adimos evento de inicio al subproceso
	private TStartEvent addStartEvent() {

		// creamos un diagrama
		//BPMNDiagram diagram = new BPMNDiagram();
		//JAXBElement<Diagram> subprocess = (new ObjectFactory()).createDiagram(diagram);
		
		// a–adimos un evento de inicio al diagrama
		TStartEvent se = new TStartEvent();
		se.setName("Sub-process Start");
		se.setId(IdGenerator.createId());
		se.setExtensionElements(new TExtensionElements ());
		JAXBElement<TStartEvent> start = objectFactory.createStartEvent(se);
		subprocess.getFlowElement().add(start);
		
		return se;
	}
	
	private void addEndEvent() {
		TEndEvent ee = new TEndEvent();
		ee.setId(IdGenerator.createId());
		subprocess.getFlowElement().add(objectFactory.createEndEvent(ee));
		
		addSequenceFlow(lastElement, ee);
		
	}




}
