package es.us.isa.cristal.neo4j.analyzer;

import java.util.Set;

import org.neo4j.cypher.ExecutionEngine;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;
import es.us.isa.cristal.neo4j.analyzer.operations.BasicConsistencyOP;
import es.us.isa.cristal.neo4j.analyzer.operations.CriticalActivitiesOP;
import es.us.isa.cristal.neo4j.analyzer.operations.CriticalParticipantsOP;
import es.us.isa.cristal.neo4j.analyzer.operations.NonParticipantsOP;
import es.us.isa.cristal.neo4j.analyzer.operations.Operation;
import es.us.isa.cristal.neo4j.analyzer.operations.PermanentParticipantsOP;
import es.us.isa.cristal.neo4j.analyzer.operations.PotentialActivitiesOP;
import es.us.isa.cristal.neo4j.analyzer.operations.PotentialParticipantsOP;

/**
 * 
 * @author Manuel Leon
 *
 */
public class Neo4jRALAnalyser implements RALAnalyser {
	
	private ExecutionEngine executionEngine;
	
	private BPEngine bpEngine;
	
	private Neo4JRalResolver resolver;
	
	private Object processInstanceId;
	
	public Neo4jRALAnalyser(ExecutionEngine executionEngine, BPEngine bpEngine, Object pid) {
		super();
		this.executionEngine = executionEngine;
		this.bpEngine = bpEngine;
		this.processInstanceId = pid;
		resolver = new Neo4JRalResolver(this.bpEngine, this.executionEngine);
		
	}

	public Set<String> potentialParticipants(String activityName, TaskDuty duty) {
		Operation<Set<String>> op = new PotentialParticipantsOP(bpEngine, resolver, processInstanceId, activityName, duty);
		return op.execute();
	}

	public Set<String> potentialActivities(String personName, TaskDuty duty) {
		Operation<Set<String>> op = new PotentialActivitiesOP(bpEngine, resolver, processInstanceId, personName, duty);
		return op.execute();
	}

	public boolean basicConsistency(String activity, TaskDuty duty) {
		Operation<Boolean> op = new BasicConsistencyOP(bpEngine, resolver, processInstanceId, activity, duty);
		return op.execute();
	}

	public Set<String> nonParticipants(Iterable<String> activities,	TaskDuty duty) {
		Operation<Set<String>> op = new NonParticipantsOP(bpEngine, executionEngine, resolver, processInstanceId, activities, duty);
		return op.execute();
	}

	public Set<String> permanentParticipants(Iterable<String> activities, TaskDuty duty) {
		Operation<Set<String>> op = new PermanentParticipantsOP(bpEngine, resolver, processInstanceId, activities, duty);
		return op.execute();
	}

	public Set<String> criticalActivities(Iterable<String> activities, TaskDuty duty) {
		Operation<Set<String>> op = new CriticalActivitiesOP(bpEngine, resolver, processInstanceId, activities, duty);
		return op.execute();
	}

	public Set<String> criticalParticipants(Iterable<String> activities, TaskDuty duty) {
		Operation<Set<String>> op = new CriticalParticipantsOP(bpEngine, resolver, processInstanceId, activities, duty);
		return op.execute();
	}

	public Set<String> indispensableParticipants(Iterable<String> activities,
			TaskDuty duty) {
		// TODO Auto-generated method stub
		return null;
	}
    
	
}
