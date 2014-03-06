package es.us.isa.cristal.owl.mappers.organization;

import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.mappers.ElementMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;

/**
 * OrgElementMapper
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public abstract class OrgElementMapper<T> extends ElementMapper<T> {
    protected IdMapper mapper;

    public OrgElementMapper(OntologyHandler ontologyHandler, IdMapper mapper) {
        super(ontologyHandler);
        this.mapper = mapper;
    }

    @Override
    protected String addPrefix(String name) {
        return mapper.mapGroup(name);
    }
}
