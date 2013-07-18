package es.us.isa.cristal.owl.analysers;

import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.DLHelper;
import es.us.isa.cristal.owl.DLQueryEngine;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.mappers.ral.misc.DTTaskDutyMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.TaskDutyMapper;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class DTRALAnalyser extends AbstractRALAnalyser {

    private static final Logger log = Logger.getLogger(DTRALAnalyser.class.getName());

    private TaskDutyMapper taskDutyMapper;

    public DTRALAnalyser(DLQueryEngine engine, IdMapper mapper) {
		super(engine, mapper);
        taskDutyMapper = new DTTaskDutyMapper();
    }

    private void testQuery(String query) {
        System.out.println(query);
        Set<OWLNamedIndividual> a = engine.getInstances(query, false);
        System.out.println(a);

    }


    @Override
    public Set<String> potentialParticipants(String activityName, TaskDuty duty) {
        String isPotentialDuty =  taskDutyMapper.map(duty);
        String expressionString = isPotentialDuty + " value " + idMapper.mapActivity(activityName);
        log.info("PotentialParticipant: " + expressionString);
        return DLHelper.mapFromOwl(engine.getInstances(expressionString, false));

    }
	
    @Override
    public Set<String> potentialActivities(String personName, TaskDuty duty) {
        String isPotentialDuty = taskDutyMapper.map(duty);
        String expressionString = "inverse(" + isPotentialDuty + ") value " + idMapper.mapPerson(personName);
        log.info("PotentialActivities: " + expressionString);
        return DLHelper.mapFromOwl(engine.getInstances(expressionString, false));
    }
	
	public Set<String> nonParticipants(Iterable<String> activities, TaskDuty duty) {
        String isPotentialDuty = taskDutyMapper.map(duty);
        List<String> act = new ArrayList<String>();
        for (String a : activities) {
            act.add(idMapper.mapActivity(a));
        }

        String query = Definitions.PERSON +" and not(" + isPotentialDuty + " some {" + DLHelper.joinWith(act, ",") + "})";
        log.info("NonParticipants: " + query);
        return DLHelper.mapFromOwl(engine.getInstances(query, false));
    }

    public Set<String> permanentParticipants(Iterable<String> activities, TaskDuty duty) {
        String isPotentialDuty = taskDutyMapper.map(duty);
        List<String> act = new ArrayList<String>();
        for (String a : activities) {
            act.add(isPotentialDuty + " value " + idMapper.mapActivity(a));
        }

        String query = DLHelper.joinWith(act, " and ");
        log.info("PermanentParticipants: " + query);
        return DLHelper.mapFromOwl(engine.getInstances(query, false));
    }

    public Set<String> criticalActivities(Iterable<String> activities, TaskDuty duty) {
        String isPotentialDuty = taskDutyMapper.map(duty);

        String query = "(inverse(" + isPotentialDuty + ") min 2 (" + Definitions.PERSON + "))";
        log.info("CriticalActivities: " + query);
        Set<String> nonCritical = DLHelper.mapFromOwl(engine.getInstances(query, false));
        Set<String> result = new HashSet<String>();
        for (String a : activities) {
            if (!nonCritical.contains(a)) {
                result.add(a);
            }
        }

        return result;
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
        String isPotentialDuty = taskDutyMapper.map(duty);
        String act = idMapper.mapActivity(activity);

        String atLeastOne = "(inverse(" + isPotentialDuty + ") min 1 (" + Definitions.PERSON + "))";
        String query = "({" + act + "}) and " + atLeastOne;
//        System.out.println(engine.isSatisfiable(query));
//
//        System.out.println(engine.isEntailed("({" + act + "}) SubClassOf: " + atLeastOne));
//        return engine.isSatisfiable(isPotentialDuty + " value " + act);

        return engine.isEntailed("({" + act + "}) SubClassOf: " + atLeastOne);
    }

}
