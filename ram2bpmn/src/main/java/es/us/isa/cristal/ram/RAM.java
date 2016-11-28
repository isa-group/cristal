package es.us.isa.cristal.ram;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * RAM
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class RAM {

    private Set<BoundedRole> boundedRoles;

    public RAM() {
        boundedRoles = new HashSet<>();
    }

    public void add(BoundedRole role, BoundedRole... roles) {
        boundedRoles.add(role);
        boundedRoles.addAll(Arrays.asList(roles));
    }

    public Set<Activity> getActivities() {
        return boundedRoles.stream().map(br -> br.getActivity()).collect(Collectors.toSet());
    }

    public Set<BoundedRole> getBoundedRoles() {
        return boundedRoles;
    }

    public Set<Responsibility> duties(Activity a) {
        return boundedRoles.stream()
                .filter(br -> br.getActivity().equals(a))
                .map(br -> br.getResponsibility())
                .collect(Collectors.toSet());
    }

    public Set<BoundedRole> filter(Activity a, Responsibility r) {
        return boundedRoles.stream()
                .filter(br -> (br.getActivity().equals(a) && br.getResponsibility().equals(r)))
                .collect(Collectors.toSet());
    }

    public boolean hasTD(Activity a, Responsibility r) {
        return boundedRoles.stream()
                .anyMatch(br -> (br.getActivity().equals(a) && br.getResponsibility().equals(r)));
    }

    public boolean onlyTD(Activity a, Responsibility r) {
        return boundedRoles.stream()
                .filter(br -> br.getActivity().equals(a))
                .allMatch(br -> br.getResponsibility().equals(r));
    }

    public boolean anyTD(Activity a) {
        return boundedRoles.stream()
                .anyMatch(br -> br.getActivity().equals(a));
    }
}
