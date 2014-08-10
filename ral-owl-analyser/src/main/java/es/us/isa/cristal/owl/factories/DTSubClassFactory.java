package es.us.isa.cristal.owl.factories;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.mappers.ral.designtimesc.DTSubClassAssignmentOntology;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.ontologyhandlers.AssignmentOntology;

/**
 * DTSubClassFactory
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class DTSubClassFactory implements DTAssignmentOntologyFactory {
    @Override
    public AssignmentOntology createAssignmentOntology(OntologyHandler ontologyHandler, IdMapper idMapper, BPEngine engine) {
        return new DTSubClassAssignmentOntology(ontologyHandler, idMapper, engine);
    }
}
