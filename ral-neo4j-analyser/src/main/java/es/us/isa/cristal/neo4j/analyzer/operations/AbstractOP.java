package es.us.isa.cristal.neo4j.analyzer.operations;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;

/**
 * 
 * @author Manuel Leon
 *
 */
public abstract class AbstractOP<T> implements Operation<T>{
	
	protected BPEngine bpEngine;
	
	protected Neo4JRalResolver resolver;
	
	protected Object processId;

	protected TaskDuty duty;
	
	public AbstractOP(BPEngine bpEngine, Neo4JRalResolver resolver,
			Object processId, TaskDuty dt) {
		super();
		this.bpEngine = bpEngine;
		this.resolver = resolver;
		this.processId = processId;
		this.duty = dt;
	}

	

	
	
}
