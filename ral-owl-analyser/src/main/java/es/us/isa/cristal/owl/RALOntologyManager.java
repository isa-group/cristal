package es.us.isa.cristal.owl;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.Organization;
import es.us.isa.cristal.ResourceAssignment;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.owl.mappers.ral.designtimesc.DTSubClassAssignmentOntology;
import es.us.isa.cristal.owl.mappers.ral.runtime.RTAssignmentOntology;
import es.us.isa.cristal.owl.ontologyhandlers.*;
import es.us.isa.cristal.owl.mappers.OrganizationOWLMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.CommonBaseIRIMapper;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 12:45
 */
public class RALOntologyManager {

    private static final Logger log = Logger.getLogger(RALOntologyManager.class.getName());

    public static final String LOG_IRI = "log";
    public static final String DTASSIGNMENT_IRI = "assignment-dt";
    public static final String RTASSIGNMENT_IRI = "assignment-rt";

    public static final String LOG = "log:";
    public static final String DTASSIGNMENT = "dtassign:";

    private OWLOntologyManager manager;
    private DefaultPrefixManager prefixManager;

    private IdMapper idMapper;
    private final BPEngine engine;
    private OntologyNamespaces namespaces;

    private IRI processIRI;
    private IRI organizationIRI;

    private LogOntologyHandler logOntologyHandler;
    private AssignmentOntology dtAssignment;
    private AssignmentOntology rtAssignment;

    private ResourceAssignment assignment;

    public RALOntologyManager(OntologyNamespaces namespaces, BPEngine engine) {
        this.engine = engine;
        this.namespaces = namespaces;
        idMapper = new IdMapper(namespaces);

        manager = createOntologyManager();
        loadCoreOntologies();

        prefixManager = createPrefixManager(namespaces);

        Reasoner.ReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(manager.getOntology(Definitions.ORGANIZATION_IRI));
        log.info("org consistent: " + reasoner.isConsistent());

        reasoner = reasonerFactory.createReasoner(manager.getOntology(Definitions.BP_IRI));
        log.info("bpmn consistent: " + reasoner.isConsistent());

        reasoner = reasonerFactory.createReasoner(manager.getOntology(Definitions.BPRELATIONSHIPS_IRI));
        log.info("bp relationships consistent: " + reasoner.isConsistent());

    }


    // Loaders -------------------------------------

    public OWLOntology loadProcessOntology(IRI documentIRI) {

        this.processIRI = IRI.create(this.namespaces.getActivity().getNamespace());
        return loadOntology(processIRI, documentIRI);
    }

    public OWLOntology loadOrganizationOntology(IRI documentIRI) {
        organizationIRI = IRI.create(this.namespaces.getGroup().getNamespace());
        return loadOntology(organizationIRI, documentIRI);
    }

    public OWLOntology loadOrganizationOntology(Organization org) {
        organizationIRI = IRI.create(this.namespaces.getGroup().getNamespace());

        OntologyHandler orgOntologyHandler = createOntology(organizationIRI, Definitions.ORGANIZATION_IRI);

        OrganizationOWLMapper orgMapper = new OrganizationOWLMapper(orgOntologyHandler, idMapper);
        orgMapper.map(org);

        return orgOntologyHandler.getOntology();
    }

    public void loadResourceAssignment(ResourceAssignment assignment) {
        this.assignment = assignment;
    }

    public LogOntologyHandler.ProcessInstance logProcessInstance(String processName, String pid) {
        return getLogOntologyHandler().processInstance(processName, pid);
    }

    // Getters and creators ----------------------------------------

    public LogOntologyHandler getLogOntologyHandler() {
        if (logOntologyHandler == null) {
            logOntologyHandler = new LogOntologyHandler(createImportAllOntology(LOG_IRI), idMapper);
        }
        return logOntologyHandler;
    }

    public AssignmentOntology getRuntimeAssignmentOntology(String pid) {
        if (rtAssignment != null)
            manager.removeOntology(rtAssignment.getOntology());

        rtAssignment = new RTAssignmentOntology(createImportAllOntology(RTASSIGNMENT_IRI + "-" + pid), pid, getLogOntologyHandler(), idMapper, engine);
        rtAssignment.buildOntology(assignment);

        return rtAssignment;
    }

    public AssignmentOntology getDesignTimeAssignmentOntology() {
        if (dtAssignment == null) {
            dtAssignment = new DTSubClassAssignmentOntology(createImportAllOntology(DTASSIGNMENT_IRI), idMapper, engine);
            dtAssignment.buildOntology(assignment);
        }
        return dtAssignment;
    }

    public RALAnalyser createDesignTimeAnalyser() {
        return getDesignTimeAssignmentOntology().createAnalyser();
    }

    public RALAnalyser createRunTimeAnalyser(String pid) {
        return getRuntimeAssignmentOntology(pid).createAnalyser();
    }

    // Private methods ---------------------------------

    private OWLOntologyManager createOntologyManager() {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        CommonBaseIRIMapper ralOntologyMapper = null;
        try {
            ralOntologyMapper = new CommonBaseIRIMapper(IRI.create(getClass().getResource("/es/us/isa/cristal/ontologies/")));
            ralOntologyMapper.addMapping(Definitions.ORGANIZATION_IRI, "organization.owl");

            URL resource = getClass().getResource("/es/us/isa/bpmn/ontologies/");
            CommonBaseIRIMapper bpmnOntologyMapper = new CommonBaseIRIMapper(IRI.create(resource));
            bpmnOntologyMapper.addMapping(Definitions.BPMN_IRI, "bpmn.owl");
            bpmnOntologyMapper.addMapping(Definitions.BPRELATIONSHIPS_IRI, "AbstractBP-relationships.owl");
            bpmnOntologyMapper.addMapping(Definitions.BP_IRI, "AbstractBP.owl");

            manager.addIRIMapper(ralOntologyMapper);
            manager.addIRIMapper(bpmnOntologyMapper);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not load core organization and process ontologies", e);
        }

        return manager;
    }

    private DefaultPrefixManager createPrefixManager(OntologyNamespaces namespaces) {
        DefaultPrefixManager prefixManager = new DefaultPrefixManager();
        prefixManager.setPrefix(Definitions.ORGANIZATION, Definitions.ORGANIZATION_IRI.toString() + "#");

        prefixManager.setPrefix(Definitions.ABSTRACTBP, Definitions.BP_IRI.toString()+"#");
        prefixManager.setPrefix(Definitions.BPRELATIONSHIPS, Definitions.BPRELATIONSHIPS_IRI.toString() + "#");
        prefixManager.setPrefix(Definitions.BPMN, Definitions.BPMN_IRI.toString() + "#");
        prefixManager.setPrefix(LOG, LOG_IRI + "#");
        prefixManager.setPrefix(DTASSIGNMENT, DTASSIGNMENT_IRI + "#");

        prefixManager.setPrefix(namespaces.getPerson().getPrefix()+":", namespaces.getPerson().getNamespace()+"#");
        prefixManager.setPrefix(namespaces.getGroup().getPrefix()+":", namespaces.getGroup().getNamespace()+"#");
        prefixManager.setPrefix(namespaces.getActivity().getPrefix() + ":", namespaces.getActivity().getNamespace() + "#");

        return prefixManager;
    }

    private void loadCoreOntologies() {
        if (manager.getOntology(Definitions.ORGANIZATION_IRI) == null) {
            try {
                manager.loadOntology(Definitions.ORGANIZATION_IRI);
            } catch (OWLOntologyCreationException e) {
                throw new RuntimeException("Could not load bpmn ontology", e);
            }
        }

        if (manager.getOntology(Definitions.BPMN_IRI) == null) {
            try {
                manager.loadOntology(Definitions.BPMN_IRI);
            } catch (OWLOntologyCreationException e) {
                throw new RuntimeException("Could not load bpmn ontology", e);
            }
        }

        if (manager.getOntology(Definitions.BPRELATIONSHIPS_IRI) == null) {
            try {
                manager.loadOntology(Definitions.BPRELATIONSHIPS_IRI);
            } catch (OWLOntologyCreationException e) {
                throw new RuntimeException("Could not load BP Relationships ontology", e);
            }
        }
    }


    private OWLOntology loadOntology(IRI ontologyIRI, IRI documentIRI) {
        OWLOntology ontology;
        try {
            manager.addIRIMapper(new SimpleIRIMapper(ontologyIRI, documentIRI));
            ontology = manager.loadOntology(ontologyIRI);
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }

        return ontology;
    }

    private OntologyHandler createOntology(IRI ontologyIRI, IRI... importIRIs) {

        OWLOntology ontology = null;
        try {
            ontology = manager.createOntology(ontologyIRI);
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }

        for (IRI importIRI : importIRIs) {
            manager.applyChange(new AddImport(ontology, manager.getOWLDataFactory().getOWLImportsDeclaration(importIRI)));
        }

        return new OntologyHandler(ontology, prefixManager);
    }


    private OntologyHandler createImportAllOntology(String ontologyIRI) {
        if (processIRI == null || organizationIRI == null) {
            throw new IllegalStateException("Process and organization ontologies should be loaded before");
        }

        return createOntology(IRI.create(ontologyIRI), organizationIRI, processIRI, Definitions.BPMN_IRI);
    }


}
