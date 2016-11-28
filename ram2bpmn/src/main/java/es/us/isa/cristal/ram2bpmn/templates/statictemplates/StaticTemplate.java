package es.us.isa.cristal.ram2bpmn.templates.statictemplates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.Bpmn20ModelHandlerImpl;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.cristal.ram.Activity;
import es.us.isa.cristal.ram.Mapping;
import es.us.isa.cristal.ram.RAM;
import es.us.isa.cristal.ram2bpmn.processes.ProcessHandler;
import es.us.isa.cristal.ram2bpmn.templates.*;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * StaticTemplate
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class StaticTemplate extends AbstractTemplate {
    @JsonProperty("xml")
    private String xml;

    @JsonIgnore
    private CompiledTemplate template;

    @JsonCreator
    public StaticTemplate(@JsonProperty("cardinalities") Cardinalities cardinalities,
                          @JsonProperty("xml") String xml,
                          @JsonProperty("mapping") Mapping mapping) {
        super(cardinalities, mapping);
        this.xml = xml;
        template = TemplateCompiler.compileTemplate(xml);
    }

    @Override
    public TemplateInstance instantiate(TTask task, RAM matrix) throws NonCompatibleTemplate {
        if (!isCompatible(matrix, Activity.fromBpmn(task))) {
            throw new NonCompatibleTemplate(getClass(), Activity.fromBpmn(task));
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (template == null) {
            template = TemplateCompiler.compileTemplate(xml);
        }
        TemplateRuntime.execute(template, buildFillers(task, matrix), outputStream);

        Bpmn20ModelHandler handler = new Bpmn20ModelHandlerImpl();
        try {
            handler.load(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (Exception e) {
            throw new RuntimeException("Unable to read XML");
        }

        ProcessHandler process = new ProcessHandler(handler.getDefinitions());

        ConvertToSubprocess conversor = ConvertToSubprocess.fromProcess(process.getProcess());


        return new TemplateInstance(conversor.convert(task), process.getParticipants(), process.getMessageFlows());
    }

}
