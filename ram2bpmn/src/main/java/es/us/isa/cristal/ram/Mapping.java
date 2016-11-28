package es.us.isa.cristal.ram;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import es.us.isa.cristal.ram.mappings.RalMapping;

/**
 * Map
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public interface Mapping {
    String map(BoundedRole boundedRole);
    String orMap(RAM ram, Activity a, Responsibility r);
    String andMap(RAM ram, Activity a, Responsibility r);

    String responsibleBoD(Activity a, RAM ram);

    static Mapping defaultMapping() {
        return new RalMapping();
    }
}
