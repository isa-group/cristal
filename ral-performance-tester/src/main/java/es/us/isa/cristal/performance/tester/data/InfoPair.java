package es.us.isa.cristal.performance.tester.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * InfoPair
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class InfoPair {
    private String key;
    private Object value;

    public InfoPair(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public static Map<String, Object> toMap(Collection<InfoPair> pairs) {
        Map<String, Object> map = new HashMap<>();
        for (InfoPair p : pairs) {
            map.put(p.key, p.value);
        }
        return map;
    }
}
