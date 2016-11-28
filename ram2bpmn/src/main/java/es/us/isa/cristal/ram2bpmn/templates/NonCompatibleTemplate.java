package es.us.isa.cristal.ram2bpmn.templates;

import es.us.isa.cristal.ram.Activity;

/**
 * NonCompatibleTemplate
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class NonCompatibleTemplate extends RuntimeException {

    private Activity ramActivity;

    public NonCompatibleTemplate(Class<? extends Template> templateClass, Activity ramActivity) {
        super(templateClass.getName() + " is not compatible with activity " + ramActivity.getName());
        this.ramActivity = ramActivity;
    }

    public Activity getRamActivity() {
        return ramActivity;
    }
}
