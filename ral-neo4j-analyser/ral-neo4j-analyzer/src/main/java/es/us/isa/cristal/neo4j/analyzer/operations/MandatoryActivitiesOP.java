package es.us.isa.cristal.neo4j.analyzer.operations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neo4j.helpers.collection.Iterables;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.xmlClasses.bpmn20.TBaseElement;
import es.us.isa.bpmn.xmlClasses.bpmn20.TParallelGateway;
import es.us.isa.bpmn.xmlClasses.bpmn20.TStartEvent;
import es.us.isa.bpmn.xmlClasses.bpmn20.TUserTask;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;

public class MandatoryActivitiesOP extends AbstractOP<Set<String>> implements Operation<Set<String>> {

	private Iterable<String> activities;
	
	public MandatoryActivitiesOP(BPEngine bpEngine, Neo4JRalResolver resolver,
			Object processId, Iterable<String> activities, TaskDuty dt) {
		super(bpEngine, resolver, processId, dt);
		this.activities = activities;
	}

	@Override
	public Set<String> execute() {
		Set<String> result = new HashSet<String>();
		Bpmn20ModelHandler bpmn = bpEngine.getBpmnModel(this.processId);
		Map<TBaseElement, Double> probs = new HashMap<TBaseElement, Double>();
		Integer numberOfStarts = bpmn.getStartEventMap().size();
		for(String startId: bpmn.getStartEventMap().keySet()){
			TStartEvent ts = bpmn.getStartEventMap().get(startId);
			probs.put(ts, 100.0/numberOfStarts.doubleValue());
			processElement(ts, probs, bpmn);
		}
		
		for(TBaseElement el: probs.keySet()){
			if(el instanceof TUserTask){
				if(probs.get(el).doubleValue() == 100){
					result.add(((TUserTask) el).getName());
				}
			}
		}
		
		List<String> activitiesCollection = Iterables.toList(activities);
		result.retainAll(activitiesCollection);
		return result;
	}
	
	private void processElement(TBaseElement element, Map<TBaseElement, Double> probs, Bpmn20ModelHandler bpmn){
		Double probability = probs.get(element);
		List<TBaseElement> targets = getFlowTargets(element, bpmn);
		setTargetsProbability(element, targets, probs);
		
	}
	
	private  List<TBaseElement> getFlowTargets(TBaseElement source, Bpmn20ModelHandler bpmn){
		List<TBaseElement> result = new LinkedList<TBaseElement>();
		
		return result;
	}
	
	private void setTargetsProbability(TBaseElement element, List<TBaseElement> targets, Map<TBaseElement, Double> probs){
		Double probability = probs.get(element);
		if(element instanceof TParallelGateway){
			setTargetsProbability(targets,probability, probs);
		}else{
			setTargetsProbability(targets,probability.doubleValue()/targets.size(), probs);
		}
	}
	
	private void setTargetsProbability(List<TBaseElement> targets, Double prob, Map<TBaseElement, Double> probs){
		for(TBaseElement el: targets){
			probs.put(el, prob);
		}
	}
	
	private boolean isGoingBackFlow(){
		return false;
	}
	

}
