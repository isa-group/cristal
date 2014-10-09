package es.us.isa.cristal;

import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;

import java.util.*;

/**
 * User: resinas
 * Date: 13/07/13
 * Time: 10:39
 */
public class ResourceAssignment<T> {

    protected Map<Target, T> assignments;

    public ResourceAssignment() {
        assignments = new HashMap<Target, T>();
    }

    public <R extends ResourceAssignment<T>> R add(String activity, TaskDuty duty, T expr) {
        Target t = new Target(activity, duty);
        assignments.put(t, expr);
        return (R) this;
    }

    public <R extends ResourceAssignment<T>> R add(String activity, T expr) {
        return (R) add(activity, TaskDuty.RESPONSIBLE, expr);
    }

    public ResourceAssignment<T> addAll(Map<String, T> assignmentsMap, TaskDuty duty) {
        for (Map.Entry<String, T> entry : assignmentsMap.entrySet()) {
            add(entry.getKey(), duty, entry.getValue());
        }
        return this;
    }

    public ResourceAssignment<T> addAll(Map<String, T> assignmentsMap) {
        return addAll(assignmentsMap, TaskDuty.RESPONSIBLE);
    }

    public T get(String activity, TaskDuty duty) {
        Target t = new Target(activity, duty);
        return assignments.get(t);
    }

    public Map<TaskDuty, T> getByActivity(String activity) {
        Map<TaskDuty, T> result = new HashMap<TaskDuty, T>();
        for (Target t : assignments.keySet()) {
            if (t.getActivity().equals(activity)) {
                result.put(t.getDuty(), assignments.get(t));
            }
        }

        return result;
    }

    public Map<String, T> getByTaskDuty(TaskDuty taskDuty) {
        Map<String, T> result = new HashMap<String, T>();
        for (Target t : assignments.keySet()) {
            if (taskDuty.equals(t.getDuty())) {
                result.put(t.getActivity(), assignments.get(t));
            }
        }

        return result;
    }

    public Collection<String> getActivities() {
        List<String> activities = new ArrayList<String>();
        for (Target t : assignments.keySet()) {
            activities.add(t.getActivity());
        }

        return activities;
    }

    public Collection<Assignment<T>> getAll() {
        List<Assignment<T>> all = new ArrayList<Assignment<T>>();
        for (Target t : assignments.keySet()) {
            all.add(new Assignment(t.getActivity(), t.getDuty(), assignments.get(t)));
        }

        return all;
    }

    public class Assignment<TA> {
        private String activity;
        private TaskDuty duty;
        private TA expr;

        public Assignment(String activity, TaskDuty duty, TA expr) {
            this.activity = activity;
            this.duty = duty;
            this.expr = expr;
        }

        public String getActivity() {
            return activity;
        }

        public TaskDuty getDuty() {
            return duty;
        }

        public TA getExpr() {
            return expr;
        }
    }

    protected static class Target {
        private String activity;
        private TaskDuty duty;

        public Target(String activity, TaskDuty duty) {
            this.activity = activity;
            this.duty = duty;
        }

        public String getActivity() {
            return activity;
        }

        public TaskDuty getDuty() {
            return duty;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Target that = (Target) o;

            if (activity != null ? !activity.equals(that.activity) : that.activity != null) return false;
            if (duty != that.duty) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = activity != null ? activity.hashCode() : 0;
            result = 31 * result + (duty != null ? duty.hashCode() : 0);
            return result;
        }
    }
}
