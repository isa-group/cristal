package es.us.isa.cristal.owl.mappers.ral.designtimesc;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.RALResourceAssignment;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.analysers.DTSubClassRALAnalyser;
import es.us.isa.cristal.owl.mappers.ral.designtime.DTClassicOwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.ActivityMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.ActivityMapperSubClass;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.InstanceTaskDutyMapper;
import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
* User: resinas
* Date: 13/07/13
* Time: 11:01
*/
public class DTSubClassAssignmentOntology extends DTAltAssignmentOntology {

    private static final Logger log = Logger.getLogger(DTSubClassAssignmentOntology.class.getName());

    private static final ActivityMapper ACTIVITY_MAPPER = new ActivityMapperSubClass();

    public DTSubClassAssignmentOntology(OntologyHandler ontologyHandler, IdMapper idMapper, BPEngine engine) {
        super(ontologyHandler, idMapper, engine,
                ACTIVITY_MAPPER,
                new DTSubClassOwlRalMapper(idMapper, engine, ACTIVITY_MAPPER),
                new DTClassicOwlRalMapper(idMapper, engine, ACTIVITY_MAPPER));

    }

    @Override
    protected void loadActivitySkeleton(String activityName, TaskDuty duty, Cardinality cardinality) {
        String hasDuty = new InstanceTaskDutyMapper().map(duty);

        createSubclassForActivity(activityName);
        setCardinalityOfActivityInstances(activityName, cardinality);
        activitySubclassIsSubclassOfActivityInstance(activityName);

        OWLSubClassOfAxiom owlSubClassOfAxiom;
        owlSubClassOfAxiom = factory.getOWLSubClassOfAxiom(
                factory.getOWLClass(activityMapper.mapAssignment(activityName), prefixManager),
                factory.getOWLClass(Definitions.ORGANIZATIONPEOPLE, prefixManager)
        );
        manager.addAxiom(ontology, owlSubClassOfAxiom)  ;
        log.info("Add axiom: " + owlSubClassOfAxiom);
    }

    private void activitySubclassIsSubclassOfActivityInstance(String activityName) {
        OWLSubClassOfAxiom owlSubClassOfAxiom = factory.getOWLSubClassOfAxiom(
                factory.getOWLClass(activityMapper.mapActivity(activityName), prefixManager),
                factory.getOWLClass(Definitions.ACTIVITYINSTANCE, prefixManager)
        );
        manager.addAxiom(ontology, owlSubClassOfAxiom)  ;
        log.info("Add axiom: " + owlSubClassOfAxiom);
    }

    private void setCardinalityOfActivityInstances(String activityName, Cardinality cardinality) {
        String axiom;
//        if (!activityName.equals("activity1")) {
        axiom = "{" + idMapper.mapActivity(activityName) + "} SubClassOf: inverse(" + Definitions.ISOFTYPE + ") "+cardinality+ " 1 " + activityMapper.mapActivity(activityName);
//        } else {
//            axiom = "{" + idMapper.mapActivity(activityName) + "} SubClassOf: inverse(" + Definitions.ISOFTYPE + ") min 1";

//        }
        log.info("Add axiom: " + axiom);
        manager.addAxiom(ontology, parser.parseAxiom(axiom));
    }

    private void createSubclassForActivity(String activityName) {
        OWLAxiom ax = factory.getOWLSubClassOfAxiom(
                factory.getOWLClass(activityMapper.mapActivity(activityName), prefixManager),
                parser.parseClassExpression(Definitions.ISOFTYPE + " value " + idMapper.mapActivity(activityName)));
        manager.addAxiom(ontology, ax);
        log.info("Add axiom: " + ax);
    }

    @Override
    protected void loadOverall(RALResourceAssignment assignment) {
        activityInstanceAsDisjointUnionOfActivitySubclasses(assignment);
    }

    private void activityInstanceAsDisjointUnionOfActivitySubclasses(RALResourceAssignment assignment) {
        Set<OWLClass> classes = new HashSet<OWLClass>();
        for (RALResourceAssignment.Assignment a : assignment.getAll()) {
            classes.add(factory.getOWLClass(activityMapper.mapActivity(a.getActivity()), prefixManager));
        }

        manager.addAxiom(ontology, factory.getOWLDisjointUnionAxiom(
                factory.getOWLClass(Definitions.ACTIVITYINSTANCE, prefixManager), classes
        ));
    }

    @Override
    protected void addAssignment(String activityName, RALExpr expr, TaskDuty duty) {
        String hasDuty = new InstanceTaskDutyMapper().map(duty);
        String ralMapping = owlRalMapper.map(expr, 0);
        String axiom;

        axiom = "(" + activityMapper.mapActivity(activityName) + ") SubClassOf: (" + hasDuty + " max 1 " + Definitions.PERSON + " and " + hasDuty + " only (" + ralMapping + "))";
        addAxiom(axiom);


        axiom = "( inverse(" + hasDuty + ") some (" + activityMapper.mapActivity(activityName) + ")) SubClassOf: (" + Definitions.ORGANIZATIONPEOPLE + ")";
        addAxiom(axiom);



        OWLAxiom axassignment = factory.getOWLEquivalentClassesAxiom(
                factory.getOWLClass(activityMapper.mapAssignment(activityName), prefixManager),
//                parser.parseClassExpression(Definitions.ORGANIZATIONPEOPLE + " and not (inverse("+hasDuty + ") some (" + activityMapper.mapActivity(activityName)+"))"));
                parser.parseClassExpression(classicRalMapper.map(expr, 0)));
        log.info("Add axiom: " + axassignment);
        manager.addAxiom(ontology, axassignment);


        String subClass = "( inverse(" + hasDuty + ") some (" + activityMapper.mapActivity(activityName) + ")) SubClassOf: (" + ralMapping + ")";
//        String subClass = "(" + Definitions.ISOFTYPE + " value " + idMapper.mapActivity(activityName) + ") SubClassOf: (" + hasDuty + " some (" + owlRalMapper.map(expr, 0) + "))";
        addAxiom(subClass);

    }


    public RALAnalyser createAnalyser() {
        engine.getReasoner().flush();

        RALAnalyser analyser = new DTSubClassRALAnalyser(engine, idMapper, prefixManager, activityMapper, organizationPeople);

        return analyser;
    }

}
