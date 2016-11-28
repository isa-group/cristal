package es.us.isa.cristal.ram2bpmn.templates.statictemplates;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.cristal.ram.Activity;
import es.us.isa.cristal.ram.BoundedRole;
import es.us.isa.cristal.ram.RAM;
import es.us.isa.cristal.ram.Role;
import es.us.isa.cristal.ram.mappings.RalMapping;
import es.us.isa.cristal.ram.responsibilities.RASCI;
import es.us.isa.cristal.ram2bpmn.templates.Cardinalities;
import es.us.isa.cristal.ram2bpmn.templates.Template;
import es.us.isa.cristal.ram2bpmn.templates.TemplateInstance;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * StaticTemplateTest
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class StaticTemplateTest extends es.us.isa.cristal.ram2bpmn.templates.AbstractTemplateTest {

    @Test
    public void testInstantiate() throws Exception {
        StaticTemplate template = buildStaticTemplate("/testtemplate.xml");


        Bpmn20ModelHandler handler = loadSourceModel();
        TTask sourceTask = getTargetTask(handler);
        TemplateInstance instance = template.instantiate(sourceTask, buildMatrix());
        updateHandler(handler, sourceTask, instance);


        handler.save(System.out);
    }

    @Test
    public void testInstantiateTemplate() throws Exception {
        StaticTemplate template = buildStaticTemplate("/testtemplateWithPlaceholders.xml");


        Bpmn20ModelHandler handler = loadSourceModel();
        TTask sourceTask = getTargetTask(handler);
        TemplateInstance instance = template.instantiate(sourceTask, buildMatrix());
        updateHandler(handler, sourceTask, instance);


        handler.save(System.out);
    }

    @Test
    public void testFullTemplate() throws Exception {
        StaticTemplate template = buildStaticFullTemplate();


        Bpmn20ModelHandler handler = loadSourceModel();
        TTask sourceTask = getTargetTask(handler);
        TemplateInstance instance = template.instantiate(sourceTask, buildMatrix());
        updateHandler(handler, sourceTask, instance);


        handler.save(System.out);
    }

    private StaticTemplate buildStaticTemplate(String name) {
        String result = loadTemplateFile(name);
        Cardinalities cardinality = buildCardinality();
        return new StaticTemplate(cardinality, result, new RalMapping());
    }

    private StaticTemplate buildStaticFullTemplate() {
        String bpmn = loadTemplateFile("fulltemplate.xml");
        return new StaticTemplate(
                new Cardinalities()
                        .add(RASCI.responsible, 1, 1)
                        .add(RASCI.support, 0, 1)
                        .add(RASCI.consult, 0, 1)
                        .add(RASCI.accountable, 0, 1)
                        .add(RASCI.informed, 0, null),
                bpmn,
                new RalMapping()
        );
    }

    @Test
    public void jsonSerialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        StaticTemplate template = buildStaticFullTemplate();

        System.out.println(mapper.writeValueAsString(template));
    }

    @Test
    public void jsonDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Template template = mapper.readValue(getClass().getResourceAsStream("/testtemplate.json"), Template.class);

        Assert.assertTrue(template instanceof StaticTemplate);
        StaticTemplate staticTemplate = (StaticTemplate) template;
        Assert.assertTrue(staticTemplate.minCardinality(RASCI.responsible) == 1);
        Assert.assertTrue(staticTemplate.maxCardinality(RASCI.responsible) == Integer.MAX_VALUE);
        Assert.assertTrue(staticTemplate.templateResponsibilities().contains(RASCI.responsible));
    }

    private Cardinalities buildCardinality() {
        Cardinalities cardinality = new Cardinalities();
        cardinality.add(RASCI.responsible, new Cardinalities.Cardinality(1, null));
        return cardinality;
    }

    private RAM buildMatrix() {
        RAM matrix = new RAM();
        matrix.add(new BoundedRole(new Activity("Write Paper"), RASCI.responsible, new Role("PhD Student") ));
        return matrix;
    }
}