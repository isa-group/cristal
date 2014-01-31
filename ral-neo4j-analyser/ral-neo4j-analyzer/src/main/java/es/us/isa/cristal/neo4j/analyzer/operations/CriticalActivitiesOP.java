package es.us.isa.cristal.neo4j.analyzer.operations;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;

public class CriticalActivitiesOP extends AbstractOP<Set<String>> implements Operation<Set<String>> {

	private Iterable<String> activities;
	
	public CriticalActivitiesOP(BPEngine bpEngine, Neo4JRalResolver resolver, Object processId, Iterable<String> activities, TaskDuty duty) {
		super(bpEngine, resolver, processId, duty);
		this.activities = activities;
	}

	

	public Set<String> execute() {
		Set<String>	criticalActivities = new HashSet<String>();
		Iterator<String> activityIterator = this.activities.iterator();
		//for each activity, find the potential participants: 
		//if there is only 1 potential participant, it is a critical activity
		while(activityIterator.hasNext()){
			String activityName = activityIterator.next();
			PotentialParticipantsOP pp = new PotentialParticipantsOP(bpEngine,resolver,processId,activityName,duty);
			if(pp.execute().size()==1){
				criticalActivities.add(activityName);
			}
			
		}
		
		return criticalActivities; 
	}


	
}
