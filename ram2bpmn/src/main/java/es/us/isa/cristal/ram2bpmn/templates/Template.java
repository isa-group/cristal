package es.us.isa.cristal.ram2bpmn.templates;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.cristal.ram.Activity;
import es.us.isa.cristal.ram.RAM;
import es.us.isa.cristal.ram2bpmn.templates.fragmenttemplates.FragmentTemplate;
import es.us.isa.cristal.ram2bpmn.templates.statictemplates.StaticTemplate;

/**
 * Template
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StaticTemplate.class, name = "static"),
        @JsonSubTypes.Type(value = FragmentTemplate.class, name = "fragment")
})
public interface Template {
    TemplateInstance instantiate(TTask task, RAM matrix) throws NonCompatibleTemplate;
    boolean isCompatible(RAM matrix, Activity ramActivity);
}
