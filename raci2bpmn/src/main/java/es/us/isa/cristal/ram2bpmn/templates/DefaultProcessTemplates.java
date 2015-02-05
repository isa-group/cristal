package es.us.isa.cristal.ram2bpmn.templates;

import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;

import java.util.List;

/**
 * DefaultProcessTemplates
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class DefaultProcessTemplates implements ProcessTemplates {

    private Template template;

    public DefaultProcessTemplates(Template template) {
        this.template = template;
    }

    @Override
    public Template getTemplate(TTask task) {
        return template;
    }
}
