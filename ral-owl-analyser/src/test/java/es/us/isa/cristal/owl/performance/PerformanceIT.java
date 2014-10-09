package es.us.isa.cristal.owl.performance;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.Organization;
import es.us.isa.cristal.RALResourceAssignment;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
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
import es.us.isa.cristal.organization.model.util.IOUtil;
import es.us.isa.cristal.owl.OntologyNamespaces;
import es.us.isa.cristal.owl.RALOntologyManager;
import es.us.isa.cristal.owl.mappers.organization.GsonModelToOrganization;
import es.us.isa.cristal.parser.RALParser;
import es.us.isa.cristal.performance.tester.data.ExecutionData;
import es.us.isa.cristal.performance.tester.data.Query;
import es.us.isa.cristal.performance.tester.exporters.ExporterFactory;
import es.us.isa.cristal.performance.tester.meters.Meter;
import es.us.isa.cristal.performance.tester.meters.TimeMeter;
import es.us.isa.cristal.performance.tester.util.PerformanceTesterBPEngine;
import es.us.isa.cristal.performance.tester.util.QueryProcessor;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.*;

/**
 * PerformanceIT
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class PerformanceIT {

    @Test
    public void basicTest() throws URISyntaxException {
        String path = "queryStack.json";

        // Set up performance evaluation environment
        ExecutionData edata = importQueryStack(path);
        OrganizationalModel model = generateModel(edata);
        QueryProcessor processor = new QueryProcessor();
        BPEngine engine = new PerformanceTesterBPEngine(model, edata.getActivityQueryMap());

        System.out.println("Mapping to organization...");
        Organization org = new GsonModelToOrganization().map(model);

        RALOntologyManager manager = new RALOntologyManager(createOntologyNamespaces(), engine);

        System.out.println("Loading organization ontology...");
        OWLOntology orgOntology = manager.loadOrganizationOntology(org);
        OWLOntologyManager owlManager = orgOntology.getOWLOntologyManager();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("org.owl"));
            owlManager.saveOntology(orgOntology, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }


        System.out.println("Load process ontology...");
        List<String> activities = new ArrayList<String>();
        for (Query q : edata.getQueryList()) {
            activities.add(q.getActivity());
        }
        OWLOntology procOntology = manager.loadProcessAsListOfActivities(activities.toArray(new String[activities.size()]));
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("proc.owl"));
            owlManager.saveOntology(procOntology, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }

        System.out.println(" Loading resource assignment...");
        Map<String, String> finalExpressions = new HashMap<String, String>();
        RALResourceAssignment assignment = new RALResourceAssignment();
        for (Query q : edata.getQueryList()) {
            String expression = q.getQuery();
            String finalExpression = processor.processQuery(expression, model);
            finalExpressions.put(q.getActivity(), finalExpression);

            RALExpr expr = RALParser.parse(finalExpression);
            assignment.add(q.getActivity(), expr);
        }

        manager.loadResourceAssignment(assignment);
        System.out.println("Executing queries...");

        for (int i = 0; i < edata.getIterations(); i++) {
            System.out.println("------ Iteration " + i + " -----");
            Meter<Long> meter = new TimeMeter();
            Meter<Long> meterPreparing = new TimeMeter();
            Meter<Long> meterConsistency = new TimeMeter();
            Meter<Long> meterCritical = new TimeMeter();


            System.out.println(" Preparing analyser");
            meterPreparing.start();
            RALAnalyser analyser = manager.createDesignTimeAnalyser();
            meterPreparing.stop();
            System.out.println(" Time preparing: " + meterPreparing.getResult());

//            System.out.println(" Consistency");
//            meterConsistency.start();
//            analyser.basicConsistency(null, TaskDuty.RESPONSIBLE);
//            meterConsistency.stop();
//            System.out.println(" Time consistency: " + meterConsistency.getResult());
//
//            System.out.println(" critical");
//            meterCritical.start();
//            analyser.criticalActivities(finalExpressions.keySet(), TaskDuty.RESPONSIBLE);
//            meterCritical.stop();
//            System.out.println(" Time critical: " + meterCritical.getResult());
            meter.start();
            Set<String> criticalActivities = analyser.criticalActivities(new ArrayList<String>(), TaskDuty.RESPONSIBLE);
            meter.stop();
            System.out.println("analyser critical: " + meter.getResult() + "-" + criticalActivities);


            meter.start();
            boolean basicConsistency = analyser.basicConsistency(null, TaskDuty.RESPONSIBLE);
            meter.stop();
            System.out.println("analyser consistency: " + meter.getResult() + "-" + basicConsistency);


            for (Query q : edata.getQueryList()) {
                System.out.println(" Analysis of " + q.getActivity());
                meter.start();
                Set<String> participants = analyser.potentialParticipants(q.getActivity(), TaskDuty.RESPONSIBLE);
                meter.stop();

                System.out.println(participants);
                System.out.println(participants.size());
                System.out.println(" Time analysis: " + meter.getResult());

                q.addExecution(finalExpressions.get(q.getActivity()), participants.toString(), meter.getResult());
            }
        }

        exportResults(edata, model);

    }

    private OntologyNamespaces createOntologyNamespaces() {
        OntologyNamespaces namespaces = new OntologyNamespaces();
        namespaces.setPerson("org", "http://localhost/orgperformance");
        namespaces.setGroup("org", "http://localhost/orgperformance");
        namespaces.setActivity("bp", "http://localhost/bpperformance");
        return namespaces;
    }

    private ExecutionData importQueryStack(String path) {
        System.out.println("Importing query stack...");
        return ExecutionData.importFromJson(IOUtil.convertStreamToString(getClass().getResourceAsStream(path)));
    }

    private OrganizationalModel generateModel(ExecutionData edata) {
        OrganizationGenerator generator = new OrganizationGenerator(buildConfiguration(edata.getModelWeight()));

        System.out.println("Generating model...");
        return generator.generate(GenerationMode.DISABLE_DELEGATESTO, GenerationMode.DISABLE_REPORTSTO);
    }

    private void exportResults(ExecutionData edata, OrganizationalModel model) {
        System.out.println("Exporting results...");
        ExporterFactory exporterFactory = new ExporterFactory();
        exporterFactory.getExporter(edata.getExport()).export(edata.getExport(), edata, model);
        System.out.println("Results exported...");
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
        result.setRolePositionDistributor(new RolePositionMultipleRandomDistributor(1,4,new ConsecutiveSelector<Role>()));
        result.setPositionUnitDistributor(new PositionUnitConsecutiveDistributor());
        result.setPositionDelegatesDistributor(new PositionDelegatesMultipleRandomDistributor(0,8,new RandomSelector<Position>()));
        result.setPositionReportsDistributor(new PositionReportsMultipleRandomDistributor(0,8,new RandomSelector<Position>()));

        return result;

    }

}
