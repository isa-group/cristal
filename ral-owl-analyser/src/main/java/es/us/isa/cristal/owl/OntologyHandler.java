package es.us.isa.cristal.owl;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/**
 * OntologyHandler
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class OntologyHandler {
    protected OWLOntology ontology;
    protected DefaultPrefixManager prefixManager;

    public OntologyHandler(OWLOntology ontology, DefaultPrefixManager prefixManager) {
        this.ontology = ontology;
        this.prefixManager = prefixManager;
    }

    public OWLOntology getOntology() {
        return ontology;
    }

    public DefaultPrefixManager getPrefixManager() {
        return prefixManager;
    }

    public DLQueryParser createDLQueryParser() {
        return new DLQueryParser(ontology, prefixManager);
    }

    public DLQueryEngine createDLQueryEngine() {
        return new DLQueryEngine(createReasoner(), prefixManager);
    }


    public OWLReasoner createReasoner() {
//        OWLReasonerFactory reasonerFactory = PelletOWLAPIReasonerFactory.getInstance();
        OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();


        OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
//        IncrementalClassifier classifier = new IncrementalClassifier((PelletReasoner) reasoner);
//        ontology.getOWLOntologyManager().addOntologyChangeListener((PelletOWLAPIReasoner) reasoner);

        return reasoner;
    }
}
