package es.us.isa.cristal.analyser;

import es.us.isa.cristal.model.TaskDuty;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: resinas
 * Date: 18/07/13
 * Time: 13:24
 */
public interface RALAnalyser {
    /**
     *  The Potential Participants operation calculates the resources that are candidate
     *  to perform a specific task duty for one activity, e.g., the individuals that can
     *  potentially become responsible for the activity indicated.
     * @param activityName an activity of the process
     * @param duty a specific task duty
     * @return the set of people of the organisation that meet the conditions set in the
     * assignment of such a task duty for the activity specified.
     */
    Set<String> potentialParticipants(String activityName, TaskDuty duty);

    /**
     * The Potential Activities operation is opposite to Potential Participants, that is,
     * it lists the activities that may be allocated to one resource with regard to a
     * specific task duty during a process instance execution.
     * @param personName a specific person in the organization.
     * @param duty a specific task duty
     * @return the activities that can be potentially allocated to this person regarding
     * that task duty.
     */
    Set<String> potentialActivities(String personName, TaskDuty duty);

    /**
     * An activity is consistent with regard to a task duty if there is at least one
     * person that is allowed to perform the task duty for the activity, according to the
     * resource expression associated to the task duty.
     * @param activity an activity of the process
     * @param duty a specific task duty
     * @return whether the activity is consistent with regard to the task duty.
     */
    boolean basicConsistency(String activity, TaskDuty duty);

    /**
     * The Non-participants operation checks if there is any person that never
     * participates in a set of activities for a specific task duty.
     * @param activities a collection of activities of a business process.
     * @param duty a specific task duty.
     * @return the set of people that can never participate in the activities performing
     * such a task duty, if any.
     */
    Set<String> nonParticipants(Iterable<String> activities, TaskDuty duty);

    /**
     * The Not Involved operation calculates the activities in which a resource cannot be
     * involved according to the resource assignments and the organisational model.
     * @param personName a a specific person in the organization.
     * @param duty a specific task duty.
     * @return the set of activities of a business process to whose set of potential
     * performers for that task duty the person in question does not belong, if any.
     */
    Set<String> notInvolved(String personName, TaskDuty duty);

    @Deprecated
    Set<String> permanentParticipants(Iterable<String> activities, TaskDuty duty);

    /**
     * An activity is a critical activity for a given task duty if it has only one potential
     * performer for such a task duty, which in turn is called critical participant. This
     * operation returns which activities are critical for a specific task duty.
     * @param activities a collection of activities of a business process.
     * @param duty a specific task duty.
     * @return the subset of activities that are critical with regard to the given task
     * duty.
     */
    Set<String> criticalActivities(Iterable<String> activities, TaskDuty duty);

    /**
     * This operation identifies the critical parts of the process when a resource is
     * not available for a specific (or indefinite) period of time, e.g. due to illness.
     * The outcome specifies the points where the process could get blocked and, thus,
     * those activities whose resource assignments should be modified temporally or
     * permanently.
     * @param personName a specific person in the organization.
     * @param duty a specific task duty.
     * @return the critical activities where that person is involved with the given task
     * duty.
     */
    Set<String> criticalActivities(String personName, TaskDuty duty);

    Set<String> criticalParticipants(Iterable<String> activities, TaskDuty duty);

    /**
     * A person is an indispensable participant if it is the critical participant of a
     * mandatory activity, i.e., an activity that will take place before the business
     * process ends. Therefore, the indispensable participants are those people without
     * whom the business process always gets blocked.
     * @param activities a collection of activities of a business process.
     * @param duty a specific task duty.
     * @return the indispensable participants for the given activities with regard to
     * the chosen task duty.
     */
    Set<String> indispensableParticipants(Iterable<String> activities, TaskDuty duty);
}
