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
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.analyzer.Neo4jRALAnalyser;
import es.us.isa.cristal.neo4j.analyzer.util.DesignTimeAnalyserBPEngine;
import es.us.isa.cristal.organization.model.gson.Document;

/**
 * 
 * @author Manuel Leon
 *
 */
@Controller
@RequestMapping("/analyser")
public class RestRalNeo4JAnalyzer {

	
	
}
