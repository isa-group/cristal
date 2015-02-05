package es.us.isa.cristal.ram2bpmn.templates;

import es.us.isa.bpmn.xmlClasses.bpmn20.*;

import java.util.ArrayList;
import java.util.List;

/**
 * TemplateInstance
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class TemplateInstance {
    protected TSubProcess subprocess;
    protected List<TParticipant> participants;
    protected List<TMessageFlow> messageFlows;

    public TemplateInstance(TSubProcess subprocess) {
        this.subprocess = subprocess;
        this.participants = new ArrayList<>();
        this.messageFlows = new ArrayList<>();
    }

    public TemplateInstance(TSubProcess subprocess, List<TParticipant> participants, List<TMessageFlow> messageFlows) {
        this.subprocess = subprocess;
        this.participants = participants;
        this.messageFlows = messageFlows;
    }

    public TSubProcess getSubprocess() {
        return subprocess;
    }

    public List<TParticipant> getParticipants() {
        return participants;
    }

    public List<TMessageFlow> getMessageFlows() {
        return messageFlows;
    }
}
