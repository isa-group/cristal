package es.us.isa.cristal.analyser.web.rest;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.util.StringLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.analyser.web.services.DataAccessService;
import es.us.isa.cristal.analyser.web.util.DesignTimeAnalyserBPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.analyzer.Neo4jRALAnalyser;
import es.us.isa.cristal.organization.model.gson.Document;

/**
 * 
 * @author Manuel Leon
 *
 */
@Controller
@RequestMapping("/analyser")
public class RestRalNeo4JAnalyzer {

	@Autowired
	DataAccessService dataAccessService;

	

	
	@RequestMapping(value = "/potential_participants/{processId}/{activityName}/{duty}", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> potentialParticipants(@PathVariable("processId") String processId,  @RequestParam("bpmn") String bpmnModelUrl, @RequestParam("organization") String organizationModelUrl, @PathVariable("activityName") String activityName, @PathVariable("duty") String duty) throws Exception {
		RALAnalyser analyser = getAnalyser(processId, bpmnModelUrl, organizationModelUrl,null);
		return analyser.potentialParticipants(activityName, TaskDuty.valueOf(duty));
	}
	
	@RequestMapping(value = "/check_participants_for_expression/{processId}/{activityName}/{duty}", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> checkParticipantsForExpression(@RequestParam("expression") String expression, @PathVariable("processId") String processId,  @RequestParam("bpmn") String bpmnModelUrl, @RequestParam("organization") String organizationModelUrl, @PathVariable("activityName") String activityName, @PathVariable("duty") String duty) throws Exception {
		RALAnalyser analyser = getAnalyser(processId, bpmnModelUrl, organizationModelUrl,expression);
		Set<String> result = analyser.potentialParticipants(activityName, TaskDuty.valueOf(duty));
		System.out.println("REUSLT: " + result);
		return result;
	}

	@RequestMapping(value = "/potential_activities/{processId}/{personName}/{duty}", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> potentialActivities(@PathVariable("processId") String processId, @RequestParam("bpmn") String bpmnModelUrl, @RequestParam("organization") String organizationModelUrl, @PathVariable("personName") String personName, @PathVariable("duty") String duty) throws Exception {
		RALAnalyser analyser = getAnalyser(processId, bpmnModelUrl, organizationModelUrl,null);
		return analyser.potentialActivities(personName, TaskDuty.valueOf(duty));
	}

	@RequestMapping(value = "/basic_consistency/{processId}/{activityName}/{duty}", method = RequestMethod.GET)
	@ResponseBody
	public boolean basicConsistency(@PathVariable("processId") String processId,  @RequestParam("bpmn") String bpmnModelUrl, @RequestParam("organization") String organizationModelUrl, @PathVariable("activityName") String activityName, @PathVariable("duty") String duty) throws Exception {
		RALAnalyser analyser = getAnalyser(processId, bpmnModelUrl, organizationModelUrl,null);
		return analyser.basicConsistency(activityName, TaskDuty.valueOf(duty));
	}

	@RequestMapping(value = "/non_participants/{processId}/{activities}/{duty}", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> nonParticipants(@PathVariable("processId") String processId, @RequestParam("bpmn") String bpmnModelUrl, @RequestParam("organization") String organizationModelUrl, @PathVariable("activities") String activities, @PathVariable("duty") String duty) throws Exception {
		RALAnalyser analyser = getAnalyser(processId, bpmnModelUrl, organizationModelUrl,null);
		List<String> acts = getActivities(activities);
		return analyser.nonParticipants(acts, TaskDuty.valueOf(duty));
	}

	@RequestMapping(value = "/permanent_participants/{processId}/{activities}/{duty}", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> permanentParticipants(@PathVariable("processId") String processId, @RequestParam("bpmn") String bpmnModelUrl, @RequestParam("organization") String organizationModelUrl, @PathVariable("activities") String activities, @PathVariable("duty") String duty) throws Exception {
		RALAnalyser analyser = getAnalyser(processId, bpmnModelUrl, organizationModelUrl,null);
		List<String> acts = getActivities(activities);
		return analyser.permanentParticipants(acts, TaskDuty.valueOf(duty));
	}

	@RequestMapping(value = "/critical_activities/{processId}/{activities}/{duty}", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> criticalActivities(@PathVariable("processId") String processId, @RequestParam("bpmn") String bpmnModelUrl, @RequestParam("organization") String organizationModelUrl, @PathVariable("activities") String activities, @PathVariable("duty") String duty) throws Exception {
		RALAnalyser analyser = getAnalyser(processId, bpmnModelUrl, organizationModelUrl,null);
		List<String> acts = getActivities(activities);
		return analyser.criticalActivities(acts, TaskDuty.valueOf(duty));
	}

	@RequestMapping(value = "/critical_participants/{processId}/{activities}/{duty}", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> criticalParticipants(@PathVariable("processId") String processId, @RequestParam("bpmn") String bpmnModelUrl, @RequestParam("organization") String organizationModelUrl, @PathVariable("activities") String activities, @PathVariable("duty") String duty) throws Exception {
		RALAnalyser analyser = getAnalyser(processId, bpmnModelUrl, organizationModelUrl,null);
		System.out.println("----> ACTIVITIES: " + activities);
		List<String> acts = getActivities(activities);
		System.out.println("----> LIST ACTIVITIES: " + acts);
		return analyser.criticalParticipants(acts, TaskDuty.valueOf(duty));
	}

	

	@RequestMapping(value = "/indispensable_participants/{processId}/{activities}/{duty}", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> indispensableParticipants(@PathVariable("processId") String processId, @RequestParam("bpmn") String bpmnModelUrl, @RequestParam("organization") String organizationModelUrl, @PathVariable("activities") String activities, @PathVariable("duty") String duty) throws Exception {
		RALAnalyser analyser = getAnalyser(processId, bpmnModelUrl, organizationModelUrl,null);
		List<String> acts = getActivities(activities);
		return analyser.indispensableParticipants(acts, TaskDuty.valueOf(duty));
	}

	
	
	
	private List<String> getActivities(String activities) {
		List<String> acts = null;
		if(!activities.isEmpty() && !activities.equalsIgnoreCase("all")){
			acts = Arrays.asList(activities.split(";"));
		}
		return acts;
	}
	
	//@Cacheable(value = "defaultCache", key = "#processId.concat('-').concat(#bpmnModelUrl).concat('-').concat(#organizationModelUrl).concat('-').concat(#ralExpr)")
	private RALAnalyser getAnalyser(String processId, String bpmnModelUrl, String organizationModelUrl, String ralExpr) throws Exception {
		
		//System.out.println("CREATE NEW ANALYZER:" + ralExpr);
		String key = ralExpr == null ? "assignment" : "analyser";
		String organization = dataAccessService.getContentFromUrl(organizationModelUrl, "org");
		String bpmn = dataAccessService.getContentFromUrl(bpmnModelUrl, key);

		Document doc = Document.importFromJson(organization);
		ExecutionEngine engine;
		
		String dirName = System.getenv("TEMP") + File.separator + "neo4j-" + UUID.randomUUID().toString();
		GraphDatabaseService graphDb = new GraphDatabaseFactory()
		.newEmbeddedDatabaseBuilder(dirName)
		.setConfig(GraphDatabaseSettings.node_keys_indexable,"name, position, role, unit")
		.setConfig(GraphDatabaseSettings.node_auto_indexing, "true")
		.newGraphDatabase();
		engine = new ExecutionEngine( graphDb,StringLogger.logger(new File(dirName + File.separator + "log.log")) );
		engine.execute(doc.getCypherCreateQuery());

		BPEngine bpEngine = new DesignTimeAnalyserBPEngine(bpmn, ralExpr);
		Neo4jRALAnalyser analyzer = new Neo4jRALAnalyser(engine, bpEngine, processId);

		
		
		return analyzer;
	}
	
}
