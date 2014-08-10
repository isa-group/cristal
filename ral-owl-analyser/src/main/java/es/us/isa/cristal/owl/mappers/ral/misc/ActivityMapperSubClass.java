package es.us.isa.cristal.owl.mappers.ral.misc;

import es.us.isa.cristal.owl.RALOntologyManager;

/**
* ActivityMapper
* Copyright (C) 2014 Universidad de Sevilla
*
* @author resinas
*/
public class ActivityMapperSubClass implements ActivityMapper {

    private String prefix;

    public ActivityMapperSubClass(String prefix) {
        this.prefix = prefix;
    }

    public ActivityMapperSubClass() {
        this.prefix = "";
    }

    @Override
    public String mapActivity(String activityName) {
        return RALOntologyManager.DTASSIGNMENT + prefix + activityName;
    }

    @Override
    public String mapAssignment(String activityName) {
        return RALOntologyManager.DTASSIGNMENT + activityName + "Assignment";
    }
}
