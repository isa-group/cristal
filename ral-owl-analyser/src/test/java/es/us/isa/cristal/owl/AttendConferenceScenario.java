package es.us.isa.cristal.owl;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.BPEngineMock;
import es.us.isa.cristal.Organization;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.util.CommonBaseIRIMapper;

import java.net.URISyntaxException;
import java.util.Arrays;

import static es.us.isa.cristal.Organization.person;
import static es.us.isa.cristal.Organization.pos;

/**
 * User: resinas
 * Date: 13/07/13
 * Time: 13:01
 */
public class AttendConferenceScenario {

    private static final String EXAMPLES_URL = "http://www.isa.us.es/cristal/";

    public static final IRI ORGANIZATION_ISA_IRI = IRI.create(EXAMPLES_URL+"organization-isa.owl");
    public static final IRI BP_ATTEND_CONFERENCE_IRI = IRI.create(EXAMPLES_URL+"bp-attend-conference.owl");

    private BPEngine bpEngine;
    private RALOntologyManager ralOntologyManager;
    private IdMapper idMapper;

    public AttendConferenceScenario() {
        OntologyNamespaces namespaces = createOntologyNamespaces();
        bpEngine = new BPEngineMock();
        idMapper = new IdMapper(namespaces);
        ralOntologyManager = new RALOntologyManager(namespaces, bpEngine);

        try {
            ralOntologyManager.init(createOrganization(), namespaces, createIRIMapper());
        } catch (URISyntaxException e) {
            // The ontology manager will try to get the ontologies from the web
            e.printStackTrace();
        }



    }

    private Organization createOrganization() {
        Organization org = new Organization();

        org.units("ProjectTHEOS")
                .roles("AccountAdministrator", "Clerk", "DoctoralThesisAdvisor", "ResearchAssistant", "Researcher", "ResourceManager", "Responsible")
                .positions(
                        pos("AccountDelegate", "ProjectTHEOS", Arrays.asList("AccountAdministrator"), null, null),
                        pos("AdministrativeAssistant", "ProjectTHEOS", Arrays.asList("Clerk"), null, null),
                        pos("PhdStudent", "ProjectTHEOS", Arrays.asList("ResearchAssistant"), null, null),
                        pos("ProjectCoordinator", "ProjectTHEOS", Arrays.asList("ResourceManager", "Responsible", "DoctoralThesisAdvisor", "Researcher", "AccountAdministrator"),
                                Arrays.asList("AccountDelegate", "AdministrativeAssistant", "ResponsibleForWorkPackage", "SeniorTechnician"),
                                Arrays.asList("AccountDelegate", "PhdStudent", "ResponsibleForWorkPackage", "SeniorTechnician", "AdministrativeAssistant")),
                        pos("ResponsibleForWorkPackage", "ProjectTHEOS", Arrays.asList("Researcher", "Responsible"),
                                Arrays.asList("PhdStudent"), Arrays.asList("PhdStudent")),
                        pos("SeniorTechnician", "ProjectTHEOS", Arrays.asList("Responsible"), null, null)
                )
                .persons(
                        person("Adela", "PhdStudent"),
                        person("Ana", "AdministrativeAssistant"),
                        person("Antonio", "ProjectCoordinator", "ResponsibleForWorkPackage"),
                        person("Beatriz", "ResponsibleForWorkPackage", "AccountDelegate"),
                        person("Cristina", "PhdStudent"),
                        person("Manuel", "ResponsibleForWorkPackage"),
                        person("Sergio", "SeniorTechnician")
                );


        return org;
    }

    private OntologyNamespaces createOntologyNamespaces() {
        OntologyNamespaces namespaces = new OntologyNamespaces();
        namespaces.setPerson("organization-isa", ORGANIZATION_ISA_IRI.toString());
        namespaces.setGroup("organization-isa", ORGANIZATION_ISA_IRI.toString());
        namespaces.setActivity("bp-attend-conference", BP_ATTEND_CONFERENCE_IRI.toString());
        return namespaces;
    }

    private OWLOntologyIRIMapper createIRIMapper() throws URISyntaxException {
        CommonBaseIRIMapper ralOntologyMapper;
        ralOntologyMapper = new CommonBaseIRIMapper(IRI.create(getClass().getResource("/es/us/isa/cristal/ontologies/")));

        ralOntologyMapper.addMapping(BP_ATTEND_CONFERENCE_IRI, "bp-attend-conference.owl");
        ralOntologyMapper.addMapping(ORGANIZATION_ISA_IRI, "organization-isa.owl");
        return ralOntologyMapper;
    }

    public RALOntologyManager getRalOntologyManager() {
        return ralOntologyManager;
    }

    public BPEngine getBpEngine() {
        return bpEngine;
    }

    public IdMapper getIdMapper() {
        return idMapper;
    }
}
