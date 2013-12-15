package es.us.isa.cristal.owl.analysers;

import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.AttendConferenceScenario;
import es.us.isa.cristal.owl.DLQueryEngine;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.RALOntologyManager;
import es.us.isa.cristal.owl.mappers.LogOntologyManager;
import es.us.isa.cristal.parser.RALParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static es.us.isa.cristal.owl.Definitions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * User: resinas
 * Date: 04/07/13
 * Time: 17:05
 */
public class ACScenarioAttendConferenceTest {
    private RALOntologyManager manager;
    private String pid;



    @Before
    public void setup() {
        manager = new AttendConferenceScenario().getRalOntologyManager();
        pid = "ac1";

        manager.addParticipant("SignTravelAuthorization", RALParser.parse("HAS POSITION PhdStudent"));
//        manager.addParticipant("FillTravelAuthorization", RALParser.parse("REPORTS TO POSITION ProjectCoordinator"));
        manager.addParticipant("MakeReservations", RALParser.parse("HAS ROLE Clerk"));

    }

    private void loadPredefinedLog() {
        manager.logProcessInstance("AttendConference", pid)
                .activity("SignTravelAuthorization", "scr1", LogOntologyManager.ActivityState.COMPLETED, "Adela")
                .activity("FillTravelAuthorization", "fta1", LogOntologyManager.ActivityState.READY)
                .activity("RegisterAtConference", "rat1", LogOntologyManager.ActivityState.READY);
    }

    @Test
    public void shouldGetPotentialPerformersOfRegisterAtConferenceReady() {
        manager.addParticipant("RegisterAtConference", RALParser.parse("IS PERSON WHO DID ACTIVITY SignTravelAuthorization"));
        manager.logProcessInstance("AttendConference", pid)
                .activity("SignTravelAuthorization", "scr1", LogOntologyManager.ActivityState.COMPLETED, "Adela")
                .activity("SignTravelAuthorization", "scr2", LogOntologyManager.ActivityState.READY)
                .activity("FillTravelAuthorization", "fta1", LogOntologyManager.ActivityState.READY)
                .activity("RegisterAtConference", "rat1", LogOntologyManager.ActivityState.READY);

        RALAnalyser analyser = manager.createRunTimeAnalyser(pid);
        Set<String> SignTravelAuthorization = analyser.potentialParticipants("RegisterAtConference", TaskDuty.RESPONSIBLE);
        Assert.assertEquals(new HashSet<String>(Arrays.asList("Adela")), SignTravelAuthorization);
        System.out.println(SignTravelAuthorization);
    }

    @Test
    public void shouldGetPotentialPerformersOfRegisterAtConferenceFuture() {
        manager.addParticipant("RegisterAtConference", RALParser.parse("IS PERSON WHO DID ACTIVITY SignTravelAuthorization"));
        loadInFutureLog();

        RALAnalyser analyser = manager.createRunTimeAnalyser(pid);
        Set<String> SignTravelAuthorization = analyser.potentialParticipants("RegisterAtConference", TaskDuty.RESPONSIBLE);
        Assert.assertEquals(new HashSet<String>(Arrays.asList("Adela", "Cristina")), SignTravelAuthorization);
        System.out.println(SignTravelAuthorization);
    }

    private void loadInFutureLog() {
        manager.logProcessInstance("AttendConference", pid)
                .activity("SignTravelAuthorization", "scr1", LogOntologyManager.ActivityState.COMPLETED, "Adela")
                .activity("SignTravelAuthorization", "scr2", LogOntologyManager.ActivityState.READY)
                .activity("FillTravelAuthorization", "fta1", LogOntologyManager.ActivityState.READY);
    }

    @Test
    public void shouldGetPotentialPerformersOfRegisterAtConferenceReadyWithNot() {
        manager.addParticipant("RegisterAtConference", RALParser.parse("NOT IS PERSON WHO DID ACTIVITY SignTravelAuthorization"));
        manager.logProcessInstance("AttendConference", pid)
                .activity("SignTravelAuthorization", "scr1", LogOntologyManager.ActivityState.COMPLETED, "Adela")
                .activity("SignTravelAuthorization", "scr2", LogOntologyManager.ActivityState.READY)
                .activity("FillTravelAuthorization", "fta1", LogOntologyManager.ActivityState.READY)
                .activity("RegisterAtConference", "rat1", LogOntologyManager.ActivityState.READY);

        RALAnalyser analyser = manager.createRunTimeAnalyser(pid);
        Set<String> signTravelAuthorization = analyser.potentialParticipants("RegisterAtConference", TaskDuty.RESPONSIBLE);
        System.out.println(signTravelAuthorization);
        DLQueryEngine engine = manager.createDLQueryEngine(manager.getRunTimeOntology());
        System.out.println(engine.getInstances(ACTIVITYINSTANCE + " and not(inverse(" + HASACTIVITYINSTANCE + ") value log:ac1 AND " + ISOFTYPE + " value bp-attend-conference:SignTravelAuthorization AND " + HASSTATE + " some " + AFTERALLOCATION+")", true));
        System.out.println(engine.isEntailed("{log:scr1} EquivalentTo: (inverse(" + HASACTIVITYINSTANCE + ") value log:ac1 AND " + ISOFTYPE + " value bp-attend-conference:SignTravelAuthorization AND " + HASSTATE + " some " + AFTERALLOCATION + ")"));
        System.out.println(engine.getInstances(Definitions.PERSON + " and not(inverse(" + HASRESPONSIBLE + ") some (inverse(" + HASACTIVITYINSTANCE + ") value log:ac1 AND " + ISOFTYPE + " value bp-attend-conference:SignTravelAuthorization AND " + HASSTATE + " some " + AFTERALLOCATION + "))", true));
        Assert.assertEquals(new HashSet<String>(Arrays.asList("Sergio", "Antonio", "Manuel", "Beatriz", "Ana", "Cristina")), signTravelAuthorization);
    }

    @Test
    public void shouldGetPotentialPerformersOfRegisterAtConferenceFutureWithNot() {
        manager.addParticipant("RegisterAtConference", RALParser.parse("NOT IS PERSON WHO DID ACTIVITY SignTravelAuthorization"));
        manager.logProcessInstance("AttendConference", pid)
                .activity("SignTravelAuthorization", "scr1", LogOntologyManager.ActivityState.COMPLETED, "Adela")
                .activity("SignTravelAuthorization", "scr2", LogOntologyManager.ActivityState.READY)
                .activity("FillTravelAuthorization", "fta1", LogOntologyManager.ActivityState.READY);

        RALAnalyser analyser = manager.createRunTimeAnalyser(pid);
        Set<String> signTravelAuthorization = analyser.potentialParticipants("RegisterAtConference", TaskDuty.RESPONSIBLE);
        Assert.assertEquals(new HashSet<String>(Arrays.asList("Sergio", "Antonio", "Manuel", "Beatriz", "Ana", "Cristina")), signTravelAuthorization);
        System.out.println(signTravelAuthorization);
    }

    @Test
    public void bodInverted() {
        manager.addParticipant("FillTravelAuthorization", RALParser.parse("(HAS POSITION PhdStudent) AND (IS PERSON WHO DID ACTIVITY SignTravelAuthorization)"));
        manager.logProcessInstance("AttendConference", pid)
                .activity("SubmitCameraReady", "scr1", LogOntologyManager.ActivityState.READY);

        RALAnalyser analyser = manager.createRunTimeAnalyser(pid);
        Set<String> signTravelAuthorization = analyser.potentialParticipants("FillTravelAuthorization", TaskDuty.RESPONSIBLE);
        Assert.assertEquals(new HashSet<String>(Arrays.asList("Adela", "Cristina")), signTravelAuthorization);
        System.out.println(signTravelAuthorization);
    }

    @Test
    public void sodInverted() {
        manager.addParticipant("SubmitCameraReady", RALParser.parse("NOT IS PERSON WHO DID ACTIVITY SignTravelAuthorization"));
        manager.logProcessInstance("AttendConference", pid)
                .activity("SubmitCameraReady", "scr1", LogOntologyManager.ActivityState.READY);

        RALAnalyser analyser = manager.createDesignTimeAnalyser();
        Set<String> signTravelAuthorization = analyser.potentialParticipants("SubmitCameraReady", TaskDuty.RESPONSIBLE);
        Assert.assertEquals(new HashSet<String>(Arrays.asList("Sergio", "Antonio", "Manuel", "Beatriz", "Ana", "Adela", "Cristina")), signTravelAuthorization);
        System.out.println(signTravelAuthorization);
    }
}
