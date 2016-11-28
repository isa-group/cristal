package es.us.isa.cristal.ram2bpmn.templates.repository;

import es.us.isa.cristal.ram.Activity;
import es.us.isa.cristal.ram2bpmn.templates.AbstractTemplateAssignment;
import es.us.isa.cristal.ram2bpmn.templates.Template;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * RepositoryTemplateAssignment
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class RepositoryTemplateAssignment extends AbstractTemplateAssignment {
    private TemplateRepository repository;
    private Map<String, String> assignment;

    public RepositoryTemplateAssignment(TemplateRepository repository, Map<String, String> assignment) {
        this.repository = repository;
        this.assignment = assignment;
    }

    @Override
    public Template getTemplate(Activity task) {
        return repository.getTemplate(assignment.get(task.getName()));
    }

    @Override
    public Set<Activity> listActivities() {
        return assignment.keySet().stream().map(Activity::new).collect(Collectors.toSet());
    }
}
