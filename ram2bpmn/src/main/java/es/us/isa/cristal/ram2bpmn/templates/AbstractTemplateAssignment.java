package es.us.isa.cristal.ram2bpmn.templates;

import es.us.isa.cristal.ram.Activity;
import es.us.isa.cristal.ram.RAM;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AbstractTemplateAssignment
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public abstract class AbstractTemplateAssignment implements TemplateAssignment {
    @Override
    public Set<Activity> listNotCompatible(RAM matrix) {
        Set<Activity> notCompatible = new HashSet<>(listActivities());
        Set<Activity> inMatrix = matrix.getActivities();

        // Everything in the assignment that is not in the matrix is not compatible
        notCompatible.removeAll(inMatrix);
        notCompatible.addAll(inMatrix.stream()
                .filter(a -> {
                            Template t = getTemplate(a);
                            return t == null || !t.isCompatible(matrix, a);
                        }
                ).collect(Collectors.toSet()));

        return notCompatible;
    }
}
