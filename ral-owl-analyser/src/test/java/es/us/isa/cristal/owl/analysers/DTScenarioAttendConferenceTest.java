package es.us.isa.cristal.owl.analysers;

import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.AttendConferenceScenario;
import es.us.isa.cristal.owl.RALOntologyManager;
import es.us.isa.cristal.owl.mappers.LogOntologyManager;
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
public class DTScenarioAttendConferenceTest {
    private RALOntologyManager manager;



    @Before
    public void setup() {
        manager = new AttendConferenceScenario().getRalOntologyManager();

        manager.addParticipant("SubmitCameraReady", RALParser.parse("HAS POSITION PhdStudent"));
        manager.addParticipant("MakeReservations", RALParser.parse("HAS ROLE Clerk"));
        manager.addParticipant("FillTravelAuthorization", RALParser.parse("REPORTS TO POSITION ProjectCoordinator"));

        manager.logProcessInstance("AttendConference", "ac1")
                .activity("SubmitCameraReady", "scr1", LogOntologyManager.ActivityState.COMPLETED, "Adela")
                .activity("FillTravelAuthorization", "fta1", LogOntologyManager.ActivityState.READY);

    }

    @Test
    public void shouldGetPotentialPerformersOfSubmitCameraReady() {
        RALAnalyser analyser = manager.createDesignTimeAnalyser();
        Set<String> submitCameraReady = analyser.potentialParticipants("SubmitCameraReady", TaskDuty.PARTICIPANT);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Cristina", "Adela")), submitCameraReady);
        System.out.println(submitCameraReady);
    }


    @Test
    public void shouldGetPotentialPerformersOfFill() {
        RALAnalyser analyser = manager.createDesignTimeAnalyser();
        Set<String> submitCameraReady = analyser.potentialParticipants("FillTravelAuthorization", TaskDuty.PARTICIPANT);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Adela", "Sergio", "Antonio", "Manuel", "Beatriz", "Ana", "Cristina")), submitCameraReady);
        System.out.println(submitCameraReady);
    }

    @Test
    public void shouldGetPotentialActivitiesOfCristina() {
        RALAnalyser analyser = manager.createDesignTimeAnalyser();
        Set<String> results = analyser.potentialActivities("Cristina", TaskDuty.RESPONSIBLE);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("SubmitCameraReady", "FillTravelAuthorization")), results);
        System.out.println(results);

    }

    @Test
    public void shouldGetNonPerformers() {
        RALAnalyser analyser = manager.createDesignTimeAnalyser();
        Set<String> results = analyser.nonParticipants(Arrays.asList("SubmitCameraReady", "MakeReservations"), TaskDuty.RESPONSIBLE);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Antonio", "Manuel", "Sergio", "Beatriz")), results);
        System.out.println(results);

    }

    @Test
    public void shouldGetPermanentParticipants() {
        RALAnalyser analyser = manager.createDesignTimeAnalyser();
        Set<String> results = analyser.permanentParticipants(Arrays.asList("SubmitCameraReady", "FillTravelAuthorization"), TaskDuty.RESPONSIBLE);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Cristina", "Adela")), results);
        System.out.println(results);

    }

    @Test
    public void shouldGetCriticalActivities() {
        RALAnalyser analyser = manager.createDesignTimeAnalyser();
        Set<String> results = analyser.criticalActivities(Arrays.asList("SubmitCameraReady", "FillTravelAuthorization", "MakeReservations"), TaskDuty.RESPONSIBLE);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("MakeReservations")), results);
        System.out.println(results);

    }

    @Test
    public void shouldCheckConsistencyOfSubmit() {
        DTRALAnalyser analyser = manager.createDesignTimeAnalyser();
        boolean result = analyser.basicConsistency("SubmitCameraReady", TaskDuty.RESPONSIBLE);

        Assert.assertTrue(result);
    }

    @Test
    public void shouldCheckConsistencyOf() {
        manager = new AttendConferenceScenario().getRalOntologyManager();
        manager.addParticipant("SendTravelAuthorization", RALParser.parse("REPORTS TO POSITION AdministrativeAssistant DIRECTLY"), TaskDuty.RESPONSIBLE);
        DTRALAnalyser analyser = manager.createDesignTimeAnalyser();
        boolean result = analyser.basicConsistency("SendTravelAuthorization", TaskDuty.RESPONSIBLE);

        Assert.assertFalse(result);
    }
}
