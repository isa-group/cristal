package es.us.isa.cristal.ram.mappings;

import es.us.isa.cristal.ram.*;
import es.us.isa.cristal.ram.responsibilities.RASCI;

import java.util.stream.Collectors;

/**
 * CamundaMapping
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class CamundaMapping implements Mapping {
    @Override
    public String map(BoundedRole boundedRole) {
        return "group(" + boundedRole.getRole().getName() + ")";
    }

    @Override
    public String orMap(RAM ram, Activity a, Responsibility r) {
        return ram.filter(a, r).stream()
                .map(br -> "group(" + br.getRole().getName() + ")")
                .collect(Collectors.joining(","));
    }

    @Override
    public String andMap(RAM ram, Activity a, Responsibility r) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String responsibleBoD(Activity a, RAM ram) {
        return orMap(ram, a, RASCI.responsible);
    }
}
