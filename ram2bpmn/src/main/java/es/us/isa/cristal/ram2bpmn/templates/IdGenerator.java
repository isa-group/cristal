package es.us.isa.cristal.ram2bpmn.templates;

import es.us.isa.bpmn.xmlClasses.bpmn20.TBaseElement;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * IdGenerator
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class IdGenerator {
    public static String random() {
        return "ram2bpmn-" + UUID.randomUUID().toString();
    }

    public static <T extends TBaseElement> Collection<T> makeUniqueIds(Collection<T> elements) {
        Map<String, String> mapIds = elements.stream()
                .filter(elem -> elem.getId() != null)
                .collect(Collectors.toMap(T::getId, elem -> IdGenerator.random()));

        elements.forEach(el -> el.setId(mapIds.get(el.getId())));

        return elements;
    }


}
