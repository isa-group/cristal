package es.us.isa.cristal.ram2bpmn.templates.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import es.us.isa.cristal.ram2bpmn.templates.Template;
import es.us.isa.cristal.ram2bpmn.templates.TemplateAssignment;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * SingleFileTemplateRepository
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class SingleFileTemplateRepository implements TemplateRepository {

    private MemoryTemplateRepository memoryRepository;
    private InputStream inputStream;

    public SingleFileTemplateRepository(InputStream file) {
        this.inputStream = file;
        this.memoryRepository = new MemoryTemplateRepository();
        loadTemplates();
    }

    private void loadTemplates() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(new TypeReference<HashMap<String,Template>>() {});

        try {
            HashMap<String,Template> templates = reader.readValue(inputStream);
            for (String key : templates.keySet()) {
                memoryRepository.add(key, templates.get(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Template getTemplate(String template) {
        return memoryRepository.getTemplate(template);
    }

    @Override
    public TemplateAssignment buildAssignmentFrom(Map<String, String> assignment) {
        return memoryRepository.buildAssignmentFrom(assignment);
    }

    @Override
    public Set<String> listTemplateNames() {
        return memoryRepository.listTemplateNames();
    }
}
