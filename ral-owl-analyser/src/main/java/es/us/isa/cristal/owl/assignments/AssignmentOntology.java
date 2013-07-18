package es.us.isa.cristal.owl.assignments;

import es.us.isa.cristal.ResourceAssignment;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.owl.DLQueryParser;
import es.us.isa.cristal.owl.RALOntologyManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.logging.Logger;

/**
* User: resinas
* Date: 13/07/13
* Time: 10:58
*/
public abstract class AssignmentOntology {

    private static final Logger log = Logger.getLogger(AssignmentOntology.class.getName());

    protected OWLOntology ontology;
    protected RALOntologyManager ralOntologyManager;
    private DLQueryParser parser;

    public AssignmentOntology(RALOntologyManager ralOntologyManager, String assignmentIRI) {
        this.ralOntologyManager = ralOntologyManager;
        ontology = ralOntologyManager.createOntology(assignmentIRI);
    }

    protected OWLAxiom addAxiom(String ax) {
        if (parser == null)
            parser = ralOntologyManager.createDLQueryParser(ontology);

        log.info("AddPerformer: " + ax);

        OWLAxiom axiom = parser.parseAxiom(ax);
        ontology.getOWLOntologyManager().addAxiom(ontology, axiom);

        return axiom;
    }

    public OWLOntology getOntology() {
        return ontology;
    }

    public abstract void buildOntology(ResourceAssignment assignment);

    //public abstract void addParticipant(String activityName, RALExpr expr, TaskDuty duty);
    public abstract RALAnalyser createAnalyser();
}
