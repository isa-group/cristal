package es.us.isa.cristal.ram2bpmn.templates;

import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;

import java.util.List;

/**
 * ProcessTemplates
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public interface ProcessTemplates {
    public Template getTemplate(TTask task);
}
