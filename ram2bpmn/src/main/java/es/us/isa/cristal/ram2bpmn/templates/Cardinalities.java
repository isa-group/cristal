package es.us.isa.cristal.ram2bpmn.templates;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import es.us.isa.cristal.ram.Responsibility;
import es.us.isa.cristal.ram.responsibilities.RASCI;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Cardinalities
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class Cardinalities {

    public static class Cardinality {
        private Integer min = 0;
        private Integer max = null;


        public Cardinality() {

        }

        public Cardinality(Integer min, Integer max) {
            this.min = min;
            this.max = max;
        }

        public Integer getMin() {
            return min;
        }

        public Integer getMax() {
            return max;
        }
    }


    private Map<Responsibility, Cardinality> cardinalityMap = new HashMap<>();

    @JsonAnyGetter
    public Map<Responsibility,Cardinality> getMap() {
        return cardinalityMap;
    }

    public Cardinality get(Responsibility r) {
        return cardinalityMap.get(r);
    }

    public Set<Responsibility> responsibilities() {
        return cardinalityMap.keySet();
    }

    @JsonAnySetter
    public void add(String responsibility, Cardinality card) {
        try {
            RASCI rasci =  RASCI.valueOf(responsibility);
            add(rasci, card);
        } catch (Exception e) {
            throw new RuntimeException("Responsibility non supported", e);
        }

    }

    public void add(Responsibility responsibility, Cardinality card) {
        cardinalityMap.put(responsibility, card);
    }

    public Cardinalities add(Responsibility responsibility, Integer min, Integer max) {
        add(responsibility, new Cardinality(min, max));
        return this;
    }



}
