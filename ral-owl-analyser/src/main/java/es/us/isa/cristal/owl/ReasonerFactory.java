package es.us.isa.cristal.owl;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

/**
 * ReasonerFactory
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class ReasonerFactory {
    private static OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();

    public static OWLReasonerFactory getReasonerFactory() {
        return reasonerFactory;
    }

    public static void setReasonerFactory(OWLReasonerFactory reasonerFactory) {
        ReasonerFactory.reasonerFactory = reasonerFactory;
    }
}
