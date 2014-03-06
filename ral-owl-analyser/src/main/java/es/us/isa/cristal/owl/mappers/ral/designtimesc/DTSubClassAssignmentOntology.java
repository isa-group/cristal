package es.us.isa.cristal.owl.mappers.ral.designtimesc;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.ResourceAssignment;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.*;
import es.us.isa.cristal.owl.analysers.DTStardogRALAnalyser;
import es.us.isa.cristal.owl.analysers.DTSubClassRALAnalyser;
import es.us.isa.cristal.owl.mappers.ral.designtime.DTOwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.InstanceTaskDutyMapper;
import es.us.isa.cristal.owl.ontologyhandlers.AssignmentOntology;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
* User: resinas
* Date: 13/07/13
* Time: 11:01
*/
public class DTSubClassAssignmentOntology extends AssignmentOntology {

    private static final Logger log = Logger.getLogger(DTSubClassAssignmentOntology.class.getName());

    private final OwlRalMapper owlRalMapper;
    private final OwlRalMapper classicRalMapper;
    private final IdMapper idMapper;

    private final OWLOntologyManager manager;
    private final OWLDataFactory factory;

    private final ActivityMapper activityMapper;

    private final DLQueryParser parser;
    private DLQueryEngine engine;


    public DTSubClassAssignmentOntology(OntologyHandler ontologyHandler, IdMapper idMapper, BPEngine engine) {
        super(ontologyHandler);
        this.idMapper = idMapper;
        owlRalMapper = new DTSubClassOwlRalMapper(idMapper, engine, new ActivityMapper());
        classicRalMapper = new DTOwlRalMapper(idMapper, engine, new ActivityMapper());
        manager = ontology.getOWLOntologyManager();
        factory = manager.getOWLDataFactory();
        activityMapper = new ActivityMapper();
        parser = createDLQueryParser();

    }

    @Override
    public void buildOntology(ResourceAssignment assignment) {
        this.engine = createDLQueryEngine();
        log.info(engine.getReasoner().getBufferingMode().toString());
        log.info(engine.getReasoner().getPrecomputableInferenceTypes().toString());

//        engine.getReasoner().precomputeInferences(InferenceType.CLASS_HIERARCHY);
//        ((IncrementalClassifier) engine.getReasoner()).classify();

        for (ResourceAssignment.Assignment a : assignment.getAll()) {
//            loadSkeletonClasses(a.getActivity(), a.getDuty());
            loadSkeletonInstances(a.getActivity(), a.getDuty());

        }

        for (ResourceAssignment.Assignment a : assignment.getAll()) {
            addParticipant(a.getActivity(), a.getExpr(), a.getDuty());
//            ((IncrementalClassifier) engine.getReasoner()).classify();
        }

        // Add CRITICAL
        String q = Definitions.ACTIVITYINSTANCE + " and " + Definitions.HASRESPONSIBLE + " max 1 " + Definitions.ORGANIZATIONPEOPLE;
        OWLClassAxiom axiom = factory.getOWLEquivalentClassesAxiom(factory.getOWLClass(IRI.create(Definitions.ORGANIZATION_IRI + "#critical")),
                parser.parseClassExpression(q));
        manager.addAxiom(ontology, axiom);

//        engine.getReasoner().flush();
//        ((IncrementalClassifier) engine.getReasoner()).classify();
    }

    private void loadSkeletonClasses(String activityName, TaskDuty duty) {
        String hasDuty = new InstanceTaskDutyMapper().map(duty);

        OWLAxiom ax = factory.getOWLEquivalentClassesAxiom(
                factory.getOWLClass(activityMapper.mapActivity(activityName), prefixManager),
                parser.parseClassExpression(Definitions.ISOFTYPE + " value " + idMapper.mapActivity(activityName)));
        manager.addAxiom(ontology, ax);
        log.info("Add axiom: " + ax);


        OWLSubClassOfAxiom owlSubClassOfAxiom = factory.getOWLSubClassOfAxiom(
                factory.getOWLClass(activityMapper.mapActivity(activityName), prefixManager),
                factory.getOWLClass(Definitions.ACTIVITYINSTANCE, prefixManager)
        );
        manager.addAxiom(ontology, owlSubClassOfAxiom)  ;
        log.info("Add axiom: " + owlSubClassOfAxiom);

//        owlSubClassOfAxiom = factory.getOWLSubClassOfAxiom(
//                factory.getOWLClass(activityMapper.mapActivity(activityName), prefixManager),
//                parser.parseClassExpression(hasDuty + " max 1 " + Definitions.ORGANIZATIONPEOPLE)
//        );
//        manager.addAxiom(ontology, owlSubClassOfAxiom)  ;
//        log.info("Add axiom: " + owlSubClassOfAxiom);


        owlSubClassOfAxiom = factory.getOWLSubClassOfAxiom(
                factory.getOWLClass(activityMapper.mapAssignment(activityName), prefixManager),
                factory.getOWLClass(Definitions.ORGANIZATIONPEOPLE, prefixManager)
        );
        manager.addAxiom(ontology, owlSubClassOfAxiom)  ;
        log.info("Add axiom: " + owlSubClassOfAxiom);
    }

    private void loadSkeletonInstances(String activityName, TaskDuty duty) {
        String hasDuty = new InstanceTaskDutyMapper().map(duty);

        OWLNamedIndividual individual = factory.getOWLNamedIndividual(activityMapper.mapActivity(activityName), prefixManager);

        OWLAxiom ax = factory.getOWLClassAssertionAxiom(
                factory.getOWLClass(Definitions.ACTIVITYINSTANCE, prefixManager),
                individual);
        manager.addAxiom(ontology, ax);
        log.info("Add axiom: " + ax);

        // Assignment
        OWLSubClassOfAxiom owlSubClassOfAxiom = factory.getOWLSubClassOfAxiom(
                factory.getOWLClass(activityMapper.mapAssignment(activityName), prefixManager),
                factory.getOWLClass(Definitions.ORGANIZATIONPEOPLE, prefixManager)
        );
        manager.addAxiom(ontology, owlSubClassOfAxiom)  ;
        log.info("Add axiom: " + owlSubClassOfAxiom);
    }

    private void addParticipant(String activityName, RALExpr expr, TaskDuty duty) {
        String hasDuty = new InstanceTaskDutyMapper().map(duty);




//        String axiom = "(" + activityMapper.mapActivity(activityName) + ") SubClassOf: (" + hasDuty + " some " + Definitions.ORGANIZATIONPEOPLE + " and " + hasDuty + " only " + Definitions.ORGANIZATIONPEOPLE + ")";

//        String axiom = "( inverse(" + hasDuty + ") some (" + activityMapper.mapActivity(activityName) + ")) SubClassOf: (" + Definitions.ORGANIZATIONPEOPLE + ")";
        String axiom = "( inverse(" + hasDuty + ") value " + activityMapper.mapActivity(activityName) + ") SubClassOf: (" + Definitions.ORGANIZATIONPEOPLE + ")";
        addAxiom(axiom);

        OWLAxiom axassignment = factory.getOWLEquivalentClassesAxiom(
                factory.getOWLClass(activityMapper.mapAssignment(activityName), prefixManager),
//                parser.parseClassExpression(Definitions.ORGANIZATIONPEOPLE + " and not (inverse("+hasDuty + ") some (" + activityMapper.mapActivity(activityName)+"))"));
                parser.parseClassExpression(classicRalMapper.map(expr, 0)));
        log.info("Add axiom: " + axassignment);
        manager.addAxiom(ontology, axassignment);



//        String subClass = "( inverse(" + hasDuty + ") some (" + activityMapper.mapActivity(activityName) + ")) SubClassOf: (" + owlRalMapper.map(expr, 0) + ")";
        String subClass = "( inverse(" + hasDuty + ") value " + activityMapper.mapActivity(activityName) + ") SubClassOf: (" + owlRalMapper.map(expr, 0) + ")";

//        String subClass = "(" + Definitions.ISOFTYPE + " value " + idMapper.mapActivity(activityName) + ") SubClassOf: (" + hasDuty + " some (" + owlRalMapper.map(expr, 0) + "))";

        addAxiom(subClass);
    }


    public RALAnalyser createAnalyser() {

//        RALAnalyser analysers = new DTStardogRALAnalyser(engine, idMapper, prefixManager, activityMapper);
        RALAnalyser analyser = new DTSubClassRALAnalyser(createDLQueryEngine(), idMapper, prefixManager, activityMapper);

        log.info("analyser consistency: " + analyser.basicConsistency(null, TaskDuty.RESPONSIBLE));
        log.info("analyser critical: " + analyser.criticalActivities(new ArrayList<String>(), TaskDuty.RESPONSIBLE));
        return analyser;
    }

    public static class ActivityMapper {
        public String mapActivity(String activityName) {
            return RALOntologyManager.DTASSIGNMENT + activityName;
        }

        public String mapAssignment(String activityName) {
            return RALOntologyManager.DTASSIGNMENT + activityName + "Assignment";
        }
    }

}
