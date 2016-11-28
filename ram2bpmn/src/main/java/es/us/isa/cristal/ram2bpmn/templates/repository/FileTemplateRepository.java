package es.us.isa.cristal.ram2bpmn.templates.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import es.us.isa.cristal.ram2bpmn.templates.Template;
import es.us.isa.cristal.ram2bpmn.templates.TemplateAssignment;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * FileTemplateRepository
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class FileTemplateRepository implements TemplateRepository {
    private MemoryTemplateRepository memoryRepository;
    private File directory;

    public FileTemplateRepository(File directory) {
        this.directory = directory;
        this.memoryRepository = new MemoryTemplateRepository();
        loadTemplates();
    }

    private void loadTemplates() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(Template.class);

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file: files) {
                try {
                    String name = file.getName();
                    String id = name.split("\\.")[0];
                    memoryRepository.add(id, reader.readValue(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
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
