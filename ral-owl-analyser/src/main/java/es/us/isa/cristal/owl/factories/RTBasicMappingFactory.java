package es.us.isa.cristal.owl.factories;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.runtime.RTAssignmentOntology;
import es.us.isa.cristal.owl.ontologyhandlers.AssignmentOntology;
import es.us.isa.cristal.owl.ontologyhandlers.LogOntologyHandler;

/**
 * RTBasicMappingFactory
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class RTBasicMappingFactory implements RTAssignmentOntologyFactory {
    @Override
    public AssignmentOntology createAssignmentOntology(OntologyHandler ontologyHandler, IdMapper idMapper, BPEngine engine, String pid, LogOntologyHandler logOntologyHandler) {
        return new RTAssignmentOntology(ontologyHandler, pid, logOntologyHandler, idMapper, engine);
    }
}
