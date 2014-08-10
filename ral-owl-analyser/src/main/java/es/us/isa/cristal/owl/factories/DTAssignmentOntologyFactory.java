package es.us.isa.cristal.owl.factories;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.owl.DLQueryEngine;
import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.ActivityMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.ontologyhandlers.AssignmentOntology;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/**
 * DTAssignmentOntologyFactory
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public interface DTAssignmentOntologyFactory {
    AssignmentOntology createAssignmentOntology(OntologyHandler ontologyHandler, IdMapper idMapper, BPEngine engine);
}
