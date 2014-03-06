package es.us.isa.cristal.owl.mappers.organization;

import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.mappers.ElementMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;

/**
* UnitMapper
* Copyright (C) 2014 Universidad de Sevilla
*
* @author resinas
*/
public class UnitMapper extends OrgElementMapper<String> {
    public UnitMapper(OntologyHandler ontologyHandler, IdMapper mapper) {
        super(ontologyHandler, mapper);
    }

    @Override
    protected String getName(String i) {
        return i;
    }

    @Override
    protected String getType() {
        return Definitions.UNIT;
    }
}
