package es.us.isa.cristal.owl.analysers;


import static es.us.isa.cristal.owl.HiringResolutionScenario.BP_HIRING_RESOLUTION_IRI;
import static es.us.isa.cristal.owl.HiringResolutionScenario.ORGANIZATION_IAAP_IRI;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

import es.us.isa.cristal.ResourceAssignment;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.OntologyNamespaces;
import es.us.isa.cristal.owl.RALOntologyManager;
import es.us.isa.cristal.parser.RALParser;
import es.us.isa.cristal.test.utils.bpEngine.MockBPEngine;

/**
 * User: resinas
 * Date: 04/07/13
 * Time: 17:05
 */
public class ScenarioHiringResolutionTest {
    private RALOntologyManager manager;
    private OntologyNamespaces namespaces;

    private OntologyNamespaces createOntologyNamespaces() {
        OntologyNamespaces namespaces = new OntologyNamespaces();
        namespaces.setPerson("organization-iaap", ORGANIZATION_IAAP_IRI.toString());
        namespaces.setGroup("organization-iaap", ORGANIZATION_IAAP_IRI.toString());
        namespaces.setActivity("bp-hiring-resolution", BP_HIRING_RESOLUTION_IRI.toString());
        return namespaces;
    }

    @Before
    public void setup() throws URISyntaxException {
        namespaces = createOntologyNamespaces();

        manager = new RALOntologyManager(namespaces, new MockBPEngine());
        manager.loadOrganizationOntology(IRI.create(getClass().getResource("/es/us/isa/cristal/ontologies/organization-iaap.owl")));
        manager.loadProcessOntology(IRI.create(getClass().getResource("/es/us/isa/cristal/ontologies/bp-hiring-resolution.owl")));

    }

    @Test
    public void shouldGetPotentialPerformersOfRequestReportConsultiveBoard() {
        manager.loadResourceAssignment(new ResourceAssignment().add("RequestReportConsultiveBoard", RALParser.parse("HAS ROLE Accountable")));

        RALAnalyser analyser = manager.createDesignTimeAnalyser();
        Set<String> requestReportConsultiveBoard = analyser.potentialParticipants("RequestReportConsultiveBoard", TaskDuty.PARTICIPANT);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Mario")), requestReportConsultiveBoard);
        System.out.println(requestReportConsultiveBoard);
    }


}
