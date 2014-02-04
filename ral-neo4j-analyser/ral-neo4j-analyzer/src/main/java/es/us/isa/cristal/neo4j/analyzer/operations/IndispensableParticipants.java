package es.us.isa.cristal.neo4j.analyzer.operations;

import java.util.Set;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;

public class IndispensableParticipants extends AbstractOP<Set<String>> implements Operation<Set<String>> {

	private Iterable<String> activities;
	
	public IndispensableParticipants(BPEngine bpEngine,
			Neo4JRalResolver resolver, Object processId, Iterable<String> activities, TaskDuty dt) {
		super(bpEngine, resolver, processId, dt);
		this.activities = activities;
	}

	@Override
	public Set<String> execute() {
		
		Operation<Set<String>> ma = new MandatoryActivitiesOP(bpEngine, resolver, processId, activities, duty);
		Set<String> mandatoryActivities = ma.execute();
		
		Operation<Set<String>> cp = new CriticalParticipantsOP(bpEngine, resolver, processId, mandatoryActivities, duty);
		Set<String> indispesableParticipants = cp.execute();
		
		return indispesableParticipants;
		
	}
	
}
