package es.us.isa.cristal.owl.analysers;

import es.us.isa.cristal.ResourceAssignment;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.AttendConferenceScenario;
import es.us.isa.cristal.owl.RALOntologyManager;
import es.us.isa.cristal.owl.ontologyhandlers.LogOntologyHandler;
import es.us.isa.cristal.parser.RALParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * User: resinas
 * Date: 04/07/13
 * Time: 17:05
 */
public class RTScenarioAttendConferenceTest {
    private RALOntologyManager manager;
    private String pid;



    @Before
    public void setup() {
        manager = new AttendConferenceScenario().getRalOntologyManager();
        pid = "ac1";

        ResourceAssignment assignment = new ResourceAssignment().
                add("SubmitCameraReady", RALParser.parse("HAS POSITION PhdStudent")).
                add("FillTravelAuthorization", RALParser.parse("REPORTS TO POSITION ProjectCoordinator")).
                add("MakeReservations", RALParser.parse("HAS ROLE Clerk")).
                add("RegisterAtConference", RALParser.parse("IS PERSON WHO DID ACTIVITY SubmitCameraReady"));

        manager.loadResourceAssignment(assignment);
    }

    private void loadPredefinedLog() {
        manager.logProcessInstance("AttendConference", pid)
                .activity("SubmitCameraReady", "scr1", LogOntologyHandler.ActivityState.COMPLETED, "Adela")
                .activity("FillTravelAuthorization", "fta1", LogOntologyHandler.ActivityState.READY);
    }

    @Test
    public void shouldGetPotentialPerformersOfSubmitCameraReady() {
        loadPredefinedLog();

        RALAnalyser analyser = manager.createRunTimeAnalyser(pid);
        Set<String> submitCameraReady = analyser.potentialParticipants("SubmitCameraReady", TaskDuty.RESPONSIBLE);
        Assert.assertEquals(new HashSet<String>(Arrays.asList("Adela")), submitCameraReady);
        System.out.println(submitCameraReady);
    }


    @Test
    public void shouldGetPotentialPerformersOfFill() {
        loadPredefinedLog();

        RALAnalyser analyser = manager.createRunTimeAnalyser(pid);
        Set<String> fillTravelAuthorization = analyser.potentialParticipants("FillTravelAuthorization", TaskDuty.PARTICIPANT);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Adela", "Sergio", "Antonio", "Manuel", "Beatriz", "Ana", "Cristina")), fillTravelAuthorization);
        System.out.println(fillTravelAuthorization);
    }

    @Test
    public void shouldGetPotentialPerformersOfRegister() {
        loadPredefinedLog();

        RALAnalyser analyser = manager.createRunTimeAnalyser(pid);
        Set<String> registerAtConference = analyser.potentialParticipants("RegisterAtConference", TaskDuty.PARTICIPANT);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Adela")), registerAtConference);
        System.out.println(registerAtConference);
    }

    @Test
    public void shouldGetPotentialPerformersOfRegisterWithoutLog() {
        manager.logProcessInstance("AttendConference", pid)
                .activity("SubmitCameraReady", "scr1", LogOntologyHandler.ActivityState.READY);

        RALAnalyser analyser = manager.createRunTimeAnalyser(pid);
        Set<String> registerAtConference = analyser.potentialParticipants("RegisterAtConference", TaskDuty.PARTICIPANT);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Adela", "Cristina")), registerAtConference);
        System.out.println(registerAtConference);
    }

    @Test
    public void shouldGetPotentialActivitiesOfCristina() {
        loadPredefinedLog();

        RALAnalyser analyser = manager.createRunTimeAnalyser(pid);
        Set<String> results = analyser.potentialActivities("Cristina", TaskDuty.RESPONSIBLE);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("FillTravelAuthorization")), results);
        System.out.println(results);

    }

    @Test
    public void shouldGetNonPerformers() {
        loadPredefinedLog();

        RALAnalyser analyser = manager.createRunTimeAnalyser(pid);
        Set<String> results = analyser.nonParticipants(Arrays.asList("SubmitCameraReady", "MakeReservations"), TaskDuty.RESPONSIBLE);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Antonio", "Cristina", "Manuel", "Sergio", "Beatriz")), results);
        System.out.println(results);
    }

    @Test
    public void shouldGetPermanentParticipants() {
        loadPredefinedLog();

        RALAnalyser analyser = manager.createRunTimeAnalyser(pid);
        Set<String> results = analyser.permanentParticipants(Arrays.asList("SubmitCameraReady", "FillTravelAuthorization"), TaskDuty.RESPONSIBLE);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Adela")), results);
        System.out.println(results);

    }

    @Test
    public void shouldGetCriticalActivities() {
        loadPredefinedLog();

        RALAnalyser analyser = manager.createRunTimeAnalyser(pid);
        Set<String> results = analyser.criticalActivities(Arrays.asList("SubmitCameraReady", "FillTravelAuthorization", "MakeReservations"), TaskDuty.RESPONSIBLE);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("MakeReservations")), results);
        System.out.println(results);

    }

    @Test
    public void shouldCheckConsistencyOfSubmit() {
        loadPredefinedLog();

        RALAnalyser analyser = manager.createRunTimeAnalyser(pid);
        boolean result = analyser.basicConsistency("SubmitCameraReady", TaskDuty.RESPONSIBLE);

        Assert.assertTrue(result);
    }

    @Test
    public void shouldCheckConsistencyOf() {
        manager = new AttendConferenceScenario().getRalOntologyManager();
        manager.loadResourceAssignment(new ResourceAssignment().add("SendTravelAuthorization", TaskDuty.RESPONSIBLE, RALParser.parse("REPORTS TO POSITION AdministrativeAssistant DIRECTLY")));
        loadPredefinedLog();

        RALAnalyser analyser = manager.createRunTimeAnalyser(pid);
        boolean result = analyser.basicConsistency("SendTravelAuthorization", TaskDuty.RESPONSIBLE);

        Assert.assertFalse(result);
    }


}
