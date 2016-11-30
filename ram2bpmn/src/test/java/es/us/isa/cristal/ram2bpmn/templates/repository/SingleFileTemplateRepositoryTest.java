package es.us.isa.cristal.ram2bpmn.templates.repository;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 * SingleFileTemplateRepositoryTest
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class SingleFileTemplateRepositoryTest {

    @Test
    public void testListTemplateNames() throws Exception {
        SingleFileTemplateRepository repo = new SingleFileTemplateRepository(this.getClass().getResourceAsStream("singlerepo.json"));
        Set<String> templ = repo.listTemplateNames();
        Assert.assertTrue(templ.contains("fragment-rasci"));
        Assert.assertTrue(templ.contains("static-rasci"));
    }
}