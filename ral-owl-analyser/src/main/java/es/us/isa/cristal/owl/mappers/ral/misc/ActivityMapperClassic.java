package es.us.isa.cristal.owl.mappers.ral.misc;

import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.RALOntologyManager;

/**
 * ActivityMapperClassic
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class ActivityMapperClassic implements ActivityMapper {
    private IdMapper idMapper;

    public ActivityMapperClassic(IdMapper idMapper) {
        this.idMapper = idMapper;
    }

    @Override
    public String mapActivity(String activityName) {
        return idMapper.mapActivity(activityName);
    }

    @Override
    public String mapAssignment(String activityName) {
        return Definitions.ISPOTENTIALRESPONSIBLE + " value " + idMapper.mapActivity(activityName);
    }


}
