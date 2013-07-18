package es.us.isa.cristal.owl.assignments;

import es.us.isa.cristal.ResourceAssignment;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.AttendConferenceScenario;
import es.us.isa.cristal.owl.RALOntologyManager;
import es.us.isa.cristal.owl.mappers.LogOntologyManager;
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
    private RTAssignmentOntology rtAssignmentOntology;
    private String pid;

    @Before
    public void setup() {
        pid = "ac1";
        scenario = new AttendConferenceScenario();
        manager = scenario.getRalOntologyManager();

//        manager.addParticipant("SubmitCameraReady", RALParser.parse("HAS POSITION PhdStudent"));
//        manager.addParticipant("MakeReservations", RALParser.parse("HAS ROLE Clerk"));
//        manager.addParticipant("FillTravelAuthorization", RALParser.parse("REPORTS TO POSITION ProjectCoordinator"));
    }

    private RTAssignmentOntology createRtAssignmentOntology() {
        return new RTAssignmentOntology(manager, pid, RALOntologyManager.RTASSIGNMENT_IRI, manager.getLogOntologyManager(), scenario.getIdMapper(), scenario.getBpEngine());
    }

    @Test
    public void shouldImportLogOntology() {
        rtAssignmentOntology = createRtAssignmentOntology();
        try {
            manager.getManager().saveOntology(rtAssignmentOntology.getOntology(), new StreamDocumentTarget(System.out));
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void shouldAddAfterAllocationParticipant() {
        manager.logProcessInstance("AttendConference", pid)
                .activity("SubmitCameraReady", "scr1", LogOntologyManager.ActivityState.COMPLETED, "Adela")
                .activity("FillTravelAuthorization", "fta1", LogOntologyManager.ActivityState.READY);

        rtAssignmentOntology = createRtAssignmentOntology();

        ResourceAssignment assignment = new ResourceAssignment();
        assignment.add("SubmitCameraReady", TaskDuty.RESPONSIBLE, RALParser.parse("HAS POSITION PhdStudent"));
        assignment.add("FillTravelAuthorization", TaskDuty.RESPONSIBLE, RALParser.parse("REPORTS TO POSITION ProjectCoordinator"));
        assignment.add("MakeReservations", TaskDuty.RESPONSIBLE, RALParser.parse("HAS ROLE Clerk"));

        rtAssignmentOntology.buildOntology(assignment);

        try {
            manager.getManager().saveOntology(rtAssignmentOntology.getOntology(), new StreamDocumentTarget(System.out));
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }


    }

}
