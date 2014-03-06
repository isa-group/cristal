package es.us.isa.cristal.owl.mappers.process;

import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;

import java.util.Arrays;

/**
 * ProcessActivitiesOWLMapper
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class ProcessActivitiesOWLMapper {

    private OntologyHandler ontologyHandler;
    private IdMapper idMapper;

    public ProcessActivitiesOWLMapper(OntologyHandler ontologyHandler, IdMapper idMapper) {
        this.ontologyHandler = ontologyHandler;
        this.idMapper = idMapper;
    }

    public void map(String... activities) {
        new ActivitiesMapper(ontologyHandler, idMapper).map(Arrays.asList(activities));
    }
}
