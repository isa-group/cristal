package es.us.isa.cristal.ram2bpmn.templates.repository;

import es.us.isa.cristal.ram2bpmn.templates.Template;
import es.us.isa.cristal.ram2bpmn.templates.TemplateAssignment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * MemoryTemplateRepository
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class MemoryTemplateRepository implements TemplateRepository {
    public Map<String, Template> templates;

    public MemoryTemplateRepository() {
        templates = new HashMap<>();
    }

    public MemoryTemplateRepository add(String id, Template template) {
        this.templates.put(id, template);
        return this;
    }

    @Override
    public Template getTemplate(String template) {
        return templates.get(template);
    }

    @Override
    public TemplateAssignment buildAssignmentFrom(Map<String, String> assignment) {
        return new RepositoryTemplateAssignment(this, assignment);
    }

    @Override
    public Set<String> listTemplateNames() {
        return templates.keySet();
    }
}
