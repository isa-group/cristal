package es.us.isa.cristal.ram2bpmn.templates;

import es.us.isa.cristal.ram.Activity;
import es.us.isa.cristal.ram.RAM;

import java.util.Set;

/**
 * ProcessTemplates
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public interface TemplateAssignment {
    Template getTemplate(Activity task);
    Set<Activity> listActivities();
    Set<Activity> listNotCompatible(RAM matrix);

}
