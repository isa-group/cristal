package es.us.isa.cristal.ram2bpmn.templates;

import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.cristal.ram.RamActivity;

/**
 * Template
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public interface Template {
    public TemplateInstance instantiate(TTask task, RamActivity ramActivity);

    public boolean isCompatible(RamActivity ramActivity);
}
