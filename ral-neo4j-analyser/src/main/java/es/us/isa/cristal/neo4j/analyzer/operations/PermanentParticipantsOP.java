package es.us.isa.cristal.neo4j.analyzer.operations;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;

/**
 * 
 * @author Manuel Leon
 *
 */
public class PermanentParticipantsOP extends AbstractOP<Set<String>> implements Operation<Set<String>> {

	private Iterable<String> activities;
	
	public PermanentParticipantsOP(BPEngine bpEngine, Neo4JRalResolver resolver, Object processId, Iterable<String> activities, TaskDuty duty) {
		super(bpEngine, resolver, processId, duty);
		this.activities = activities;
	}

	

	public Set<String> execute() {
		Set<String>	performers = null;

		Iterator<String> activityIterator;
		if(activities==null){
			Operation<Set<String>> op = new AllActivitiesOP(bpEngine, resolver, processId, duty);
			activityIterator = op.execute().iterator();
		}else{
			activityIterator = this.activities.iterator();
		}
		
		//for each activity, find the potential participants, and retain those who
		//were there before. Except the first time
		while(activityIterator.hasNext()){
			String activityName = activityIterator.next();
			PotentialParticipantsOP pp = new PotentialParticipantsOP(bpEngine,resolver,processId,activityName,duty);
			if(performers==null){
				performers=new HashSet<String>();
				performers.addAll(pp.execute());
			}else{
				performers.retainAll(pp.execute());
			}
			
		}
		
		//if the performers is still null (no activities), return an empty hashset
		if(performers==null){
			performers = new HashSet<String>();
		}
		
		return performers; 
	}


	
}
