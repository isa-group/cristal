package es.us.isa.cristal.neo4j.analyzer.operations;

import java.util.Set;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;

public class BasicConsistencyOP extends AbstractOP<Boolean> implements Operation<Boolean>{

	private String activityName;
	
	public BasicConsistencyOP(BPEngine bpEngine, Neo4JRalResolver resolver, Object processId, String activityName, TaskDuty duty) {
		super(bpEngine, resolver, processId, duty);
		this.activityName = activityName;
	}

	public Boolean execute() {
		Operation<Set<String>> op = new PotentialParticipantsOP(bpEngine, resolver, processId, activityName, duty);
		return !op.execute().isEmpty();
	}

}
