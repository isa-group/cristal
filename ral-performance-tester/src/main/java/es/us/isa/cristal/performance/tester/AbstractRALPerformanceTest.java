package es.us.isa.cristal.performance.tester;

import es.us.isa.cristal.*;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.neo4j.analyzer.Neo4jRALAnalyser;
import es.us.isa.cristal.organization.generator.GenerationMode;
import es.us.isa.cristal.organization.generator.GeneratorConfiguration;
import es.us.isa.cristal.organization.generator.OrganizationGenerator;
import es.us.isa.cristal.organization.generator.distributors.*;
import es.us.isa.cristal.organization.generator.functions.ConstantFunction;
import es.us.isa.cristal.organization.generator.selectors.ConsecutiveSelector;
import es.us.isa.cristal.organization.generator.selectors.RandomSelector;
import es.us.isa.cristal.organization.model.gson.OrganizationalModel;
import es.us.isa.cristal.organization.model.gson.Position;
import es.us.isa.cristal.organization.model.gson.Role;
import es.us.isa.cristal.owl.OntologyNamespaces;
import es.us.isa.cristal.owl.RALOntologyManager;
import es.us.isa.cristal.owl.mappers.organization.GsonModelToOrganization;
import es.us.isa.cristal.performance.tester.meters.TimeMeter;
import es.us.isa.cristal.performance.tester.util.PerformanceTesterBPEngine;
import es.us.isa.cristal.performance.tester.util.QueryProcessor;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.util.StringLogger;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * AbstractRALPerformanceTest
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class AbstractRALPerformanceTest extends AbstractPerformanceTest {

    protected RawResourceAssignment loadAssignment(OrganizationalModel model, String... values) {
        RawResourceAssignment assignment = new RawResourceAssignment();
        QueryProcessor processor = new QueryProcessor();

        for (int i = 0; i < values.length - 1; i+=2) {
            assignment.add(values[i], processor.processQuery(values[i + 1], model));
        }

        return assignment;
    }

    // Organizational model ------------------------------------------------------------

    protected OrganizationalModel generateModel(int modelWeight) {
        OrganizationGenerator generator = new OrganizationGenerator(buildConfiguration(modelWeight));

        System.out.println("Generating model...");
        return generator.generate(GenerationMode.DISABLE_DELEGATESTO, GenerationMode.DISABLE_REPORTSTO);
    }

    private GeneratorConfiguration buildConfiguration(int weight) {
        int f = 20;

        GeneratorConfiguration result= new GeneratorConfiguration();

        result.setNumberOfPersonsFunction(new ConstantFunction(weight*3/2));
        result.setNumberOfPositionsFunction(new ConstantFunction(weight));
        result.setNumberOfUnitsFunction(new ConstantFunction(1));
        result.setNumberOfRolesFunction(new ConstantFunction(weight*3/f));
//        result.setNumberOfPersonsFunction(new RandomFunction(weight, weight*2));
//        result.setNumberOfPositionsFunction(new RandomFunction(weight/f, weight*2/f));
//        result.setNumberOfUnitsFunction(new ConstantFunction(2));
//        result.setNumberOfRolesFunction(new RandomFunction(weight*2/f, weight*3/f));

        result.setPersonPositionDistributor(new PersonPositionConsecutiveDistributor());
        result.setRolePositionDistributor(new RolePositionMultipleRandomDistributor(1,3,new ConsecutiveSelector<Role>()));
        result.setPositionUnitDistributor(new PositionUnitConsecutiveDistributor());
        result.setPositionDelegatesDistributor(new PositionDelegatesMultipleRandomDistributor(0,8,new RandomSelector<Position>()));
        result.setPositionReportsDistributor(new PositionReportsMultipleRandomDistributor(0,8,new RandomSelector<Position>()));

        return result;

    }


    // Analysers ------------------------------------------------------------

    protected RALAnalyser neo4JAnalyser(OrganizationalModel orgModel, RawResourceAssignment assignment) {
        ExecutionEngine engine;

        String dirName = System.getenv("TEMP") + File.separator + "neo4j-" + UUID.randomUUID().toString();
        GraphDatabaseService graphDb = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(dirName)
                .setConfig(GraphDatabaseSettings.node_keys_indexable, "name, position, role, unit")
                .setConfig(GraphDatabaseSettings.node_auto_indexing, "true")
                .newGraphDatabase();
        engine = new ExecutionEngine(graphDb, StringLogger.logger(new File(dirName + File.separator + "log.log")));
        engine.execute(orgModel.getCypherCreateQuery());

        BPEngine bpEngine = new PerformanceTesterBPEngine(null, assignment);
        Neo4jRALAnalyser analyzer = new Neo4jRALAnalyser(engine, bpEngine, 0);

        return analyzer;
    }


    protected RALAnalyser owlAnalyser(OrganizationalModel orgModel, RawResourceAssignment assignment, boolean fullPrecompute) {
        return owlAnalyser(orgModel, assignment, fullPrecompute, new HashMap<String, Long>());
    }

    protected RALAnalyser owlAnalyser(OrganizationalModel orgModel, RawResourceAssignment assignment, boolean fullPrecompute, Map<String, Long> measures) {
        TimeMeter timeMeter = new TimeMeter();
        System.out.println("Setting up...");
        timeMeter.start();
        BPEngine engine = new PerformanceTesterBPEngine(orgModel, assignment);
        RALOntologyManager manager = new RALOntologyManager(createOntologyNamespaces(), engine);
        timeMeter.stop();
        measures.put("Setting up", timeMeter.getResult());

        System.out.println("Loading organization ontology...");
        timeMeter.start();
        Organization org = new GsonModelToOrganization().map(orgModel);
        manager.loadOrganizationOntology(org);
        timeMeter.stop();
        measures.put("Loading organization", timeMeter.getResult());


        System.out.println("Load process ontology...");
        timeMeter.start();
        String[] activities = assignment.getActivities().toArray(new String[assignment.getActivities().size()]);
        manager.loadProcessAsListOfActivities(activities);
        timeMeter.stop();
        measures.put("Loading process", timeMeter.getResult());

        RALResourceAssignment ralResourceAssignment = new RALResourceAssignment();
        ralResourceAssignment.addFromString(assignment);

        System.out.println("Precompute models");
        timeMeter.start();
        manager.precomputeDesignTimeModels(ralResourceAssignment, fullPrecompute);
        timeMeter.stop();
        measures.put("Precompute models", timeMeter.getResult());

        System.out.println("Loading resource assignment...");
        timeMeter.start();
        manager.loadResourceAssignment(ralResourceAssignment);
        timeMeter.stop();
        measures.put("Loading assignment", timeMeter.getResult());

        timeMeter.start();
        RALAnalyser designTimeAnalyser = manager.createDesignTimeAnalyser();
        timeMeter.stop();
        measures.put("Creating analyser", timeMeter.getResult());

        return designTimeAnalyser;
    }

    protected String toString(RawResourceAssignment assignment) {
        StringBuilder sb = new StringBuilder();

        for (ResourceAssignment<String>.Assignment<String> assign : assignment.getAll()) {
            sb.append("{").append(assign.getActivity()).append(", ")
                    .append(assign.getDuty()).append(", ")
                    .append(assign.getExpr()).append("}\n");
        }

        return sb.toString();
    }

    private OntologyNamespaces createOntologyNamespaces() {
        OntologyNamespaces namespaces = new OntologyNamespaces();
        namespaces.setPerson("org", "http://localhost/orgperformance");
        namespaces.setGroup("org", "http://localhost/orgperformance");
        namespaces.setActivity("bp", "http://localhost/bpperformance");
        return namespaces;
    }

}
