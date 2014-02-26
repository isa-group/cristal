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
public class AllActivitiesOP extends AbstractOP<Set<String>> implements Operation<Set<String>> {

	public AllActivitiesOP(BPEngine bpEngine, Neo4JRalResolver resolver, Object processId, TaskDuty dt) {
		super(bpEngine, resolver, processId, dt);
		
	}

	@Override
	public Set<String> execute() {
		Bpmn20ModelHandler bpmn = bpEngine.getBpmnModel(processId);
		Set<String> result = new HashSet<String>();
		
		for(TTask t: bpmn.getTaskMap().values()){
			result.add(t.getName());
		}
		
		return result;
	}
	
}
