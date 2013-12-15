package es.us.isa.cristal.owl.analysers;

import es.us.isa.cristal.BPEngineMock;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.OntologyNamespaces;
import es.us.isa.cristal.owl.RALOntologyManager;
import es.us.isa.cristal.parser.RALParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.util.CommonBaseIRIMapper;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static es.us.isa.cristal.owl.HiringResolutionScenario.*;

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
    public void setup() {
        namespaces = createOntologyNamespaces();

        manager = new RALOntologyManager(namespaces, new BPEngineMock());

        try {
            manager.init(namespaces, createIRIMapper());
        } catch (URISyntaxException e) {
            // The ontology manager will try to get the ontologies from the web
            e.printStackTrace();
        }

    }

    private OWLOntologyIRIMapper createIRIMapper() throws URISyntaxException {
        CommonBaseIRIMapper ralOntologyMapper;
        ralOntologyMapper = new CommonBaseIRIMapper(IRI.create(getClass().getResource("/es/us/isa/cristal/ontologies/")));

        ralOntologyMapper.addMapping(BP_HIRING_RESOLUTION_LOG_IRI, "bp-hiring-resolution-log.owl");
        ralOntologyMapper.addMapping(BP_HIRING_RESOLUTION_IRI, "bp-hiring-resolution.owl");
        ralOntologyMapper.addMapping(ORGANIZATION_IAAP_IRI, "organization-iaap.owl");
        return ralOntologyMapper;
    }


    @Test
    public void shouldGetPotentialPerformersOfRequestReportConsultiveBoard() {
        manager.addParticipant("RequestReportConsultiveBoard", RALParser.parse("HAS ROLE Accountable"));

        RALAnalyser analyser = manager.createDesignTimeAnalyser();
        Set<String> requestReportConsultiveBoard = analyser.potentialParticipants("RequestReportConsultiveBoard", TaskDuty.PARTICIPANT);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Mario")), requestReportConsultiveBoard);
        System.out.println(requestReportConsultiveBoard);
    }


}
