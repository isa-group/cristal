package es.us.isa.cristal.owl.ontologyhandlers;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.ResourceAssignment;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.DLHelper;
import es.us.isa.cristal.owl.DLQueryEngine;
import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.analysers.RTRALAnalyser;
import es.us.isa.cristal.owl.mappers.ral.InstanceOwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.RTOwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.InstanceTaskDutyMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.LogMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.RTTaskDutyMapper;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static es.us.isa.cristal.owl.Definitions.*;

/**
* User: resinas
* Date: 13/07/13
* Time: 11:03
*/
public class RTAssignmentOntology extends AssignmentOntology {

    private static final Logger log = Logger.getLogger(RTAssignmentOntology.class.toString());

    private RTOwlRalMapper rtOwlRalMapper;
    private InstanceOwlRalMapper instanceOwlRalMapper;
    private IdMapper idMapper;
    private LogMapper logMapper;
    private DLQueryEngine engine;
    private String pid;
    private String currentInstance;
    private LogOntologyHandler logManager;

    public RTAssignmentOntology(OntologyHandler ontologyHandler, String pid, LogOntologyHandler logManager, IdMapper idMapper, BPEngine bpEngine) {
        super(ontologyHandler);
        this.idMapper = idMapper;
        this.pid = pid;
        this.logManager = logManager;

        logMapper = new LogMapper();
        currentInstance = logMapper.map(pid);

        ontology.getOWLOntologyManager().applyChange(new AddImport(ontology, ontology.getOWLOntologyManager().getOWLDataFactory().getOWLImportsDeclaration(logManager.getOntology().getOntologyID().getOntologyIRI())));

        rtOwlRalMapper = new RTOwlRalMapper(idMapper, bpEngine, logManager);
        instanceOwlRalMapper = new InstanceOwlRalMapper(idMapper, bpEngine);
        engine = ontologyHandler.createDLQueryEngine();
    }

    @Override
    public void buildOntology(ResourceAssignment assignment) {

        for (OWLNamedIndividual a : activityInstancesThatHaveBeenAllocated()) {
            String logA = logMapper.toPrefix(a);
            String act = logManager.getTypeOf(a);

            Map<TaskDuty, RALExpr> assignments = assignment.getByTaskDuty(act);
            for (TaskDuty t : assignments.keySet()) {
                String isDuty = new RTTaskDutyMapper().map(t);
                String hasDuty = new InstanceTaskDutyMapper().map(t);
                String axiom = "(" + isDuty + " value " + logA + ") EquivalentTo: ( inverse(" + hasDuty + ") value " + logA + ")";
                addAxiom(axiom);
            }
        }

        for (OWLNamedIndividual a : activityInstancesBeforeAllocation()) {
            String logA = logMapper.toPrefix(a);
            String act = logManager.getTypeOf(a);

            Map<TaskDuty, RALExpr> assignments = assignment.getByTaskDuty(act);
            for (TaskDuty t : assignments.keySet()) {
                String isDuty = new RTTaskDutyMapper().map(t);
                String axiom = "(" + isDuty + " value " + logA + ") EquivalentTo: " + instanceOwlRalMapper.map(assignments.get(t),pid);
                addAxiom(axiom);
            }
        }

        Set<OWLNamedIndividual> activitiesAlreadyStartedAndNotInLoop = activitiesAlreadyStartedAndNotInLoop();

        for (OWLNamedIndividual a : activitiesInWeakOrderWithRunningInstances()) {
            if (!activitiesAlreadyStartedAndNotInLoop.contains(a)) {
                String activityName = a.getIRI().getFragment();
                String logA = logMapper.map(activityName + "-rtInstance");
                OWLNamedIndividual fakeInstance = logManager.individual(logA);
                OWLNamedIndividual processInstance = logManager.individual(logMapper.map(pid));

                logManager.classAssertion(ACTIVITYINSTANCE, fakeInstance);
                logManager.propertyAssertion(ISOFTYPE, fakeInstance, a);
                logManager.propertyAssertion(HASACTIVITYINSTANCE, processInstance, fakeInstance);
                logManager.noInversePropertyAssertion(HASSTATE, fakeInstance);


                Map<TaskDuty, RALExpr> assignments = assignment.getByTaskDuty(activityName);
                for (TaskDuty t : assignments.keySet()) {
                    String isDuty = new RTTaskDutyMapper().map(t);
                    String axiom = "(" + isDuty + " value " + logA + ") EquivalentTo: " + rtOwlRalMapper.map(assignments.get(t),pid);
                    addAxiom(axiom);
                }
            }
        }

        closeLogOntology();

    }

    private void closeLogOntology() {
        engine = createDLQueryEngine();
        closer("inverse(" + HASACTIVITYINSTANCE + ") value " + currentInstance);
    }

    private void closer(String instancesExpressionString) {
        Set<OWLNamedIndividual> result = engine.getInstances(instancesExpressionString, false);

        if (!result.isEmpty()) {
            LogMapper m = new LogMapper();
            Set<String> names = new HashSet<String>();
            for (OWLNamedIndividual i : result) {
                names.add(m.map(i.getIRI().getFragment()));
            }

            String axiom = "{" + DLHelper.joinWith(names, ",") + "} EquivalentTo: ("+instancesExpressionString + ")";
            log.info("Closing with:" + axiom);
            addAxiom(axiom);
        }
    }

    private Iterable<? extends OWLNamedIndividual> activityInstancesThatHaveBeenAllocated() {
        String commonQuery = "inverse(" + HASACTIVITYINSTANCE + ") value " + currentInstance;
        String afterAllocationQuery = commonQuery + " AND " + HASSTATE + " some " + AFTERALLOCATION;
        log.info("AfterAllocationQuery: " + afterAllocationQuery);

        Set<OWLNamedIndividual> result = engine.getInstances(afterAllocationQuery, false);
        log.info("AfterAllocationQuery result: " + result);

        return result;
    }

    private Iterable<? extends OWLNamedIndividual> activityInstancesBeforeAllocation() {
        String commonQuery = "inverse(" + HASACTIVITYINSTANCE + ") value " + currentInstance;
        String beforeAllocationQuery = commonQuery + " AND " + HASSTATE + " some " + BEFOREALLOCATION;
        log.info("BeforeAllocationQuery: " + beforeAllocationQuery);

        Set<OWLNamedIndividual> result = engine.getInstances(beforeAllocationQuery, false);
        log.info("BeforeAllocationQuery result: " + result);

        return result;
    }

    private Iterable<? extends OWLNamedIndividual> activitiesInWeakOrderWithRunningInstances() {
        String runningInstances = "inverse(" + HASACTIVITYINSTANCE + ") value " + currentInstance + " and not(" + HASSTATE + " some " + ENDSTATE +")";
        testQuery(runningInstances);

        String activityTypeWithRunningInstances = "inverse(" + ISOFTYPE + ") some (" + runningInstances + ")";
        testQuery(activityTypeWithRunningInstances);

        String query = "inverse(" + WEAKORDER + ") some (" + activityTypeWithRunningInstances + ")";
        log.info("ActivitiesInWeakOrderWithRunningInstances: " + query);
        Set<OWLNamedIndividual> result = engine.getInstances(query, false);
        log.info("ActivitiesInWeakOrderWithRunningInstances result: " + result);
        return result;
    }

    private Set<OWLNamedIndividual> activitiesAlreadyStartedAndNotInLoop() {
        String activitiesNotInLoop = "not(" + WEAKORDER + " some Self)";
        String activitiesAlreadyStarted = "inverse(" + ISOFTYPE + ") some (inverse (" + HASACTIVITYINSTANCE + ") value " + currentInstance + " and " + HASSTATE + " some owl:Thing )";
        String query = activitiesAlreadyStarted + " AND " + activitiesNotInLoop;
        log.info("activitiesAlreadyStartedAndNotInLoop: " + query);
        Set<OWLNamedIndividual> result = engine.getInstances(query, false);
        log.info("activitiesAlreadyStartedAndNotInLoop result: " + result);

        return result;
    }

    @Override
    public RALAnalyser createAnalyser() {
        RTRALAnalyser analyser = new RTRALAnalyser(createDLQueryEngine(), idMapper, pid);
        return analyser;
    }

    private void testQuery(String query) {
//        System.out.println(query);
//        Set<OWLNamedIndividual> a = engine.getInstances(query, false);
//        System.out.println(a);

    }
}
