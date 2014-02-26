package es.us.isa.cristal.neo4j.analyzer.operations;

import java.util.HashSet;
import java.util.Set;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;

/**
 * 
 * @author Manuel Leon
 *
 */
public class PotentialActivitiesOP extends AbstractOP<Set<String>> implements Operation<Set<String>> {
	
	private String personName;
	
	public PotentialActivitiesOP(BPEngine bpEngine, Neo4JRalResolver resolver,
			Object processId, String personName, TaskDuty dt) {
		super(bpEngine, resolver, processId, dt);
		this.personName = personName;
	}

	public Set<String> execute() {
		Set<String> activities = new HashSet<String>();
		Bpmn20ModelHandler bpmn = bpEngine.getBpmnModel(this.processId);
		for(TTask t: bpmn.getTaskMap().values()){
			Operation<Set<String>> op = new PotentialParticipantsOP(bpEngine,resolver,processId,t.getName(),duty);
			if(op.execute().contains(personName)){
				activities.add(t.getName());
			}
		}
		return activities;
	}

	
	

}
