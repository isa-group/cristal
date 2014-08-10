package es.us.isa.cristal.owl.factories;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.ontologyhandlers.AssignmentOntology;
import es.us.isa.cristal.owl.ontologyhandlers.LogOntologyHandler;

/**
 * DTAssignmentOntologyFactory
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public interface RTAssignmentOntologyFactory {
    AssignmentOntology createAssignmentOntology(OntologyHandler ontologyHandler, IdMapper idMapper, BPEngine engine, String pid, LogOntologyHandler logOntologyHandler);
}
