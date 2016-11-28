package es.us.isa.cristal.ram;

import es.us.isa.bpmn.xmlClasses.bpmn20.TActivity;

/**
 * Activity
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class Activity {
    private String name;

    private Activity() {

    }

    public Activity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Activity fromBpmn(TActivity activity) {
        return new Activity(activity.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;

        Activity activity = (Activity) o;

        return name.equals(activity.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
