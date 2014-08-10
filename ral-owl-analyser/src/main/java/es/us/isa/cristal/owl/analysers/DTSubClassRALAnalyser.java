package es.us.isa.cristal.owl.analysers;

import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.DLHelper;
import es.us.isa.cristal.owl.DLQueryEngine;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.analysers.operations.PotentialParticipants;
import es.us.isa.cristal.owl.mappers.ral.misc.ActivityMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.InstanceTaskDutyMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.TaskDutyMapper;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class DTSubClassRALAnalyser extends DTAltRALAnalyser {

    private static final Logger log = Logger.getLogger(DTSubClassRALAnalyser.class.getName());

    public DTSubClassRALAnalyser(DLQueryEngine engine, IdMapper mapper, DefaultPrefixManager prefixManager, ActivityMapper activityMapper) {
		super(engine, mapper, activityMapper, prefixManager);
    }

    // Potential participants implementations -----------------------------------

    @Override
    public Set<String> potentialParticipants(String activityName, TaskDuty duty) {
        return new ClassicPP().run(activityName, duty);
    }

    private class ClassicPP implements PotentialParticipants {
        @Override
        public Set<String> run(String activityName, TaskDuty duty) {
            String hasDuty =  taskDutyMapper.map(duty);
            String expressionString = Definitions.ORGANIZATIONPEOPLE + " and not ( inverse("+hasDuty + ") some (" + activityMapper.mapActivity(activityName)+"))";
            log.info("PotentialParticipant: " + expressionString);

            Set<String> notParticipants = DLHelper.mapFromOwl(engine.getInstances(expressionString, false));

//        OWLReasoner reasoner = engine.getReasoner();
//        OWLOntology ontology = reasoner.getRootOntology();
//        OWLOntologyManager manager = ontology.getOWLOntologyManager();
//        OWLDataFactory factory = manager.getOWLDataFactory();
//        OWLClass assign = factory.getOWLClass(activityMapper.mapAssignment(activityName), prefixManager);
//        Set<String> notParticipants = DLHelper.mapFromOwlIndividual(assign.getIndividuals(ontology));
            log.info("Not potential participants: " + notParticipants);

            Set<String> result = new HashSet<String>(organizationPeople);
            result.removeAll(notParticipants);

            return result;
        }

    }
    private class NewPP implements PotentialParticipants {
        @Override
        public Set<String> run(String activityName, TaskDuty duty) {
            String hasDuty =  taskDutyMapper.map(duty);
            String expressionString = " inverse("+hasDuty + ") some (" + activityMapper.mapActivity(activityName)+")";
            log.info("PotentialParticipant: " + expressionString);

            Set<String> notParticipants = DLHelper.mapClassesFromOwl(engine.getSubClasses(expressionString, false));

            log.info("Not potential participants: " + notParticipants);

            Set<String> result = new HashSet<String>(organizationPeople);
            result.removeAll(notParticipants);

            return result;
        }
    }

    private class PeoplePP implements PotentialParticipants {
        @Override
        public Set<String> run(String activityName, TaskDuty duty) {
            String hasDuty =  taskDutyMapper.map(duty);
            OWLReasoner reasoner = engine.getReasoner();
            OWLOntology ontology = reasoner.getRootOntology();
            OWLOntologyManager manager = ontology.getOWLOntologyManager();
            OWLDataFactory factory = manager.getOWLDataFactory();

//        OWLClassExpression classExpression = factory.getOWLObjectIntersectionOf(
//                factory.getOWLObjectSomeValuesFrom(
//                        factory.getOWLObjectInverseOf(factory.getOWLObjectProperty(hasDuty, prefixManager)),
//                        factory.getOWLClass(activityMapper.mapActivity(activityName), prefixManager)),
//                factory.getOWLObjectAllValuesFrom(
//                        factory.getOWLObjectInverseOf(factory.getOWLObjectProperty(hasDuty, prefixManager)),
//                        factory.getOWLClass(activityMapper.mapActivity(activityName), prefixManager))
//        );

            OWLClassExpression classExpression = factory.getOWLObjectSomeValuesFrom(
                    factory.getOWLObjectInverseOf(factory.getOWLObjectProperty(hasDuty, prefixManager)),
                    factory.getOWLClass(activityMapper.mapActivity(activityName), prefixManager));

            Set<OWLNamedIndividual> result = new HashSet<OWLNamedIndividual>();

            Set<OWLNamedIndividual> potential = engine.getInstances(activityMapper.mapAssignment(activityName), false);

//
//        if (potential.size() == organizationPeople.size()) {
//            OWLSubClassOfAxiom axiom = factory.getOWLSubClassOfAxiom(
//                    factory.getOWLClass(Definitions.ORGANIZATIONPEOPLE, prefixManager),
//                    classExpression
//            );
//            manager.addAxiom(ontology, axiom);
//            reasoner.flush();
//            if (reasoner.isConsistent()) {
//                return DLHelper.mapFromOwl(potential);
//            }
//            manager.removeAxiom(ontology, axiom);
//        }


            // Hope for the best
            for (OWLNamedIndividual p: potential) {
                OWLAxiom axiom = factory.getOWLClassAssertionAxiom(classExpression, p);
                manager.addAxiom(ontology, axiom);
            }
            reasoner.flush();

            if (!reasoner.isConsistent()) {
                log.info("Starting one by one");
                for (OWLNamedIndividual p : potential) {
                    OWLAxiom axiom = factory.getOWLClassAssertionAxiom(classExpression, p);
                    manager.removeAxiom(ontology, axiom);
                }
                reasoner.flush();
                for (OWLNamedIndividual p : potential) {
                    OWLAxiom axiom = factory.getOWLClassAssertionAxiom(classExpression, p);
//                OWLAxiom axiom = factory.getOWLSubClassOfAxiom(
//                        factory.getOWLClass(activityMapper.mapActivity(activityName), prefixManager),
//                        factory.getOWLObjectIntersectionOf(
//                                factory.getOWLObjectHasValue(
//                                        factory.getOWLObjectProperty(hasDuty, prefixManager),
//                                        p),
//                                factory.getOWLObjectAllValuesFrom(
//                                        factory.getOWLObjectInverseOf(factory.getOWLObjectProperty(hasDuty, prefixManager)),
//                                        factory.getOWLObjectOneOf(p)
//                                )
//                        )
//                );


                    manager.addAxiom(ontology, axiom);
                    reasoner.flush();

                    if (reasoner.isConsistent()) {
                        result.add(p);
                    } else {
//                    reasoner.flush();
                        log.info("Removed " + p);

                    }
                    manager.removeAxiom(ontology, axiom);

                }
                reasoner.flush();
            } else {
                result = potential;
            }


            return DLHelper.mapFromOwl(result);
        }
    }

    // Potential activities implementation --------------------------------------

    @Override
    public Set<String> potentialActivities(String personName, TaskDuty duty) {
        String hasDuty = taskDutyMapper.map(duty);
        String expressionString = Definitions.ACTIVITYINSTANCE + " and not(" +hasDuty+" value "+ idMapper.mapPerson(personName)+")";
        log.info("PotentialActivities: " + expressionString);

        Set<String> notActivities = DLHelper.mapClassesFromOwl(engine.getSubClasses(expressionString, false));
        log.info("Not potential activities: " + notActivities);

        Set<String> activities = DLHelper.mapClassesFromOwl(engine.getSubClasses(Definitions.ACTIVITYINSTANCE, false));
        activities.removeAll(notActivities);


        return activities;
    }

    // Non participants implementation -------------------------------------------

    @Override
	public Set<String> nonParticipants(Iterable<String> activities, TaskDuty duty) {
        String hasDuty =  taskDutyMapper.map(duty);
        List<String> act = new ArrayList<String>();
        for (String a : activities) {
            act.add(activityMapper.mapActivity(a));
        }

        String expressionString = Definitions.ORGANIZATIONPEOPLE + " and not ( inverse("+hasDuty + ") some ( " + DLHelper.joinWith(act, " or ") +"))";
        log.info("NonParticipants: " + expressionString);

        return DLHelper.mapFromOwl(engine.getInstances(expressionString, false));
    }

    // Critical activities implementation ----------------------------------

    public Set<String> criticalActivities(String person, TaskDuty duty) {
        String hasDuty = taskDutyMapper.map(duty);

        String query = Definitions.ACTIVITYINSTANCE + " and "+hasDuty + " value " + idMapper.mapPerson(person);
        log.info("CriticalActivities query: " + query);

        Set<String> criticalActivities = DLHelper.mapClassesFromOwl(engine.getSubClasses(query, false));
        log.info("Super critical: " + engine.getSuperClasses(query, false));
        log.info("CriticalActivities result: " + criticalActivities);

        return criticalActivities;
    }

    // Critical participants implementation --------------------------------

    public Set<String> criticalParticipants(Iterable<String> activities, TaskDuty duty) {
        String hasDuty = taskDutyMapper.map(duty);

        List<String> act = new ArrayList<String>();
        for (String a : activities) {
            act.add(activityMapper.mapActivity(a));
        }
        String query = "inverse("+hasDuty+") some (" + DLHelper.joinWith(act, " or ") + ")";
        log.info("CriticalParticipants: " + query);

        return DLHelper.mapFromOwl(engine.getInstances(query, false));
    }

    public boolean allActivities(String personName, TaskDuty duty) {
        // There is a different way of implementing this, which involves adding properties to all activities and
        // checking the consistency

        String hasDuty = taskDutyMapper.map(duty);
        OWLReasoner reasoner = engine.getReasoner();
        OWLOntology ontology = reasoner.getRootOntology();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();

        OWLNamedIndividual person = factory.getOWLNamedIndividual(idMapper.mapPerson(personName), prefixManager);
        String query = "( inverse(" + hasDuty + ") max 2 (" + Definitions.ACTIVITYINSTANCE + ")) ";

        OWLAxiom axiom = factory.getOWLClassAssertionAxiom(engine.getParser().parseClassExpression(query), person);

        boolean permanent = ! reasoner.isEntailed(axiom);
        log.info("is permanent " + personName + " " + permanent);

        return permanent;
    }

    public Set<String> permanentParticipants(Iterable<String> activities, TaskDuty duty) {
        throw new UnsupportedOperationException();
    }

    public Set<String> notInvolved(String person, TaskDuty duty) {
        String hasDuty = taskDutyMapper.map(duty);
        String expressionString = Definitions.ACTIVITYINSTANCE + " and not(" + hasDuty + " value " + idMapper.mapPerson(person) + ")";
        log.info("notInvolved query: " + expressionString);

        Set<String> notActivities = DLHelper.mapClassesFromOwl(engine.getSubClasses(expressionString, false));
        log.info("notInvolved result: " + notActivities);

        return notActivities;
    }

    public Set<String> criticalActivities(Iterable<String> activities, TaskDuty duty) {


//        return classiccriticalActivities(activities, duty);
        return newcriticalActivities(activities, duty);
    }
    public Set<String> classiccriticalActivities(Iterable<String> activities, TaskDuty duty) {
        String hasDuty = taskDutyMapper.map(duty);

//        String q = Definitions.ACTIVITYINSTANCE + " and " + hasDuty + " max 1 " + Definitions.ORGANIZATIONPEOPLE;
        String q = Definitions.ORGANIZATION + "critical";
//        String q = Definitions.ACTIVITYINSTANCE + " and " + Definitions.ORGANIZATION +"criticalResponsible max 1 "+ Definitions.ORGANIZATIONPEOPLE;
        log.info("Critical activities query:" + q);

        Set<String> criticalActivities = DLHelper.mapClassesFromOwl(engine.getSubClasses(q, false));
        log.info("Critical activities result: " + criticalActivities);

        return criticalActivities;
    }


    public Set<String> newcriticalActivities(Iterable<String> activities, TaskDuty duty) {
        String hasDuty = taskDutyMapper.map(duty);
        Set<String> criticalActivities = new HashSet<String>();

//        String query = "( inverse(" + hasDuty + ") some (" + Definitions.ACTIVITYINSTANCE + ")) ";
////        query = Definitions.ORGANIZATION + "critical";
//        Set<String> potential = DLHelper.mapFromOwl(engine.getInstances(query, false));
//        return potential;
//        log.info("p: " + potential.size() + "  " + potential);
        for (String p : criticalParticipants(activities, duty)) {
            criticalActivities.addAll(criticalActivities(p, duty));
        }

        return criticalActivities;
    }

    public Set<String> indispensableParticipants(Iterable<String> activities, TaskDuty duty) {
        throw new UnsupportedOperationException();
    }

    public boolean basicConsistency(String activity, TaskDuty duty) {
        boolean inconsistent = false;
        OWLReasoner reasoner = engine.getReasoner();

//        reasoner.isConsistent()
//        try {
//            Set<OWLClass> inconsistentClasses = reasoner.getUnsatisfiableClasses().getEntitiesMinusBottom();
//            log.info("inc: " + inconsistentClasses);
//            inconsistent = inconsistentClasses.isEmpty();
//        } catch (InconsistentOntologyException e) {
//            inconsistent = true;
//        }

        return reasoner.isConsistent();
    }

}
