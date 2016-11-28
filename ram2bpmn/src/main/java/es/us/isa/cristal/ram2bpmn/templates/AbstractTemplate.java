package es.us.isa.cristal.ram2bpmn.templates;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.cristal.ram.*;
import es.us.isa.cristal.ram.responsibilities.RASCI;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * AbstractTemplate
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public abstract class AbstractTemplate implements Template {

    @JsonProperty("description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected String description;

    @JsonProperty("mapping")
    protected Mapping mapping;

    @JsonProperty("cardinalities")
    protected Cardinalities cardinalities;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public AbstractTemplate(Cardinalities cardinalities,
                            Mapping mapping) {
        this.cardinalities = cardinalities;
        this.mapping = mapping;
    }

    protected Map<String, Object> buildFillers(TTask task, RAM matrix) {
        Map<String, Object> fillers = new HashMap<>();

        fillers.put("activityName", task.getName());
        fillers.put("activityId", task.getId());
        fillers.put("responsibleBoD", mapping.responsibleBoD(Activity.fromBpmn(task), matrix));
        fillers.put("f", new Context(matrix, task, mapping));
        fillers.put("rasci", new RasciMapping());

        return fillers;
    }

    public Integer minCardinality(Responsibility r) {
        return cardinalities.get(r).getMin();
    }

    public Integer maxCardinality(Responsibility r) {
        Integer max = cardinalities.get(r).getMax();
        return max == null ? Integer.MAX_VALUE : max;
    }

    public Set<Responsibility> templateResponsibilities() {
        return cardinalities.responsibilities();
    }

    @Override
    public boolean isCompatible(RAM matrix, Activity ramActivity) {
        Set<Responsibility> responsibilities = matrix.duties(ramActivity);
        return !responsibilities.isEmpty() && responsibilities.stream().allMatch(td -> {
            int size = matrix.filter(ramActivity, td).size();
            return (templateResponsibilities().contains(td) && minCardinality(td) <= size && size <= maxCardinality(td));
        });

    }

    protected static class RasciMapping {
        public Responsibility getResponsible() {
            return RASCI.responsible;
        }
        public Responsibility getAccountable() {
            return RASCI.accountable;
        }
        public Responsibility getConsulted() {
            return RASCI.consult;
        }
        public Responsibility getInformed() {
            return RASCI.informed;
        }
        public Responsibility getSupport() {
            return RASCI.support;
        }
    }

    public static class Context {
        private RAM matrix;
        private Activity activity;
        private Mapping mapping;

        public Context(RAM matrix, TTask task, Mapping mapping) {
            this.matrix = matrix;
            this.activity = Activity.fromBpmn(task);
            this.mapping = mapping;
        }

        public Set<Responsibility> duties() {
            return matrix.duties(activity);
        }

        public boolean anyTD() {
            return matrix.anyTD(activity);
        }

        public Set<BoundedRole> filter(Responsibility r) {
            return matrix.filter(activity, r);
        }

        public boolean hasTD(Responsibility r) {
            return matrix.hasTD(activity, r);
        }

        public boolean onlyTD(Responsibility r) {
            return matrix.onlyTD(activity, r);
        }

        public String map(BoundedRole boundedRole) {
            return mapping.map(boundedRole);
        }

        public String orMap(Responsibility r) {
            return mapping.orMap(matrix, activity, r);
        }

        public String andMap(Responsibility r) {
            return mapping.andMap(matrix, activity, r);
        }
    }
}
