package es.us.isa.cristal.owl.mappers.ral.designtimesc;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.analysers.DTSubClassRALAnalyser;
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
public class DTMixAssignmentOntology extends DTAltAssignmentOntology {

    private static final Logger log = Logger.getLogger(DTMixAssignmentOntology.class.getName());

    private ActivityMapper instanceMapper1;
    private ActivityMapper instanceMapper2;

    public DTMixAssignmentOntology(OntologyHandler ontologyHandler, IdMapper idMapper, BPEngine engine) {
        super(ontologyHandler, idMapper, engine, null, null, null);
        this.instanceMapper1 = new ActivityMapperSubClass("inst1-");
        this.instanceMapper2 = new ActivityMapperSubClass("inst2-");
    }

    @Override
    protected void loadActivitySkeleton(String activityName, TaskDuty duty, Cardinality cardinality) {
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

        loadAssignment(activityName);

        loadInstancesSkeleton(activityName, duty, instanceMapper1);
        loadInstancesSkeleton(activityName, duty, instanceMapper2);

        OWLDifferentIndividualsAxiom owlDifferentIndividualsAxiom = factory.getOWLDifferentIndividualsAxiom(factory.getOWLNamedIndividual(instanceMapper1.mapActivity(activityName), prefixManager),
                factory.getOWLNamedIndividual(instanceMapper2.mapActivity(activityName), prefixManager));
        log.info("diff:" + owlDifferentIndividualsAxiom);
        manager.addAxiom(ontology, owlDifferentIndividualsAxiom);

        // Only one instance for each ActivityInstance
//        String axiom = "{" + idMapper.mapActivity(activityName) + "} SubClassOf: inverse(" + Definitions.ISOFTYPE + ") exactly 2";
        String axiom = "{" + instanceMapper1.mapActivity(activityName) + "," + instanceMapper2.mapActivity(activityName) + "} EquivalentTo: " + Definitions.ISOFTYPE + " value " + idMapper.mapActivity(activityName);
        manager.addAxiom(ontology, parser.parseAxiom(axiom));

    }

    private void loadAssignment(String activityName) {
        OWLSubClassOfAxiom owlSubClassOfAxiom;// Assignment
        owlSubClassOfAxiom = factory.getOWLSubClassOfAxiom(
                factory.getOWLClass(activityMapper.mapAssignment(activityName), prefixManager),
                factory.getOWLClass(Definitions.ORGANIZATIONPEOPLE, prefixManager)
        );
        manager.addAxiom(ontology, owlSubClassOfAxiom)  ;
        log.info("Add axiom: " + owlSubClassOfAxiom);
    }


    protected void loadInstancesSkeleton(String activityName, TaskDuty duty, ActivityMapper instanceMapper) {
        OWLNamedIndividual individual = factory.getOWLNamedIndividual(instanceMapper.mapActivity(activityName), prefixManager);

        OWLAxiom ax = factory.getOWLClassAssertionAxiom(
                factory.getOWLClass(activityMapper.mapActivity(activityName), prefixManager),
                individual);
        manager.addAxiom(ontology, ax);
        log.info("Add axiom: " + ax);

    }


    @Override
    protected void addAssignment(String activityName, RALExpr expr, TaskDuty duty) {
        String hasDuty = new InstanceTaskDutyMapper().map(duty);
        String ralMapping = owlRalMapper.map(expr, 0);
        String axiom;

        axiom = "(" + activityMapper.mapActivity(activityName) + ") SubClassOf: (" + hasDuty + " some " + Definitions.PERSON + " and " + hasDuty + " only (" + ralMapping + "))";
//        addAxiom(axiom);

        axiom = "( inverse(" + hasDuty + ") some (" + activityMapper.mapActivity(activityName) + ")) SubClassOf: (" + Definitions.ORGANIZATIONPEOPLE + ")";
//        addAxiom(axiom);



        OWLAxiom axassignment = factory.getOWLEquivalentClassesAxiom(
                factory.getOWLClass(activityMapper.mapAssignment(activityName), prefixManager),
//                parser.parseClassExpression(Definitions.ORGANIZATIONPEOPLE + " and not (inverse("+hasDuty + ") some (" + activityMapper.mapActivity(activityName)+"))"));
                parser.parseClassExpression(classicRalMapper.map(expr, 0)));
        log.info("Add axiom: " + axassignment);
        manager.addAxiom(ontology, axassignment);


        String subClass = "( inverse(" + hasDuty + ") some (" + activityMapper.mapActivity(activityName) + ")) SubClassOf: (" + ralMapping + ")";
//        String subClass = "(" + Definitions.ISOFTYPE + " value " + idMapper.mapActivity(activityName) + ") SubClassOf: (" + hasDuty + " some (" + owlRalMapper.map(expr, 0) + "))";
//        addAxiom(subClass);



//        String critical = "(" + activityMapper.mapActivity(activityName) + ") SubClassOf: (" + Definitions.ORGANIZATION+"criticalResponsible only ( inverse(" + hasDuty + ") some (" + activityMapper.mapActivity(activityName) + ")))";
//        String critical = "(not ( inverse("+hasDuty+" ) some ("+ activityMapper.mapActivity(activityName)+ "))) SubClassOf: (not ( inverse(" + Definitions.ORGANIZATION+"criticalResponsible) some (" + activityMapper.mapActivity(activityName) + ")))";
//        String critical = "( inverse("+Definitions.ORGANIZATION+"criticalResponsible) some ("+ activityMapper.mapActivity(activityName)+ ")) SubClassOf: ( inverse(" + hasDuty +") some (" + activityMapper.mapActivity(activityName) + "))";
//        String critical = "( inverse("+Definitions.ORGANIZATION+"criticalResponsible) some ("+ activityMapper.mapActivity(activityName)+ ")) SubClassOf: ( " + ralMapping +")";
//        String critical = "( inverse(" + Definitions.ORGANIZATION+"criticalResponsible) some (" + activityMapper.mapActivity(activityName) + ")) SubClassOf: (" + ralMapping.replace(Definitions.HASRESPONSIBLE, Definitions.ORGANIZATION+"criticalResponsible") + ")";
//        addAxiom(critical);
//        critical = "(" + activityMapper.mapActivity(activityName) + ") SubClassOf: (" + Definitions.ORGANIZATION+"criticalResponsible max 1 " + Definitions.PERSON + " and " + Definitions.ORGANIZATION+"criticalResponsible only (" + ralMapping.replace(Definitions.HASRESPONSIBLE, Definitions.ORGANIZATION+"criticalResponsible") + "))";
//        addAxiom(critical);
        String critical = "(" + activityMapper.mapActivity(activityName) + ") SubClassOf: (" + Definitions.ORGANIZATION+"criticalResponsible only (inverse(" + hasDuty +") some ({" + instanceMapper1.mapActivity(activityName)+","+instanceMapper2.mapActivity(activityName) + "})))";
        addAxiom(critical);

        addInstanceParticipant(activityName, expr, duty, instanceMapper1);
        addInstanceParticipant(activityName, expr, duty, instanceMapper2);

    }


    protected void addInstanceParticipant(String activityName, RALExpr expr, TaskDuty duty, ActivityMapper instanceMapper) {
        String hasDuty = new InstanceTaskDutyMapper().map(duty);
        String axiom;
        String ralMapping = owlRalMapper.map(expr, 0).replace(activityMapper.mapActivity(activityName), "{"+instanceMapper.mapActivity(activityName)+"}");

//        axiom = "(" + activityMapper.mapActivity(activityName) + ") SubClassOf: (" + hasDuty + " some " + Definitions.ORGANIZATIONPEOPLE + " and " + hasDuty + " only " + Definitions.ORGANIZATIONPEOPLE + ")";
//        addAxiom(axiom);

//        String axiom = "( inverse(" + hasDuty + ") some (" + activityMapper.mapActivity(activityName) + ")) SubClassOf: (" + Definitions.ORGANIZATIONPEOPLE + ")";
        axiom = "( inverse(" + hasDuty + ") value " + instanceMapper.mapActivity(activityName) + ") SubClassOf: (" + Definitions.ORGANIZATIONPEOPLE + ")";
        addAxiom(axiom);


        OWLAxiom type = factory.getOWLClassAssertionAxiom(
                parser.parseClassExpression("(" + hasDuty + " max 1 " + Definitions.ORGANIZATIONPEOPLE + " and " + hasDuty + " only (" + ralMapping + "))"),
                factory.getOWLNamedIndividual(instanceMapper.mapActivity(activityName), prefixManager)
        );
        manager.addAxiom(ontology, type);



//        String subClass = "( inverse(" + hasDuty + ") some (" + activityMapper.mapActivity(activityName) + ")) SubClassOf: (" + owlRalMapper.map(expr, 0) + ")";
        String subClass = "( inverse(" + hasDuty + ") value " + instanceMapper.mapActivity(activityName) + ") SubClassOf: (" + ralMapping + ")";

//        String subClass = "(" + Definitions.ISOFTYPE + " value " + idMapper.mapActivity(activityName) + ") SubClassOf: (" + hasDuty + " some (" + owlRalMapper.map(expr, 0) + "))";

        addAxiom(subClass);

//        String critical = "( inverse(" + Definitions.ORGANIZATION+"criticalResponsible) value " + activityMapper.mapActivity(activityName) + ") SubClassOf: (" + ralMapping.replace(Definitions.HASRESPONSIBLE, Definitions.ORGANIZATION+"criticalResponsible") + ")";
//        addAxiom(critical);
//        critical = "{" + activityMapper.mapActivity(activityName) + "} SubClassOf: (" + Definitions.ORGANIZATION+"criticalResponsible max 1 " + Definitions.ORGANIZATIONPEOPLE + " and " + Definitions.ORGANIZATION+"criticalResponsible only (" + ralMapping.replace(Definitions.HASRESPONSIBLE, Definitions.ORGANIZATION+"criticalResponsible") + "))";
//        addAxiom(critical);

    }


    public RALAnalyser createAnalyser() {

//        RALAnalyser analysers = new DTStardogRALAnalyser(engine, idMapper, prefixManager, activityMapper);
        RALAnalyser analyser = new DTSubClassRALAnalyser(createDLQueryEngine(), idMapper, prefixManager, activityMapper);

        log.info("analyser consistency: " + analyser.basicConsistency(null, TaskDuty.RESPONSIBLE));
        log.info("analyser critical: " + analyser.criticalActivities(new ArrayList<String>(), TaskDuty.RESPONSIBLE));

        return analyser;
    }
}
