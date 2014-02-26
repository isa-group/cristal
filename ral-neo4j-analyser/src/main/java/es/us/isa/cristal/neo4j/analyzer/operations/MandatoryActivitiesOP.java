package es.us.isa.cristal.neo4j.analyzer.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.neo4j.helpers.collection.Iterables;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.xmlClasses.bpmn20.TBaseElement;
import es.us.isa.bpmn.xmlClasses.bpmn20.TParallelGateway;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSequenceFlow;
import es.us.isa.bpmn.xmlClasses.bpmn20.TStartEvent;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;

/**
 * 
 * @author Manuel Leon
 *
 */
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
		Map<TBaseElement, BpmnElementData> probs = new HashMap<TBaseElement, BpmnElementData>();
		Integer numberOfStarts = bpmn.getStartEventMap().size();
		log("START PROCESSING START ELEMENTS");
		for(String startId: bpmn.getStartEventMap().keySet()){
			TStartEvent ts = bpmn.getStartEventMap().get(startId);
			BpmnElementData data = new BpmnElementData();
			data.setProbability(100.0/numberOfStarts.doubleValue());
			probs.put(ts, data);
			processElement(ts, probs, bpmn);
		}
		log("START PROCESSING UNHANDLED ELEMENTS");
		//Are there flows which return back and there are elements waiting for them?
		List<TBaseElement> unhandledFlows = getUnhandledFlows(probs);
		while(!unhandledFlows.isEmpty()){
			for(TBaseElement flow: unhandledFlows){
				log(" ------  handling: " + flow.getId());
				handleUnhandledFlow(flow,probs,bpmn);
			}
			
			
			unhandledFlows = getUnhandledFlows(probs);
		}
		
		
		for(TBaseElement el: probs.keySet()){
			if(el instanceof TTask){
				if(probs.get(el).getProbability().doubleValue() == 100){
					result.add(((TTask) el).getName());
				}
			}
		}
		if(activities!=null){
			List<String> activitiesCollection = Iterables.toList(activities);
			result.retainAll(activitiesCollection);
		}
		return result;
	}
	
	private void handleUnhandledFlow(TBaseElement element, Map<TBaseElement, BpmnElementData> probs, Bpmn20ModelHandler bpmn){
		Iterator<TBaseElement> it = probs.get(element).getWaitingSources().iterator();
		while(it.hasNext()){
			TBaseElement source = it.next();
			if(containsTargetDeep(element,source, bpmn)){
				//probs.get(element).getBlackList().add(source);
				it.remove();
				List<PathNode> path = getReturningPath(element,element, new ArrayList<PathNode>(), new ArrayList<TBaseElement>(),bpmn);
				removePath(path, probs, bpmn);
				processElement(element, probs, bpmn);
			}
		}
		
		
	}
	
	private boolean containsTargetDeep(TBaseElement element, TBaseElement wantedTarget, Bpmn20ModelHandler bpmn){
		boolean result=false;
		List<TBaseElement> targetTargets = getFlowTargets(element,bpmn);
		if(targetTargets.contains(wantedTarget)){
			result = true;
		}else{
			for(TBaseElement t: targetTargets){
				result = result || containsTargetDeep(t,wantedTarget, bpmn);
				if(result==true){
					break;
				}
			}
		}
		return result;
	}
	
	private List<PathNode> getReturningPath(TBaseElement element, TBaseElement wantedTarget, List<PathNode> path, List<TBaseElement> exclusions, Bpmn20ModelHandler bpmn){
		List<TBaseElement> sources = getFlowSources(element,bpmn);
		List<TBaseElement> targets = getFlowTargets(element,bpmn);
		exclusions.add(element);
		
				
		
		if(targets.size()>1){
			path.clear();
		}else{
			//¿se puede desplazar la comprobacion a un source?
			sources.removeAll(exclusions);
			for(TBaseElement source: sources){
				if(getFlowSources(source,bpmn).size()>1 && getFlowTargets(source,bpmn).size()==1 && containsTargetDeep(source,source, bpmn)){
					return getReturningPath(source,source,new ArrayList<PathNode>(),exclusions,bpmn);
				}
			}
		}
		if(targets.contains(wantedTarget)){
			//if(!(sources.size()>1 && targets.size()==1)){
				PathNode pn = new PathNode(element,wantedTarget);
				path.add(pn);
			//}
			return path;
		}else{
			for(TBaseElement t: targets){
				if(containsTargetDeep(t, wantedTarget, bpmn)){
					//if(!(getFlowSources(t,bpmn).size()>1 && getFlowTargets(t,bpmn).size()==1)){
						PathNode pn = new PathNode(element,t);
						path.add(pn);
					//}
					getReturningPath(t,wantedTarget,path, exclusions, bpmn);
				}
			}
		}
		return path;
	}
	
	private void removePath(List<PathNode> path, Map<TBaseElement, BpmnElementData> probs, Bpmn20ModelHandler bpmn){
		for(PathNode node: path){
			removeFlow(node.getSource(), node.getTarget(), probs, bpmn);
			log("FLOW REMOVED: " + node.getSource().getId() + " --> " + node.getTarget().getId());
		}
	}
	
	private void removeFlow(TBaseElement source, TBaseElement target, Map<TBaseElement, BpmnElementData> probs, Bpmn20ModelHandler bpmn){
		Iterator<TSequenceFlow> it = bpmn.getSequenceFlowMap().values().iterator();
		while(it.hasNext()){
			TSequenceFlow flow = it.next();
			if(flow.getSourceRef().equals(source) && flow.getTargetRef().equals(target)){
				it.remove();
				break;
			}
		}
	}
	
	private List<TBaseElement> getUnhandledFlows(Map<TBaseElement, BpmnElementData> probs){
		List<TBaseElement> result = new LinkedList<TBaseElement>();
		for(Entry<TBaseElement, BpmnElementData> entry: probs.entrySet()){
			if(entry.getValue().getWaitingSources()!=null && !entry.getValue().getWaitingSources().isEmpty()){
				result.add(entry.getKey());
			}
		}
		return result;
	}
	
	
	private void processElement(TBaseElement element, Map<TBaseElement, BpmnElementData> probs, Bpmn20ModelHandler bpmn){
		List<TBaseElement> targets = getFlowTargets(element, bpmn);
		updateTargetsProbability(element, targets, probs, bpmn);
		for(TBaseElement el: targets){
			if(!probs.containsKey(el) || probs.get(el).getWaitingSources()==null || probs.get(el).getWaitingSources().isEmpty()){
				if(!isElementBlackListed(element, el, probs)){
					processElement(el, probs,bpmn);
				}
			}
		}
	}
	
	private boolean isElementBlackListed(TBaseElement source, TBaseElement target, Map<TBaseElement, BpmnElementData> probs){
		return probs.containsKey(target) && probs.get(target).getBlackList().contains(source);
	}
	private void updateTargetsProbability(TBaseElement element, List<TBaseElement> targets, Map<TBaseElement, BpmnElementData> probs, Bpmn20ModelHandler bpmn){
		Double probability = probs.get(element).getProbability();
		Double targetProb;
		if(element instanceof TParallelGateway){
			targetProb = probability;
		}else{
			targetProb=probability.doubleValue()/targets.size();
		}
		
		for(TBaseElement target: targets){
			updateTargetProbability(element, target, targetProb, probs, bpmn);
		}
	}
	
	private void updateTargetProbability(TBaseElement source, TBaseElement target, Double prob, Map<TBaseElement, BpmnElementData> probs, Bpmn20ModelHandler bpmn){
		List<TBaseElement> allSources = getFlowSources(target, bpmn);
		
		
		if(allSources.size()==1){
			//Si sólo tiene 1 source: 
			if(!probs.containsKey(target)){
				//  --> Si no existe se le asigna el prob.
				BpmnElementData data = new BpmnElementData();
				data.setProbability(prob);
				probs.put(target, data);
				
				log("[" + target.getId() + "] --> marked: " + prob);
				
			}else{
				//  --> si existe, se le asigna el más alto entre el existente y el nuevo
				Double oldProb = probs.get(target).getProbability();
				if(prob>oldProb){
					probs.get(target).setProbability(prob);
					log("[" + target.getId() + "] --> updated: " + prob);
				}
				
			}
		}else if (allSources.size()>1){
			//si tiene multiples sources:
			if(!probs.containsKey(target)){
				//  --> si no existe, después de asignarsele la probabilidad, se obtienen los sources, se elimina el actual y se añade a waiting.
				BpmnElementData data = new BpmnElementData();
				data.setProbability(prob);
				allSources.remove(source);
				data.setWaitingSources(allSources);
				probs.put(target,data);
				log("[" + target.getId() + "] --> marked: " + data);
			}else{
				//  --> si existe:
				//     > Se obtienen de waiting los sources restantes y se elimina de la lista de sources pendientes.
				probs.get(target).getWaitingSources().remove(source);
				//	   > Se actualiza la probabilidad:
				log("[" + target.getId() + "] --> deleted WS: " + source.getId() + ". WS: " + probs.get(target).getWaitingSources());
				if(target instanceof TParallelGateway){
					//	     - Si es un ParallelGateway: elige entre la mayor probabilidad entre el valor anterior y el nuevo
					Double oldProb = probs.get(target).getProbability();
					if(prob>oldProb){
						probs.get(target).setProbability(prob);
					}
				}else{
					//      - Si es otra cosa: suma probabilidad.
					Double newProb = probs.get(target).getProbability() + prob;
					if(newProb>100.0){
						newProb=100.0;
					}
					probs.get(target).setProbability(newProb);
				}
				log("[" + target.getId() + "] --> updated: " + probs.get(target));
				
				
			}
		}	
		
	}
	
	
	private  List<TBaseElement> getFlowTargets(TBaseElement source, Bpmn20ModelHandler bpmn){
		List<TBaseElement> result = new LinkedList<TBaseElement>();
		for (TSequenceFlow flow: bpmn.getSequenceFlowMap().values()){
			if(flow.getSourceRef().equals(source)){
				result.add((TBaseElement) flow.getTargetRef());
			}
		}
		return result;
	}
	
	private  List<TBaseElement> getFlowSources(TBaseElement element, Bpmn20ModelHandler bpmn){
		List<TBaseElement> result = new LinkedList<TBaseElement>();
		for (TSequenceFlow flow: bpmn.getSequenceFlowMap().values()){
			if(flow.getTargetRef().equals(element)){
				result.add((TBaseElement) flow.getSourceRef());
			}
		}
		return result;
	}
	
	
	private void log(String info){
		//System.out.println(info);
	}
	
	
	
	private class BpmnElementData{
		
		private Double probability;
		
		private List<TBaseElement> waitingSources;
		
		private List<TBaseElement> blackList;

		public BpmnElementData(){
			waitingSources = new LinkedList<TBaseElement>();
			blackList = new LinkedList<TBaseElement>();
		}
		
		public final Double getProbability() {
			return probability;
		}

		public final void setProbability(Double probability) {
			this.probability = probability;
		}

		public final List<TBaseElement> getWaitingSources() {
			return waitingSources;
		}

		public final void setWaitingSources(List<TBaseElement> waitingSources) {
			this.waitingSources = waitingSources;
		}

		public final List<TBaseElement> getBlackList() {
			return blackList;
		}

		@Override
		public String toString() {
			return "BpmnElementData {p=" + probability
					+ ", ws=" + waitingSources + ", bl="
					+ blackList + "}";
		}

		
	}
	
	private class PathNode{
		private TBaseElement source;
		private TBaseElement target;
		
		
		
		public PathNode(TBaseElement source, TBaseElement target) {
			super();
			this.source = source;
			this.target = target;
		}
		public final TBaseElement getSource() {
			return source;
		}
		
		public final TBaseElement getTarget() {
			return target;
		}
		@Override
		public String toString() {
			return "PathNode [source=" + source.getId() + ", target=" + target.getId() + "]";
		}
		
		
		
	}

}
