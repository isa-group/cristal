package es.us.isa.cristal.owl.ontologyhandlers;

import es.us.isa.cristal.RALResourceAssignment;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.owl.DLQueryParser;
import es.us.isa.cristal.owl.OntologyHandler;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.logging.Logger;

/**
* User: resinas
* Date: 13/07/13
* Time: 10:58
*/
public abstract class AssignmentOntology extends OntologyHandler {

    private static final Logger log = Logger.getLogger(AssignmentOntology.class.getName());

    private DLQueryParser parser;

    public AssignmentOntology(OntologyHandler ontologyHandler) {
        super(ontologyHandler.getOntology(), ontologyHandler.getPrefixManager());
        ontology = ontologyHandler.getOntology();
    }

    protected OWLAxiom addAxiom(String ax) {
        if (parser == null)
            parser = createDLQueryParser();

        log.info("AddPerformer: " + ax);

        OWLAxiom axiom = parser.parseAxiom(ax);
        ontology.getOWLOntologyManager().addAxiom(ontology, axiom);

        return axiom;
    }

    public abstract void buildOntology(RALResourceAssignment assignment);

    public abstract RALAnalyser createAnalyser();

    public void precompute(RALResourceAssignment assignment, boolean full) {}
}
