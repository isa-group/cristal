package es.us.isa.cristal.ram2bpmn.templates.repository;

import es.us.isa.cristal.ram2bpmn.templates.Template;
import es.us.isa.cristal.ram2bpmn.templates.TemplateAssignment;

import java.util.Map;
import java.util.Set;

/**
 * TemplateRepository
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public interface TemplateRepository {
    Template getTemplate(String template);
    TemplateAssignment buildAssignmentFrom(Map<String, String> assignment);
    Set<String> listTemplateNames();
}
