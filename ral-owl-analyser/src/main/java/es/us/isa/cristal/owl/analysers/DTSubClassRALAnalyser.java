package es.us.isa.cristal.owl.analysers;

import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.DLHelper;
import es.us.isa.cristal.owl.DLQueryEngine;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.InstanceTaskDutyMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.TaskDutyMapper;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class DTSubClassRALAnalyser extends AbstractRALAnalyser {

    private static final Logger log = Logger.getLogger(DTSubClassRALAnalyser.class.getName());

    private TaskDutyMapper taskDutyMapper;

    public DTSubClassRALAnalyser(DLQueryEngine engine, IdMapper mapper) {
		super(engine, mapper);
        taskDutyMapper = new InstanceTaskDutyMapper();
    }

    private void testQuery(String query) {
        System.out.println(query);
        Set<OWLNamedIndividual> a = engine.getInstances(query, false);
        System.out.println(a);

    }


    @Override
    public Set<String> potentialParticipants(String activityName, TaskDuty duty) {
        String hasDuty =  taskDutyMapper.map(duty);
        String expressionString = Definitions.PERSON + " and not (inverse("+hasDuty + ") some (" + Definitions.ISOFTYPE + " value " + idMapper.mapActivity(activityName)+"))";
        log.info("PotentialParticipant: " + expressionString);
        Set<String> notParticipants = DLHelper.mapFromOwl(engine.getInstances(expressionString, false));
        log.info("Not potential participants: " + notParticipants);

        Set<String> participants = DLHelper.mapFromOwl(engine.getInstances(Definitions.ORGANIZATIONPEOPLE, false));
        participants.removeAll(notParticipants);
        return participants;
    }
	
    @Override
    public Set<String> potentialActivities(String personName, TaskDuty duty) {
        String isPotentialDuty = taskDutyMapper.map(duty);
        String expressionString = Definitions.ACTIVITYINSTANCE + " and not("+isPotentialDuty+" value "+ idMapper.mapPerson(personName)+")";
        log.info("PotentialActivities: " + expressionString);

        Set<String> notActivities = DLHelper.mapClassesFromOwl(engine.getSubClasses(expressionString, false));
        log.info("Not potential activities: " + notActivities);

        Set<String> activities = DLHelper.mapClassesFromOwl(engine.getSubClasses(Definitions.ACTIVITYINSTANCE, false));
        activities.removeAll(notActivities);

        return activities;
    }
	
	public Set<String> nonParticipants(Iterable<String> activities, TaskDuty duty) {
        String hasDuty =  taskDutyMapper.map(duty);
        List<String> act = new ArrayList<String>();
        for (String a : activities) {
            act.add(idMapper.mapActivity(a));
        }

        String expressionString = Definitions.PERSON + " and not (inverse("+hasDuty + ") some (" + Definitions.ISOFTYPE + " some { " + DLHelper.joinWith(act, ",") +"}))";
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

        Set<String> participants = DLHelper.mapFromOwl(engine.getInstances(Definitions.ORGANIZATIONPEOPLE, false));
        participants.removeAll(notParticipants);

        return participants;
    }

    public Set<String> criticalActivities(String person, TaskDuty duty) {
        String hasDuty = taskDutyMapper.map(duty);

        String query = hasDuty + " value " + idMapper.mapPerson(person);
        log.info("CriticalActivities query: " + query);

        Set<String> criticalActivities = DLHelper.mapClassesFromOwl(engine.getSubClasses(query, false));
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
        String hasDuty = taskDutyMapper.map(duty);

        log.info("ca: " + criticalActivities("Ana", duty));
        log.info("ca: " + criticalActivities("Adela", duty));
        log.info("ni: " + notInvolved("Adela", duty));
        log.info("ni: " + notInvolved("Ana", duty));

        String q = Definitions.ACTIVITYINSTANCE + " and " + hasDuty + " exactly 1 " + Definitions.ORGANIZATIONPEOPLE + "";
        log.info("Critical activities query:" + q);

        Set<String> criticalActivities = DLHelper.mapClassesFromOwl(engine.getSubClasses(q, false));
        log.info("Critical activities result: " + criticalActivities);

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
//        String query = "organization:Person and not (organization:occupies some (organization:reportsTo some ({organization-isa:AdministrativeAssistant})))";
//        testQuery(query);


        Set<String> inconsistent = DLHelper.mapClassesFromOwl(engine.getEquivalentClasses(DLQueryEngine.NOTHING));
        log.info("Inconsistent: " + inconsistent);
        return inconsistent.isEmpty();
    }

}
