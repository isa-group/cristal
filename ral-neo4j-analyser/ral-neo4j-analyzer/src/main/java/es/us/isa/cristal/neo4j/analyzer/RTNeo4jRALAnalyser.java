package es.us.isa.cristal.neo4j.analyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.neo4j.cypher.ExecutionEngine;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;

/**
 * 
 * @author Manuel Leon
 *
 */
public class RTNeo4jRALAnalyser implements RALAnalyser {
	
	private ExecutionEngine executionEngine;
	
	private BPEngine bpEngine;
	
	private Neo4JRalResolver resolver;
	
	private Object processInstanceId;
	
	public RTNeo4jRALAnalyser(ExecutionEngine executionEngine, BPEngine bpEngine, Object pid) {
		super();
		this.executionEngine = executionEngine;
		this.bpEngine = bpEngine;
		this.processInstanceId = pid;
		resolver = new Neo4JRalResolver(this.bpEngine, this.executionEngine);
		
	}

	public Set<String> potentialParticipants(String activityName, TaskDuty duty) {
		RALExpr exp = bpEngine.getResourceExpressionByProcessInstanceId(this.processInstanceId, activityName);
		Set<String> result = new HashSet<String>();
		result.addAll(resolver.resolve(exp, this.processInstanceId));
		
		
		return result;
	}

	public Set<String> potentialActivities(String personName, TaskDuty duty) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean basicConsistency(String activity, TaskDuty duty) {
		// TODO Auto-generated method stub
		return false;
	}

	public Set<String> nonParticipants(Iterable<String> activities,
			TaskDuty duty) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> permanentParticipants(Iterable<String> activities,
			TaskDuty duty) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> criticalActivities(Iterable<String> activities,
			TaskDuty duty) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> criticalParticipants(Iterable<String> activities,
			TaskDuty duty) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> indispensableParticipants(Iterable<String> activities,
			TaskDuty duty) {
		// TODO Auto-generated method stub
		return null;
	}
    
	
}
