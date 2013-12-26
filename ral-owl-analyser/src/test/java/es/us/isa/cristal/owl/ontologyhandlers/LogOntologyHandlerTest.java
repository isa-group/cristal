package es.us.isa.cristal.owl.ontologyhandlers;

import es.us.isa.cristal.owl.*;
import es.us.isa.cristal.owl.ontologyhandlers.LogOntologyHandler;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * User: resinas
 * Date: 13/07/13
 * Time: 13:29
 */
public class LogOntologyHandlerTest {

    private AttendConferenceScenario scenario;
    private RALOntologyManager manager;

    @Before
    public void setup() {
        scenario = new AttendConferenceScenario();
        manager = scenario.getRalOntologyManager();
    }

    @Test
    public void shouldCreateActivityInstances() {
        manager.logProcessInstance("AttendConference", "ac1")
                .activity("SubmitCameraReady", "scr1", LogOntologyHandler.ActivityState.COMPLETED, "Adela")
                .activity("CheckResponse", "cr1", LogOntologyHandler.ActivityState.ALLOCATED);

        LogOntologyHandler handler = manager.getLogOntologyHandler();
        OWLOntology logOntology = handler.getOntology();
        DLQueryEngine engine = handler.createDLQueryEngine();

        Set<OWLNamedIndividual> individuals = engine.getInstances("inverse(" + Definitions.HASACTIVITYINSTANCE + ") value log:ac1", false);

        Assert.assertEquals(2, individuals.size());
        Assert.assertEquals(new HashSet<String>(Arrays.asList("scr1", "cr1")), OntologyTestUtils.toFragments(individuals));

        try {
            logOntology.getOWLOntologyManager().saveOntology(logOntology, new StreamDocumentTarget(System.out));
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }

    }

}
