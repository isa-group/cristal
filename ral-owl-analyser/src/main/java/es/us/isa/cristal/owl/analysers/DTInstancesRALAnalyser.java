package es.us.isa.cristal.owl.analysers;

import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.DLHelper;
import es.us.isa.cristal.owl.DLQueryEngine;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.mappers.ral.misc.ActivityMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class DTInstancesRALAnalyser extends DTAltRALAnalyser {

    private static final Logger log = Logger.getLogger(DTInstancesRALAnalyser.class.getName());

    public DTInstancesRALAnalyser(DLQueryEngine engine, IdMapper mapper, DefaultPrefixManager prefixManager, ActivityMapper activityMapper) {
		super(engine, mapper, activityMapper, prefixManager);
    }


    private Set<String> classicPP(String activityName, TaskDuty duty) {
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

    private Set<String> instancePP(String activityName, TaskDuty duty) {
        String hasDuty =  taskDutyMapper.map(duty);
        String expressionString = Definitions.ORGANIZATIONPEOPLE + " and not ( inverse("+hasDuty + ") value " + activityMapper.mapActivity(activityName)+")";
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

    @Override
    public Set<String> potentialParticipants(String activityName, TaskDuty duty) {
//        return classicPP(activityName, duty);
//        return peoplePP(activityName, duty);
//        return instancePP(activityName, duty);
        return peopleInstancePP(activityName, duty);
    }

    private Set<String> peoplePP(String activityName, TaskDuty duty) {
        String hasDuty =  taskDutyMapper.map(duty);
        OWLReasoner reasoner = engine.getReasoner();
        OWLOntology ontology = reasoner.getRootOntology();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();

        OWLClassExpression classExpression = factory.getOWLObjectIntersectionOf(
                factory.getOWLObjectSomeValuesFrom(
                        factory.getOWLObjectInverseOf(factory.getOWLObjectProperty(hasDuty, prefixManager)),
                        factory.getOWLClass(activityMapper.mapActivity(activityName), prefixManager)),
                factory.getOWLObjectAllValuesFrom(
                        factory.getOWLObjectInverseOf(factory.getOWLObjectProperty(hasDuty, prefixManager)),
                        factory.getOWLClass(activityMapper.mapActivity(activityName), prefixManager))
        );

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
            manager.addAxiom(ontology,axiom);
        }
        reasoner.flush();

        if (!reasoner.isConsistent()) {
            for (OWLNamedIndividual p: potential) {
                OWLAxiom axiom = factory.getOWLClassAssertionAxiom(classExpression, p);
                manager.removeAxiom(ontology,axiom);
            }
            reasoner.flush();
            for (OWLNamedIndividual p : potential) {
//                OWLAxiom axiom = factory.getOWLClassAssertionAxiom(classExpression, p);
                OWLAxiom axiom = factory.getOWLSubClassOfAxiom(
                        factory.getOWLClass(activityMapper.mapActivity(activityName), prefixManager),
                        factory.getOWLObjectIntersectionOf(
                                factory.getOWLObjectHasValue(
                                        factory.getOWLObjectProperty(hasDuty, prefixManager),
                                        p),
                                factory.getOWLObjectAllValuesFrom(
                                        factory.getOWLObjectInverseOf(factory.getOWLObjectProperty(hasDuty, prefixManager)),
                                        factory.getOWLObjectOneOf(p)
                                )
                        )
                );

                log.info(axiom.toString());

                manager.addAxiom(ontology, axiom);
                reasoner.flush();

                if (reasoner.isConsistent()) {
                    result.add(p);
                } else {
//                    reasoner.flush();
                }
                manager.removeAxiom(ontology, axiom);

            }
            reasoner.flush();
        } else {
            result = potential;
        }


        return DLHelper.mapFromOwl(result);
    }

    private Set<String> peopleInstancePP(String activityName, TaskDuty duty) {
        String hasDuty =  taskDutyMapper.map(duty);
        Set<OWLNamedIndividual> result = new HashSet<OWLNamedIndividual>();
        Set<OWLNamedIndividual> potential = engine.getInstances(activityMapper.mapAssignment(activityName), false);


        OWLReasoner reasoner = engine.getReasoner();
        OWLOntology ontology = reasoner.getRootOntology();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();


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
        Set<OWLAxiom> addedAxioms = new HashSet<OWLAxiom>();
        for (OWLNamedIndividual p: potential) {
            OWLAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(
                    factory.getOWLObjectProperty(hasDuty, prefixManager),
                    factory.getOWLNamedIndividual(activityMapper.mapActivity(activityName), prefixManager),
                    p
            );
            addedAxioms.add(axiom);
        }
        manager.addAxioms(ontology, addedAxioms);
        reasoner.flush();

        if (!reasoner.isConsistent()) {
            manager.removeAxioms(ontology, addedAxioms);
            reasoner.flush();

            for (OWLAxiom axiom : addedAxioms) {
                log.info(axiom.toString());

                manager.addAxiom(ontology, axiom);
                reasoner.flush();

                if (reasoner.isConsistent()) {
                    result.add(((OWLObjectPropertyAssertionAxiom)axiom).getObject().asOWLNamedIndividual());
                }

                manager.removeAxiom(ontology, axiom);
            }

            reasoner.flush();
        } else {
            result = potential;
        }


        return DLHelper.mapFromOwl(result);
    }

    @Override
    public Set<String> potentialActivities(String personName, TaskDuty duty) {
        String isPotentialDuty = taskDutyMapper.map(duty);
        String expressionString = Definitions.ACTIVITYINSTANCE + " and not(" +isPotentialDuty+" value "+ idMapper.mapPerson(personName)+")";
        log.info("PotentialActivities: " + expressionString);

        Set<String> notActivities = DLHelper.mapFromOwl(engine.getInstances(expressionString, false));
        log.info("Not potential activities: " + notActivities);

        Set<String> activities = DLHelper.mapFromOwl(engine.getInstances(Definitions.ACTIVITYINSTANCE, false));
        activities.removeAll(notActivities);

        return activities;
    }
	
	public Set<String> nonParticipants(Iterable<String> activities, TaskDuty duty) {
        String hasDuty =  taskDutyMapper.map(duty);
        List<String> act = new ArrayList<String>();
        for (String a : activities) {
            act.add(activityMapper.mapActivity(a));
        }

        String expressionString = Definitions.ORGANIZATIONPEOPLE + " and not ( inverse("+hasDuty + ") some {" + DLHelper.joinWith(act, ",")+"})";

//        String expressionString = Definitions.ORGANIZATIONPEOPLE + " and not (inverse("+hasDuty + ") some (" + Definitions.ISOFTYPE + " some { " + DLHelper.joinWith(act, ",") +"}))";
        log.info("NonParticipants: " + expressionString);
        return DLHelper.mapFromOwl(engine.getInstances(expressionString, false));
    }

    public Set<String> permanentParticipants(Iterable<String> activities, TaskDuty duty) {
        String hasDuty = taskDutyMapper.map(duty);
        List<String> act = new ArrayList<String>();
        for (String a : activities) {
//            act.add(isPotentialDuty + " value " + idMapper.mapActivity(a));
            act.add("("+Definitions.PERSON + " and not (inverse(" + hasDuty + ") some (" + Definitions.ISOFTYPE + " value " + idMapper.mapActivity(a) + ")))");

        }

        String query = DLHelper.joinWith(act, " or ");
        log.info("PermanentParticipants: " + query);
        Set<String> notParticipants = DLHelper.mapFromOwl(engine.getInstances(query, false));
        log.info("Not potential participants: " + notParticipants);

        Set<String> participants = new HashSet<String>(organizationPeople);
        participants.removeAll(notParticipants);

        return participants;
    }

    public Set<String> criticalActivities(String person, TaskDuty duty) {
        String hasDuty = taskDutyMapper.map(duty);

        String query = hasDuty + " value " + idMapper.mapPerson(person);
        log.info("CriticalActivities query: " + query);

        Set<String> criticalActivities = DLHelper.mapFromOwl(engine.getInstances(query, false));
        log.info("CriticalActivities result: " + criticalActivities);

        return criticalActivities;
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
//        String hasDuty = taskDutyMapper.map(duty);
//
//        String q = Definitions.ACTIVITYINSTANCE + " and " + hasDuty + " exactly 1 " + Definitions.ORGANIZATIONPEOPLE;
////        String q = Definitions.ORGANIZATION + "critical";
//        log.info("Critical activities query:" + q);
//
////        Set<String> criticalActivities = DLHelper.mapClassesFromOwl(engine.getSubClasses(Definitions.ORGANIZATION+"critical", false));
//        Set<String> criticalActivities = DLHelper.mapFromOwl(engine.getInstances(q, false));
//        log.info("Critical activities result: " + criticalActivities);
//
//        return criticalActivities;

        String hasDuty = taskDutyMapper.map(duty);
        Set<String> criticalActivities = new HashSet<String>();

        String query = "( inverse(" + hasDuty + ") some (" + Definitions.ACTIVITYINSTANCE + ")) ";
        query = Definitions.ORGANIZATION + "critical";
        Set<String> potential = DLHelper.mapFromOwl(engine.getInstances(query, false));



        log.info("p: " + potential.size() + "  " + potential);
        for (String p : potential) {
            criticalActivities.addAll(criticalActivities(p, duty));
        }

        return criticalActivities;
    }

	public Set<String> criticalParticipants(Iterable<String> activities, TaskDuty duty) {
        Set<String> criticalActivities = criticalActivities(activities, duty);
        String isPotentialDuty = taskDutyMapper.map(duty);
        List<String> act = new ArrayList<String>();
        for (String a : criticalActivities) {
            act.add(isPotentialDuty + " value " + idMapper.mapActivity(a));
        }

        String query = DLHelper.joinWith(act, " or ");
        log.info("CriticalParticipants: " + query);
        return DLHelper.mapFromOwl(engine.getInstances(query, false));
    }

    public Set<String> indispensableParticipants(Iterable<String> activities, TaskDuty duty) {
        Set<String> mandatoryActivities = mandatoryActivities(activities);
        return criticalParticipants(mandatoryActivities, duty);
    }

    public Set<String> mandatoryActivities(Iterable<String> activities) {
        List<String> act = new ArrayList<String>();
        for (String a : activities) {
            act.add(idMapper.mapActivity(a));
        }

        String activitiesSet = "{" + DLHelper.joinWith(act, ", ") + "}";
        String query = "inverse(" + Definitions.MANDATORY + ") some (inverse(" + Definitions.INITIALACTIVITY + ") some ( " + Definitions.HASACTIVITY + " some " + activitiesSet + "))";
        testQuery(query);

        return DLHelper.mapFromOwl(engine.getInstances(query + " and " + activitiesSet, false));
    }

    public boolean basicConsistency(String activity, TaskDuty duty) {
        OWLReasoner reasoner = engine.getReasoner();

        return reasoner.isConsistent();
    }

}
