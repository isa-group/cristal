package es.us.isa.cristal.ram2bpmn.templates.rasci;

import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.cristal.ram.RamActivity;
import es.us.isa.cristal.ram2bpmn.templates.NonCompatibleTemplate;
import es.us.isa.cristal.ram2bpmn.templates.Template;
import es.us.isa.cristal.ram2bpmn.templates.TemplateInstance;

import static es.us.isa.cristal.ram2bpmn.templates.rasci.RasciDuties.*;

/**
 * RasciTemplate
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class RasciTemplate implements Template {

    @Override
    public TemplateInstance instantiate(TTask task, RamActivity ramActivity) {
        if (!isCompatible(ramActivity)) {
            throw new NonCompatibleTemplate(getClass(), ramActivity);
        }

        RasciTemplateInstance instance = new RasciTemplateInstance(task);
        instance.buildSubprocess(ramActivity);

        return instance;
    }

    @Override
    public boolean isCompatible(RamActivity ramActivity) {
        return RasciDuties.all().containsAll(ramActivity.getDutiesDefined())
                && ramActivity.numberOf(RESPONSIBLE) == 1
                && ramActivity.numberOf(ACCOUNTABLE) <= 1;
    }
}
