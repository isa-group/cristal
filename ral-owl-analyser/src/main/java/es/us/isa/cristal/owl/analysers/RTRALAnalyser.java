package es.us.isa.cristal.owl.analysers;

import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.DLHelper;
import es.us.isa.cristal.owl.DLQueryEngine;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.LogMapper;
import es.us.isa.cristal.owl.mappers.ral.runtime.RTTaskDutyMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.TaskDutyMapper;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static es.us.isa.cristal.owl.Definitions.*;

public class RTRALAnalyser extends AbstractRALAnalyser {

    private static final Logger log = Logger.getLogger(RTRALAnalyser.class.getName());

    private TaskDutyMapper taskDutyMapper;
    private String pid;
    private String currentInstance;

    public RTRALAnalyser(DLQueryEngine engine, IdMapper mapper, String pid) {
		super(engine, mapper);
        taskDutyMapper = new RTTaskDutyMapper();
        this.pid = pid;
        currentInstance = new LogMapper().map(pid);
    }


    private void testQuery(String query) {
        System.out.println(query);
        Set<OWLNamedIndividual> a = engine.getInstances(query, false);
        System.out.println(a);

    }


    @Override
    public Set<String> potentialParticipants(String activityName, TaskDuty duty) {
        String isPotentialDuty =  taskDutyMapper.map(duty);
        String expressionString = isPotentialDuty + " some " + instancesOfActivity(idMapper.mapActivity(activityName));
        log.info("PotentialParticipant: " + expressionString);
        return DLHelper.mapFromOwl(engine.getInstances(expressionString, false));

    }
	
    @Override
    public Set<String> potentialActivities(String personName, TaskDuty duty) {
        String isPotentialDuty = taskDutyMapper.map(duty);
        String expressionString = "inverse(" + ISOFTYPE + ") some ( inverse(" + isPotentialDuty + ") value " + idMapper.mapPerson(personName) + " AND inverse(" + HASACTIVITYINSTANCE + ") value " + currentInstance +")";
        log.info("PotentialActivities: " + expressionString);
        return DLHelper.mapFromOwl(engine.getInstances(expressionString, false));
    }

    @Override
    public Set<String> nonParticipants(Iterable<String> activities, TaskDuty duty) {
        String isPotentialDuty = taskDutyMapper.map(duty);
        List<String> act = new ArrayList<String>();
        for (String a : activities) {
            act.add(idMapper.mapActivity(a));
        }
        String instancesOfActivity = ISOFTYPE + " some ({" + DLHelper.joinWith(act, ",") + "}) AND inverse(" + HASACTIVITYINSTANCE + ") value " + currentInstance;
        String query = Definitions.PERSON + " and not(" + isPotentialDuty + " some (" + instancesOfActivity + "))";

        log.info("NonParticipants: " + query);
        return DLHelper.mapFromOwl(engine.getInstances(query, false));
    }

    @Override
    public Set<String> notInvolved(String personName, TaskDuty duty) {
        throw new NotImplementedException();
    }

    @Override
    public Set<String> permanentParticipants(Iterable<String> activities, TaskDuty duty) {
        String isPotentialDuty = taskDutyMapper.map(duty);
        List<String> act = new ArrayList<String>();
        for (String a : activities) {
            act.add(isPotentialDuty + " some " + instancesOfActivity(idMapper.mapActivity(a)));
        }

        String query = DLHelper.joinWith(act, " and ");
        log.info("PermanentParticipants: " + query);
        return DLHelper.mapFromOwl(engine.getInstances(query, false));
    }

    @Override
    public Set<String> criticalActivities(Iterable<String> activities, TaskDuty duty) {
        String isPotentialDuty = taskDutyMapper.map(duty);

        String atLeastTwo = "(inverse(" + isPotentialDuty + ") min 2 (" + Definitions.PERSON + ")) AND inverse(" + HASACTIVITYINSTANCE + ") value " + currentInstance;
        String alreadyAllocated = "inverse(" + HASACTIVITYINSTANCE + ") value " + currentInstance + " AND " + HASSTATE + " some " + AFTERALLOCATION;
        String nonCriticalQuery = "inverse(" + Definitions.ISOFTYPE + ") some ((" + atLeastTwo + ") OR (" + alreadyAllocated + "))";
        log.info("CriticalActivities: " + nonCriticalQuery);
        Set<String> nonCritical = DLHelper.mapFromOwl(engine.getInstances(nonCriticalQuery, false));
        Set<String> result = new HashSet<String>();
        for (String a : activities) {
            if (!nonCritical.contains(a)) {
                result.add(a);
            }
        }

        return result;
    }

    @Override
    public Set<String> criticalActivities(String personName, TaskDuty duty) {
        throw new NotImplementedException();
    }

    @Override
    public boolean basicConsistency(String activity, TaskDuty duty) {
        String isPotentialDuty = taskDutyMapper.map(duty);
        String act = idMapper.mapActivity(activity);

        String atLeastOne = "(inverse(" + isPotentialDuty + ") min 1 (" + Definitions.PERSON + "))";

/*
        This is an alternative way of obtaining the consistency to avoid open world assumption if the log ontology
        is not closed

        Set<String> activities = DLHelper.mapFromOwl(engine.getInstances(instancesOfActivity(idMapper.mapActivity(activity)), false));
        List<String> activ = new ArrayList<String>();
        LogMapper m = new LogMapper();
        for (String a : activities) {
            activ.add(m.map(a));
        }
        String instancesOfActivity = "{"+DLHelper.joinWith(activ, ",")+"}";
*/

        String instancesOfActivity = instancesOfActivity(act);
        String axiomString = "("+instancesOfActivity + ") SubClassOf: (" + atLeastOne+")";
        log.info("BasicConsistency: " + axiomString);
        return engine.isEntailed(axiomString);
    }

    private String instancesOfActivity(String act) {
        return "(" + Definitions.ISOFTYPE + " value " + act + " AND inverse(" + HASACTIVITYINSTANCE + ") value " + currentInstance + ")";
    }

    @Override
    public Set<String> criticalParticipants(Iterable<String> activities, TaskDuty duty) {
        Set<String> criticalActivities = criticalActivities(activities, duty);
        String isPotentialDuty = taskDutyMapper.map(duty);
        List<String> act = new ArrayList<String>();
        for (String a : criticalActivities) {
            act.add(isPotentialDuty + " some " + instancesOfActivity(a));
        }

        String query = DLHelper.joinWith(act, " or ");
        log.info("CriticalParticipants: " + query);
        return DLHelper.mapFromOwl(engine.getInstances(query, false));
    }

    @Override
    public Set<String> indispensableParticipants(Iterable<String> activities, TaskDuty duty) {
        Set<String> mandatoryActivities = mandatoryActivities(activities);
        return criticalParticipants(mandatoryActivities, duty);
    }

    private Set<String> mandatoryActivities(Iterable<String> activities) {
        List<String> act = new ArrayList<String>();
        for (String a : activities) {
            act.add(idMapper.mapActivity(a));
        }

        String activitiesSet = "{" + DLHelper.joinWith(act, ", ") + "}";
        String mandatories = "(inverse(" + Definitions.MANDATORY + ") some (inverse(" + Definitions.ISOFTYPE + ") some ( " + Definitions.HASACTIVITYINSTANCE + " value " + currentInstance + " AND " + Definitions.HASSTATE + " some " + Definitions.STARTSTATE + ")))";

        String query = mandatories + " and " + activitiesSet;
        testQuery(query);

        return DLHelper.mapFromOwl(engine.getInstances(query, false));
    }

}
