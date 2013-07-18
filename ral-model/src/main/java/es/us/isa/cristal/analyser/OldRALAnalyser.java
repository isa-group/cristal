package es.us.isa.cristal.analyser;

import java.util.List;

@Deprecated
public interface OldRALAnalyser {
	
	// obtiene los usuarios candidatos para una instancia de proceso y una actividad dadas
	List<String> getCandidateUsers(String processInstanceId, String activityId);

	// Is there any person that could be allocated to all the activities of an instance of the BP? 
	List<String> SomePersonToAllActivities(String processInstanceId, List<String> activities);
	
	// Have activity A and B the same potential performers?
	Boolean SamePotentialPerformers(String processInstanceId, String activityIdA, String activityIdB);
	
	// Can all the potential performers of an activity A be allocated also to activity B?
	Boolean AllPerformersAllocatedAtB(String processInstanceId, String activityIdA, String activityIdB);
	
	// Is there anybody in the company that never participates in the BP?
	List<String> SomePersonNeverParticipating(String processInstanceId, List<String> activities);
	
	// Is there anybody necessary in the BP?
	List<String> SomebodyIndispensable(String processInstanceId, List<String> activities);

	List<String> AllActivitiesByPerson(String processInstanceId, String personId, List<String> activities);
	
}
