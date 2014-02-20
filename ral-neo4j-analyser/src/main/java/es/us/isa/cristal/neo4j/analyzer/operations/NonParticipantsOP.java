package es.us.isa.cristal.neo4j.analyzer.operations;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.helpers.collection.IteratorUtil;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;

/**
 * 
 * @author Manuel Leon
 *
 */
public class NonParticipantsOP extends AbstractOP<Set<String>> implements Operation<Set<String>> {

	private Iterable<String> activities;

	private ExecutionEngine execEngine;
	
	public NonParticipantsOP(BPEngine bpEngine, ExecutionEngine execEngine, Neo4JRalResolver resolver, Object processId, Iterable<String> activities, TaskDuty duty) {
		super(bpEngine, resolver, processId, duty);
		this.activities = activities;
		this.execEngine = execEngine;
	}

	

	public Set<String> execute() {
		Set<String>	performers = new HashSet<String>();
		//get all persons
		Set<String> persons = getAllPersons();
		
		Iterator<String> activityIterator;
		if(activities==null){
			Operation<Set<String>> op = new AllActivitiesOP(bpEngine, resolver, processId, duty);
			activityIterator = op.execute().iterator();
		}else{
			activityIterator = this.activities.iterator();
		}
		
		
		//for each activity, find the potential participants, and add them to the performers set
		while(activityIterator.hasNext()){
			String activityName = activityIterator.next();
			PotentialParticipantsOP pp = new PotentialParticipantsOP(bpEngine,resolver,processId,activityName,duty);
			performers.addAll(pp.execute());
		}
		//remove performers from all persons, and we have the people who cannot perform any activity
		persons.removeAll(performers);
		return persons; 
	}


	private Set<String> getAllPersons(){
		Set<String> res = new HashSet<String>();
		String query = "START person=node:node_auto_index('name:*') RETURN DISTINCT person.name";
		ExecutionResult result = execEngine.execute(query);
		for(Object p: IteratorUtil.asIterable(result.javaColumnAs("person.name"))) {
			res.add(p.toString());
		}
		return res;
	}
}
