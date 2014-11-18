package es.us.isa.cristal.owl.mappers.ral.designtimesc;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.*;
import es.us.isa.cristal.owl.analysers.DTInstancesRALAnalyser;
import es.us.isa.cristal.owl.analysers.DTSubClassRALAnalyser;
import es.us.isa.cristal.owl.mappers.ral.designtime.DTClassicOwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.ActivityMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.ActivityMapperSubClass;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.InstanceTaskDutyMapper;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
* User: resinas
* Date: 13/07/13
* Time: 11:01
*/
public class DTInstanceAssignmentOntology extends DTAltAssignmentOntology {

    private static final Logger log = Logger.getLogger(DTInstanceAssignmentOntology.class.getName());

    private static final ActivityMapper ACTIVITY_MAPPER = new ActivityMapperSubClass();

    public DTInstanceAssignmentOntology(OntologyHandler ontologyHandler, IdMapper idMapper, BPEngine engine) {
        super(ontologyHandler, idMapper, engine,
                ACTIVITY_MAPPER,
                new DTSubClassOwlRalMapper(idMapper, engine, ACTIVITY_MAPPER),
                new DTClassicOwlRalMapper(idMapper, engine, ACTIVITY_MAPPER));

    }


    @Override
    protected void loadActivitySkeleton(String activityName, TaskDuty duty, Cardinality cardinality) {
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

    @Override
    protected void addAssignment(String activityName, RALExpr expr, TaskDuty duty) {
        String hasDuty = new InstanceTaskDutyMapper().map(duty);
        String axiom;
        String ralMapping = owlRalMapper.map(expr, 0);




//        axiom = "(" + activityMapper.mapActivity(activityName) + ") SubClassOf: (" + hasDuty + " some " + Definitions.ORGANIZATIONPEOPLE + " and " + hasDuty + " only " + Definitions.ORGANIZATIONPEOPLE + ")";
//        addAxiom(axiom);

//        String axiom = "( inverse(" + hasDuty + ") some (" + activityMapper.mapActivity(activityName) + ")) SubClassOf: (" + Definitions.ORGANIZATIONPEOPLE + ")";
        axiom = "( inverse(" + hasDuty + ") value " + activityMapper.mapActivity(activityName) + ") SubClassOf: (" + Definitions.ORGANIZATIONPEOPLE + ")";
        addAxiom(axiom);


        OWLAxiom type = factory.getOWLClassAssertionAxiom(
                parser.parseClassExpression("(" + hasDuty + " max 1 " + Definitions.ORGANIZATIONPEOPLE + " and " + hasDuty + " only (" + ralMapping + "))"),
                factory.getOWLNamedIndividual(activityMapper.mapActivity(activityName), prefixManager)
        );
        manager.addAxiom(ontology, type);

        OWLAxiom axassignment = factory.getOWLEquivalentClassesAxiom(
                factory.getOWLClass(activityMapper.mapAssignment(activityName), prefixManager),
//                parser.parseClassExpression(Definitions.ORGANIZATIONPEOPLE + " and not (inverse("+hasDuty + ") some (" + activityMapper.mapActivity(activityName)+"))"));
                parser.parseClassExpression(classicRalMapper.map(expr, 0)));
        log.info("Add axiom: " + axassignment);
        manager.addAxiom(ontology, axassignment);



//        String subClass = "( inverse(" + hasDuty + ") some (" + activityMapper.mapActivity(activityName) + ")) SubClassOf: (" + owlRalMapper.map(expr, 0) + ")";
        String subClass = "( inverse(" + hasDuty + ") value " + activityMapper.mapActivity(activityName) + ") SubClassOf: (" + ralMapping + ")";

//        String subClass = "(" + Definitions.ISOFTYPE + " value " + idMapper.mapActivity(activityName) + ") SubClassOf: (" + hasDuty + " some (" + owlRalMapper.map(expr, 0) + "))";

//        addAxiom(subClass);

    }

    public RALAnalyser createAnalyser() {

//        RALAnalyser analysers = new DTStardogRALAnalyser(engine, idMapper, prefixManager, activityMapper);
        RALAnalyser analyser = new DTInstancesRALAnalyser(engine, idMapper, prefixManager, activityMapper);

        log.info("analyser consistency: " + analyser.basicConsistency(null, TaskDuty.RESPONSIBLE));
        log.info("analyser critical: " + analyser.criticalActivities(new ArrayList<String>(), TaskDuty.RESPONSIBLE));

        return analyser;
    }

}
