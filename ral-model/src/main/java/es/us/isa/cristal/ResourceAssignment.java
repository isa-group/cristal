package es.us.isa.cristal;

import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;

import java.util.*;

/**
 * User: resinas
 * Date: 13/07/13
 * Time: 10:39
 */
public class ResourceAssignment {

    private Map<Target, RALExpr> assignments;

    public ResourceAssignment() {
        assignments = new HashMap<Target, RALExpr>();
    }

    public ResourceAssignment add(String activity, TaskDuty duty, RALExpr expr) {
        Target t = new Target(activity, duty);
        assignments.put(t, expr);
        return this;
    }

    public RALExpr get(String activity, TaskDuty duty) {
        Target t = new Target(activity, duty);
        return assignments.get(t);
    }

    public ResourceAssignment add(String activity, RALExpr expr) {
        add(activity, TaskDuty.RESPONSIBLE, expr);
        return this;
    }

    public Map<TaskDuty, RALExpr> getByTaskDuty(String activity) {
        Map<TaskDuty, RALExpr> result = new HashMap<TaskDuty, RALExpr>();
        for (Target t : assignments.keySet()) {
            if (t.getActivity().equals(activity)) {
                result.put(t.getDuty(), assignments.get(t));
            }
        }

        return result;
    }

    public Collection<Assignment> getAll() {
        List<Assignment> all = new ArrayList<Assignment>();
        for (Target t : assignments.keySet()) {
            all.add(new Assignment(t.getActivity(), t.getDuty(), assignments.get(t)));
        }

        return all;
    }

    public class Assignment {
        private String activity;
        private TaskDuty duty;
        private RALExpr expr;

        public Assignment(String activity, TaskDuty duty, RALExpr expr) {
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

        public RALExpr getExpr() {
            return expr;
        }
    }

    private class Target {
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
