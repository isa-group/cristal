package es.us.isa.cristal.neo4j.analyzer.operations;

import java.util.Set;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;

public class PotentialActivitiesOP extends AbstractOP<Set<String>> implements Operation<Set<String>> {
	
	private String personName;
	
	public PotentialActivitiesOP(BPEngine bpEngine, Neo4JRalResolver resolver,
			Object processId, String personName, TaskDuty dt) {
		super(bpEngine, resolver, processId, dt);
		this.personName = personName;
	}

	public Set<String> execute() {
		//TODO: hacer!!
		return null;
	}

	
	

}
