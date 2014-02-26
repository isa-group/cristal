package es.us.isa.cristal.neo4j.analyzer.operations;

import java.util.HashSet;
import java.util.Set;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;
import es.us.isa.cristal.neo4j.analyzer.extractors.ExtractorRepository;

/**
 * 
 * @author Manuel Leon
 *
 */
public class PotentialParticipantsOP extends AbstractOP<Set<String>> implements Operation<Set<String>> {

	private String activityName;

	
	
	public PotentialParticipantsOP(BPEngine bpEngine, Neo4JRalResolver resolver, Object processId, String activityName, TaskDuty duty) {
		super(bpEngine, resolver, processId, duty);
		this.activityName = activityName;
	}

	

	public Set<String> execute() {
		Set<String>	result = new HashSet<String>();	
		RALExpr expr = ExtractorRepository.getExtractor(duty).extractExpression(bpEngine, processId, activityName);
		result.addAll(this.resolver.resolve(expr, processId));
		return result; 
	}

	

}
