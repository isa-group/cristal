package es.us.isa.cristal.owl.assignments;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.ResourceAssignment;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.DLHelper;
import es.us.isa.cristal.owl.DLQueryEngine;
import es.us.isa.cristal.owl.RALOntologyManager;
import es.us.isa.cristal.owl.analysers.RTRALAnalyser;
import es.us.isa.cristal.owl.mappers.LogOntologyManager;
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
    private LogOntologyManager logManager;

    public RTAssignmentOntology(RALOntologyManager ralOntologyManager, String pid, String assignmentIRI, LogOntologyManager logManager, IdMapper idMapper, BPEngine bpEngine) {
        super(ralOntologyManager, assignmentIRI);
        this.idMapper = idMapper;
        this.pid = pid;
        this.logManager = logManager;

        logMapper = new LogMapper();
        currentInstance = logMapper.map(pid);

        ontology.getOWLOntologyManager().applyChange(new AddImport(ontology, ontology.getOWLOntologyManager().getOWLDataFactory().getOWLImportsDeclaration(logManager.getOntology().getOntologyID().getOntologyIRI())));

        rtOwlRalMapper = new RTOwlRalMapper(idMapper, bpEngine, ralOntologyManager);
        instanceOwlRalMapper = new InstanceOwlRalMapper(idMapper, bpEngine);
        engine = ralOntologyManager.createDLQueryEngine(ontology);
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
        engine = ralOntologyManager.createDLQueryEngine(ontology);
        String instancesExpressionString = "inverse(" + HASACTIVITYINSTANCE + ") value " + currentInstance;
        Set<OWLNamedIndividual> result = engine.getInstances(instancesExpressionString, false);

        LogMapper m = new LogMapper();
        Set<String> names = new HashSet<String>();
        for (OWLNamedIndividual i : result) {
            names.add(m.map(i.getIRI().getFragment()));
        }

        String axiom = "{" + DLHelper.joinWith(names, ",") + "} EquivalentTo: (inverse(" + HASACTIVITYINSTANCE + ") value "+ currentInstance + ")";
        addAxiom(axiom);
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
        RTRALAnalyser analyser = new RTRALAnalyser(ralOntologyManager.createDLQueryEngine(ontology), idMapper, pid);
        return analyser;
    }

    private void testQuery(String query) {
//        System.out.println(query);
//        Set<OWLNamedIndividual> a = engine.getInstances(query, false);
//        System.out.println(a);

    }
}
