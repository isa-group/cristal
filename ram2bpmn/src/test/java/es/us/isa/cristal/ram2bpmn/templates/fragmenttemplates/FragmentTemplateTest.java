package es.us.isa.cristal.ram2bpmn.templates.fragmenttemplates;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.cristal.ram.RAM;
import es.us.isa.cristal.ram.mappings.CamundaMapping;
import es.us.isa.cristal.ram.responsibilities.RASCI;
import es.us.isa.cristal.ram2bpmn.templates.AbstractTemplateTest;
import es.us.isa.cristal.ram2bpmn.templates.Cardinalities;
import es.us.isa.cristal.ram2bpmn.templates.Template;
import es.us.isa.cristal.ram2bpmn.templates.TemplateInstance;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * FragmentTemplateTest
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class FragmentTemplateTest extends AbstractTemplateTest {

    @Test
    public void testInstantiate() throws Exception {
        FragmentTemplate template = buildTemplate();

        Bpmn20ModelHandler handler = loadSourceModel();
        TTask targetTask = getTargetTask(handler);

        RAM matrix = new RAM();
        matrix.add(
                boundedRole("Write Paper", "asdf", RASCI.responsible),
                boundedRole("Write Paper", "dfas", RASCI.support),
                boundedRole("Write Paper", "dfas", RASCI.accountable)
        );

        TemplateInstance instance = template.instantiate(targetTask, matrix);

        updateHandler(handler, targetTask, instance);

        handler.save(System.out);

    }

    @Test
    public void testInstantiateWithoutSupport() throws Exception {
        FragmentTemplate template = buildTemplate();

        Bpmn20ModelHandler handler = loadSourceModel();
        TTask targetTask = getTargetTask(handler);

        RAM matrix = new RAM();
        matrix.add(
                boundedRole("Write Paper", "asdf", RASCI.responsible),
                boundedRole("Write Paper", "dfas", RASCI.accountable)
        );

        TemplateInstance instance = template.instantiate(targetTask, matrix);

        updateHandler(handler, targetTask, instance);

        handler.save(System.out);

    }

    @Test
    public void testInstantiateInformed() throws Exception {
        FragmentTemplate template = buildTemplate();

        Bpmn20ModelHandler handler = loadSourceModel();
        TTask targetTask = getTargetTask(handler);

        RAM matrix = new RAM();
        matrix.add(
                boundedRole("Write Paper", "asdf", RASCI.responsible),
                boundedRole("Write Paper", "dfas", RASCI.accountable),
                boundedRole("Write Paper", "inf1", RASCI.informed),
                boundedRole("Write Paper", "inf2", RASCI.informed)
        );

        TemplateInstance instance = template.instantiate(targetTask, matrix);

        updateHandler(handler, targetTask, instance);

        handler.save(System.out);

    }

    private FragmentTemplate buildTemplate() {
        Map<String, String> fragments = new HashMap<>();
        fragments.put("responsible", loadTemplateFile("responsible.xml"));
        fragments.put("accountable", loadTemplateFile("accountable.xml"));
        fragments.put("support", loadTemplateFile("support.xml"));
        fragments.put("consult", loadTemplateFile("consult.xml"));
        fragments.put("inform", loadTemplateFile("inform.xml"));

        return new FragmentTemplate(
                new Cardinalities()
                        .add(RASCI.responsible, 1, 1)
                        .add(RASCI.support, 0, 1)
                        .add(RASCI.consult, 0, 1)
                        .add(RASCI.accountable, 0, 1)
                        .add(RASCI.informed, 0, null),
                loadTemplateFile("code"),
                fragments,
                new CamundaMapping()
        );
    }

    @Test
    public void jsonSerialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        FragmentTemplate template = buildTemplate();

        System.out.println(mapper.writeValueAsString(template));
    }

    @Test
    public void jsonDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Template template = mapper.readValue(getClass().getResourceAsStream("/testfragmenttemplate.json"), Template.class);

        Assert.assertTrue(template instanceof FragmentTemplate);
        FragmentTemplate fragmentTemplate = (FragmentTemplate) template;
        Assert.assertTrue(fragmentTemplate.minCardinality(RASCI.responsible) == 1);
        Assert.assertTrue(fragmentTemplate.maxCardinality(RASCI.support) == 1);
        Assert.assertTrue(fragmentTemplate.maxCardinality(RASCI.informed) == Integer.MAX_VALUE);
        Assert.assertTrue(fragmentTemplate.templateResponsibilities().contains(RASCI.accountable));
        Assert.assertTrue(! fragmentTemplate.getDescription().isEmpty());

    }
}