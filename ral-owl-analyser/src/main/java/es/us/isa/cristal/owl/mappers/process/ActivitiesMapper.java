package es.us.isa.cristal.owl.mappers.process;

import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.mappers.ElementMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;

/**
 * ActivitiesMapper
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class ActivitiesMapper extends ElementMapper<String>{
    private IdMapper idMapper;

    public ActivitiesMapper(OntologyHandler ontologyHandler, IdMapper idMapper) {
        super(ontologyHandler);
        this.idMapper = idMapper;
    }

    @Override
    protected String getName(String i) {
        return i;
    }

    @Override
    protected String addPrefix(String name) {
        return idMapper.mapActivity(name);
    }

    @Override
    protected String getType() {
        return Definitions.ACTIVITY;
    }


}
