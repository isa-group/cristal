package es.us.isa.cristal.analyser;

import es.us.isa.cristal.model.TaskDuty;

import java.util.Set;

/**
 * User: resinas
 * Date: 18/07/13
 * Time: 13:24
 */
public interface RALAnalyser {
    Set<String> potentialParticipants(String activityName, TaskDuty duty);

    Set<String> potentialActivities(String personName, TaskDuty duty);

    boolean basicConsistency(String activity, TaskDuty duty);

    Set<String> nonParticipants(Iterable<String> activities, TaskDuty duty);

    Set<String> permanentParticipants(Iterable<String> activities, TaskDuty duty);

    Set<String> criticalActivities(Iterable<String> activities, TaskDuty duty);

    Set<String> criticalParticipants(Iterable<String> activities, TaskDuty duty);

    Set<String> indispensableParticipants(Iterable<String> activities, TaskDuty duty);
}
