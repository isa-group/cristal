package es.us.isa.cristal.ram2bpmn.templates;

import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.cristal.ram.RamActivity;

/**
 * NonCompatibleTemplate
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class NonCompatibleTemplate extends RuntimeException {

    private RamActivity ramActivity;

    public NonCompatibleTemplate(Class<? extends Template> templateClass, RamActivity ramActivity) {
        super(templateClass.getName() + " is not compatible with activity " + ramActivity.getActivityName());
        this.ramActivity = ramActivity;
    }

    public RamActivity getRamActivity() {
        return ramActivity;
    }
}
