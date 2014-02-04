package es.us.isa.cristal.neo4j.analyzer.operations;

import java.util.HashSet;
import java.util.Set;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSequenceFlow;
import es.us.isa.bpmn.xmlClasses.bpmn20.TStartEvent;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;

public class MandatoryActivitiesOP extends AbstractOP<Set<String>> implements Operation<Set<String>> {

	private Iterable<String> activities;
	
	public MandatoryActivitiesOP(BPEngine bpEngine, Neo4JRalResolver resolver,
			Object processId, Iterable<String> activities, TaskDuty dt) {
		super(bpEngine, resolver, processId, dt);
		this.activities = activities;
	}

	@Override
	public Set<String> execute() {
		Set<String> activities = new HashSet<String>();
		Bpmn20ModelHandler bpmn = bpEngine.getBpmnModel(this.processId);
		
		int numberOfStarts = bpmn.getStartEventMap().size();
		for(String startId: bpmn.getStartEventMap().keySet()){
			bpmn.getStartEventMap().get(startId);
		}
		
		TStartEvent ts;
		ts.getOutputSet().;
		TSequenceFlow flow = bpmn.getSequenceFlowMap().get("");
		flow.getTargetRef()
		
		for(TTask t: bpmn.getTaskMap().values()){
			Operation<Set<String>> op = new PotentialParticipantsOP(bpEngine,resolver,processId,t.getName(),duty);
			if(op.execute().contains(personName)){
				activities.add(t.getName());
			}
		}
		return activities;
	}

}
