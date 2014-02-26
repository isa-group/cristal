package es.us.isa.cristal.owl.mappers.ral.runtime;

import es.us.isa.cristal.ResourceAssignment;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.AttendConferenceScenario;
import es.us.isa.cristal.owl.RALOntologyManager;
import es.us.isa.cristal.owl.ontologyhandlers.AssignmentOntology;
import es.us.isa.cristal.owl.ontologyhandlers.LogOntologyHandler;
import es.us.isa.cristal.parser.RALParser;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 * User: resinas
 * Date: 13/07/13
 * Time: 13:05
 */
public class RTAssignmentOntologyTest {
    private AttendConferenceScenario scenario;
    private RALOntologyManager manager;
    private String pid;

    @Before
    public void setup() {
        pid = "ac1";
        scenario = new AttendConferenceScenario();
        manager = scenario.getRalOntologyManager();
    }


    @Test
    public void shouldAddAfterAllocationParticipant() {
        manager.logProcessInstance("AttendConference", pid)
                .activity("SubmitCameraReady", "scr1", LogOntologyHandler.ActivityState.COMPLETED, "Adela")
                .activity("FillTravelAuthorization", "fta1", LogOntologyHandler.ActivityState.READY);

        ResourceAssignment assignment = new ResourceAssignment();
        assignment.add("SubmitCameraReady", TaskDuty.RESPONSIBLE, RALParser.parse("HAS POSITION PhdStudent"));
        assignment.add("FillTravelAuthorization", TaskDuty.RESPONSIBLE, RALParser.parse("REPORTS TO POSITION ProjectCoordinator"));
        assignment.add("MakeReservations", TaskDuty.RESPONSIBLE, RALParser.parse("HAS ROLE Clerk"));

        manager.loadResourceAssignment(assignment);

        AssignmentOntology rtAssignmentOntology = manager.getRuntimeAssignmentOntology(pid);

        try {
            rtAssignmentOntology.getOntology().getOWLOntologyManager().saveOntology(rtAssignmentOntology.getOntology(), new StreamDocumentTarget(System.out));
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }


    }

}
