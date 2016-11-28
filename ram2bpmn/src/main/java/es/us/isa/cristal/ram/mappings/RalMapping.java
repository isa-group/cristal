package es.us.isa.cristal.ram.mappings;

import es.us.isa.cristal.ram.*;

import java.util.stream.Collectors;

/**
 * RalMap
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class RalMapping implements Mapping {

    @Override
    public String map(BoundedRole boundedRole) {
        StringBuilder sb = new StringBuilder();

        if (! boundedRole.hasAdditionalRestrictions()) {
            sb.append("HAS ROLE ").append(boundedRole.getRole().getName());
            addOrganisationalContext(boundedRole, sb);
        } else {
            sb.append("(HAS ROLE ").append(boundedRole.getRole().getName());
            addOrganisationalContext(boundedRole, sb);
            sb.append(") AND (").append(boundedRole.getAdditionalRestrictions()).append(")");
        }

        return sb.toString();
    }

    private void addOrganisationalContext(BoundedRole boundedRole, StringBuilder sb) {
        if (boundedRole.hasOrganisationalContext()) {
            sb.append(" IN UNIT ").append(boundedRole.getOrganisationalContext());
        }
    }

    @Override
    public String orMap(RAM ram, Activity a, Responsibility r) {
        return ram.filter(a, r).stream()
                .map(br -> "(" + map(br) + ")")
                .collect(Collectors.joining(" OR "));
    }

    @Override
    public String andMap(RAM ram, Activity a, Responsibility r) {
        return ram.filter(a, r).stream()
                .map(br -> "(" + map(br) + ")")
                .collect(Collectors.joining(" AND "));

    }

    @Override
    public String responsibleBoD(Activity a, RAM ram) {
        return "IS ANY PERSON responsible for ACTIVITY " + a.getName();
    }
}
