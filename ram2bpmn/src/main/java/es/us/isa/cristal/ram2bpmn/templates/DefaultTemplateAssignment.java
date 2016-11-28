package es.us.isa.cristal.ram2bpmn.templates;

import es.us.isa.cristal.ram.Activity;

import java.util.Collections;
import java.util.Set;

/**
 * DefaultProcessTemplates
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class DefaultTemplateAssignment extends AbstractTemplateAssignment {

    private Template template;

    public DefaultTemplateAssignment(Template template) {
        this.template = template;
    }

    @Override
    public Template getTemplate(Activity task) {
        return template;
    }

    @Override
    public Set<Activity> listActivities() {
        return Collections.emptySet();
    }
}
