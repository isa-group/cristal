package es.us.isa.cristal.owl.mappers;

import es.us.isa.cristal.owl.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
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
public class LogOntologyManagerTest {

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
                .activity("SubmitCameraReady", "scr1", LogOntologyManager.ActivityState.COMPLETED, "Adela")
                .activity("CheckResponse", "cr1", LogOntologyManager.ActivityState.ALLOCATED);


        OWLOntology logOntology = manager.getManager().getOntology(IRI.create(RALOntologyManager.LOG_IRI));
        DLQueryEngine engine = manager.createDLQueryEngine(logOntology);

        Set<OWLNamedIndividual> individuals = engine.getInstances("inverse(" + Definitions.HASACTIVITYINSTANCE + ") value log:ac1", false);

        Assert.assertEquals(2, individuals.size());
        Assert.assertEquals(new HashSet<String>(Arrays.asList("scr1", "cr1")), OntologyTestUtils.toFragments(individuals));

        try {
            manager.getManager().saveOntology(logOntology, new StreamDocumentTarget(System.out));
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }

    }

}
