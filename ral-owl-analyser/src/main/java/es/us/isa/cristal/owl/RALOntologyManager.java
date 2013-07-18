package es.us.isa.cristal.owl;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.Organization;
import es.us.isa.cristal.ResourceAssignment;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.analysers.DTRALAnalyser;
import es.us.isa.cristal.owl.analysers.RTRALAnalyser;
import es.us.isa.cristal.owl.assignments.AssignmentOntology;
import es.us.isa.cristal.owl.assignments.DTAssignmentOntology;
import es.us.isa.cristal.owl.assignments.RTAssignmentOntology;
import es.us.isa.cristal.owl.mappers.LogOntologyManager;
import es.us.isa.cristal.owl.mappers.OrganizationOWLMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.CommonBaseIRIMapper;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.net.URISyntaxException;
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

    private OWLOntologyManager manager;
    private DefaultPrefixManager prefixManager;

    private IdMapper idMapper;
    private final BPEngine engine;
    private OntologyNamespaces namespaces;

    private OWLOntology processOntology;
    private IRI processIRI;
    private OWLOntology organizationOntology;
    private IRI organizationIRI;

    private LogOntologyManager logOntologyManager;
    private AssignmentOntology dtAssignment;
    private AssignmentOntology rtAssignment;

    private ResourceAssignment assignment;


    public OWLOntologyManager getManager() {
        return manager;
    }

    public LogOntologyManager getLogOntologyManager() {
        return logOntologyManager;
    }

    public RALOntologyManager(OntologyNamespaces namespaces, BPEngine engine) {
        this.engine = engine;
        assignment = new ResourceAssignment();
        this.namespaces = namespaces;
        idMapper = new IdMapper(namespaces);


        manager = createOntologyManager();
        prefixManager = createPrefixManager();

    }

    private DefaultPrefixManager createPrefixManager() {
        DefaultPrefixManager prefixManager = new DefaultPrefixManager();
        prefixManager.setPrefix(Definitions.ABSTRACTBP, Definitions.BP_IRI.toString()+"#");
        prefixManager.setPrefix(Definitions.BPRELATIONSHIPS, Definitions.BPRELATIONSHIPS_IRI.toString() + "#");
        prefixManager.setPrefix(Definitions.ORGANIZATION, Definitions.ORGANIZATION_IRI.toString() + "#");
        prefixManager.setPrefix(Definitions.BPMN, Definitions.BPMN_IRI.toString() + "#");
        prefixManager.setPrefix(namespaces.getPerson().getPrefix()+":", namespaces.getPerson().getNamespace()+"#");
        prefixManager.setPrefix(namespaces.getGroup().getPrefix()+":", namespaces.getGroup().getNamespace()+"#");
        prefixManager.setPrefix(namespaces.getActivity().getPrefix() + ":", namespaces.getActivity().getNamespace() + "#");
        prefixManager.setPrefix(LOG, LOG_IRI + "#");

        return prefixManager;
    }

    private OWLOntologyManager createOntologyManager() {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        CommonBaseIRIMapper ralOntologyMapper = null;
        try {
            ralOntologyMapper = new CommonBaseIRIMapper(IRI.create(getClass().getResource("/ontologies/")));

            ralOntologyMapper.addMapping(Definitions.ORGANIZATION_IRI, "organization.owl");
            ralOntologyMapper.addMapping(Definitions.BPMN_IRI, "bpmn.owl");
            ralOntologyMapper.addMapping(Definitions.BPRELATIONSHIPS_IRI, "AbstractBP-relationships.owl");
            ralOntologyMapper.addMapping(Definitions.BP_IRI, "AbstractBP.owl");

            manager.addIRIMapper(ralOntologyMapper);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return manager;
    }

    public void init(OntologyNamespaces namespaces, OWLOntologyIRIMapper mapper) {
        this.namespaces = namespaces;
        idMapper = new IdMapper(namespaces);

        addIRIMapper(mapper);

        loadProcessOntology(IRI.create(namespaces.getActivity().getNamespace()));
        loadOrganizationOntology(IRI.create(namespaces.getGroup().getNamespace()));

        logOntologyManager = new LogOntologyManager(createOntology(LOG_IRI), idMapper, prefixManager);
    }

    public void init(Organization org, OntologyNamespaces namespaces, OWLOntologyIRIMapper mapper) {
        this.namespaces = namespaces;
        idMapper = new IdMapper(namespaces);

        addIRIMapper(mapper);

        loadProcessOntology(IRI.create(namespaces.getActivity().getNamespace()));
        buildOrganizationOntology(org);

        logOntologyManager = new LogOntologyManager(createOntology(LOG_IRI), idMapper, prefixManager);
    }

    private void buildOrganizationOntology(Organization org) {
        organizationIRI = IRI.create(this.namespaces.getGroup().getNamespace());
        OWLOntology orgOntology = null;
        try {
            orgOntology = manager.createOntology(organizationIRI);
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }

        if (manager.getOntology(Definitions.ORGANIZATION_IRI) == null) {
            try {
                manager.loadOntology(Definitions.ORGANIZATION_IRI);
            } catch (OWLOntologyCreationException e) {
                throw new RuntimeException("Could not load bpmn ontology");
            }
        }

        manager.applyChange(new AddImport(orgOntology, manager.getOWLDataFactory().getOWLImportsDeclaration(Definitions.ORGANIZATION_IRI)));

        OrganizationOWLMapper orgMapper = new OrganizationOWLMapper(org, orgOntology, createDLQueryParser(orgOntology), idMapper, prefixManager);
        orgMapper.map();
    }

    public void addIRIMapper(OWLOntologyIRIMapper mapper) {
        manager.addIRIMapper(mapper);
    }

    private OWLOntology loadProcessOntology(IRI ontologyIRI) {
        try {
            processIRI = ontologyIRI;
            processOntology = manager.loadOntology(ontologyIRI);
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }

        return processOntology;
    }

    private OWLOntology loadOrganizationOntology(IRI ontologyIRI) {
        try {
            organizationIRI = ontologyIRI;
            organizationOntology = manager.loadOntology(ontologyIRI);
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }

        return organizationOntology;
    }


    public void addParticipant(String activityName, RALExpr expr) {
        addParticipant(activityName, expr, TaskDuty.RESPONSIBLE);
    }

    public void addParticipant(String activityName, RALExpr expr, TaskDuty duty) {
        assignment.add(activityName, duty, expr);
    }

    public LogOntologyManager.ProcessInstance logProcessInstance(String processName, String pid) {
        return logOntologyManager.processInstance(processName, pid);
    }

    public DTRALAnalyser createDesignTimeAnalyser() {
        if (dtAssignment == null) {
            dtAssignment = new DTAssignmentOntology(this, DTASSIGNMENT_IRI, idMapper, engine);
            dtAssignment.buildOntology(assignment);
        }
        return (DTRALAnalyser) dtAssignment.createAnalyser();
    }

    public RTRALAnalyser createRunTimeAnalyser(String pid) {
        if (rtAssignment != null)
            manager.removeOntology(rtAssignment.getOntology());

        rtAssignment = new RTAssignmentOntology(this, pid, RTASSIGNMENT_IRI + "-" + pid, logOntologyManager, idMapper, engine);
        rtAssignment.buildOntology(assignment);

        return (RTRALAnalyser) rtAssignment.createAnalyser();
    }

    public OWLOntology getDesignTimeOntology() {
        return dtAssignment.getOntology();
    }

    public OWLOntology getLogOntology() {
        return logOntologyManager.getOntology();
    }

    public OWLOntology getRunTimeOntology() {
        return rtAssignment.getOntology();
    }


    public DLQueryParser createDLQueryParser(OWLOntology ontology) {
        return new DLQueryParser(ontology, prefixManager);
    }

    public DLQueryEngine createDLQueryEngine(OWLOntology ontology) {
        return new DLQueryEngine(createReasoner(ontology), prefixManager);
    }

    public OWLOntology createOntology(String ontologyIRI) {
        OWLOntology ontology;

        try {
            ontology = manager.createOntology(IRI.create(ontologyIRI));
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }

        if (manager.getOntology(Definitions.BPMN_IRI) == null) {
            try {
                manager.loadOntology(Definitions.BPMN_IRI);
            } catch (OWLOntologyCreationException e) {
                throw new RuntimeException("Could not load bpmn ontology");
            }
        }

        manager.applyChange(new AddImport(ontology, manager.getOWLDataFactory().getOWLImportsDeclaration(organizationIRI)));
        manager.applyChange(new AddImport(ontology, manager.getOWLDataFactory().getOWLImportsDeclaration(processIRI)));
        manager.applyChange(new AddImport(ontology, manager.getOWLDataFactory().getOWLImportsDeclaration(Definitions.BPMN_IRI)));


        return ontology;
    }

    public OWLReasoner createReasoner(OWLOntology ontology) {
        Reasoner.ReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
        return reasoner;
    }

}
