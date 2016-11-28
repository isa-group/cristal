package es.us.isa.cristal.ram;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import es.us.isa.cristal.ram.responsibilities.RASCI;

/**
 * Responsibility
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RASCI.class, name = "rasci")
})
public interface Responsibility {
}
