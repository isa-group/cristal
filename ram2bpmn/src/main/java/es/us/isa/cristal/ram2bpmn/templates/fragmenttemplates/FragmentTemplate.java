package es.us.isa.cristal.ram2bpmn.templates.fragmenttemplates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.Bpmn20ModelHandlerImpl;
import es.us.isa.bpmn.xmlClasses.bpmn20.TProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.cristal.ram.Activity;
import es.us.isa.cristal.ram.Mapping;
import es.us.isa.cristal.ram.RAM;
import es.us.isa.cristal.ram2bpmn.processes.ProcessHandler;
import es.us.isa.cristal.ram2bpmn.templates.*;
import org.mvel2.MVEL;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * FragmentTemplate
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class FragmentTemplate extends AbstractTemplate {
    @JsonProperty("code")
    private String code;
    private Serializable codeCompiled;

    @JsonProperty("fragments")
    private Map<String, String> fragments;


    @JsonCreator
    public FragmentTemplate(@JsonProperty("cardinalities") Cardinalities cardinalities,
                            @JsonProperty("code") String code,
                            @JsonProperty("fragments") Map<String, String> fragments,
                            @JsonProperty("mapping") Mapping mapping) {
        super(cardinalities, mapping);
        this.code = code;
        this.codeCompiled = MVEL.compileExpression(code);
        this.fragments = fragments;
    }


    public String getCode() {
        return code;
    }

    public Map<String, String> getFragments() {
        return fragments;
    }

    @Override
    public TemplateInstance instantiate(TTask task, RAM matrix) throws NonCompatibleTemplate {
        if (!isCompatible(matrix, Activity.fromBpmn(task))) {
            throw new NonCompatibleTemplate(getClass(), Activity.fromBpmn(task));
        }

        Bpmn20ModelHandler baseProcessHandler = new Bpmn20ModelHandlerImpl();
        try {
            baseProcessHandler.load(getClass().getResourceAsStream("/basicfragment.xml"));
        } catch (Exception e) {
            throw new RuntimeException("Unable to read XML");
        }
        ProcessHandler baseProcess = new ProcessHandler(baseProcessHandler.getDefinitions());

        Fragment result = (Fragment) MVEL.executeExpression(codeCompiled, buildContext(task, matrix, baseProcess.getProcess()));

        ConvertToSubprocess conversor = ConvertToSubprocess.fromFragment(result);

        return new TemplateInstance(conversor.convert(task), baseProcess.getParticipants(), baseProcess.getMessageFlows());
    }

    private Map<String, Object> buildContext(TTask task, RAM matrix, TProcess process) {
        Map<String, Object> context = buildFillers(task, matrix);
        context.put("process", Fragment.fromProcess(process));
        context.put("fragments", new FragmentsCompiled(fragments, task, matrix));

        return context;
    }

    public class FragmentsCompiled {
        private Map<String, CompiledTemplate> fragmentTemplates;
        private TTask task;
        private RAM matrix;

        public FragmentsCompiled(Map<String, String> fragments, TTask task, RAM matrix) {
            this.task = task;
            this.matrix = matrix;
            fragmentTemplates = new HashMap<>();
            fragments.forEach((key, fragment) -> fragmentTemplates.put(key, TemplateCompiler.compileTemplate(fragment)));
        }

        public Fragment replace(String fragmentName) {
            Map<String, Object> context = buildFillers(task, matrix);
            return processFragment(fragmentName, context);
        }

        private Fragment processFragment(String fragmentName, Map<String, Object> context) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TemplateRuntime.execute(fragmentTemplates.get(fragmentName), context, outputStream);
            Bpmn20ModelHandler handler = new Bpmn20ModelHandlerImpl();
            try {
                handler.load(new ByteArrayInputStream(outputStream.toByteArray()));
            } catch (Exception e) {
                throw new RuntimeException("Unable to read XML", e);
            }
            ProcessHandler process = new ProcessHandler(handler.getDefinitions());
            return Fragment.fromProcess(process.getProcess());
        }

        public Fragment replace(String fragmentName, Map<String, Object> params) {
            Map<String, Object> context = buildFillers(task, matrix);
            context.putAll(params);
            return processFragment(fragmentName, context);
        }
    }
}
