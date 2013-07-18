package es.us.isa.cristal.resolver;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.constraints.ActivityConstraint;
import es.us.isa.cristal.model.constraints.Constraint;
import es.us.isa.cristal.model.constraints.DataConstraint;
import es.us.isa.cristal.model.constraints.IdConstraint;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 20:07
 */
public class ConstraintResolver {
    private BPEngine engine;
    private Object pid;

    public ConstraintResolver(BPEngine eng, Object pid) {
        this.engine = eng;
        this.pid = pid;
    }

    public String resolve(Constraint c) {
        String result;

        if (c instanceof IdConstraint) {
            result = ((IdConstraint) c).getId();
        }
        else if (c instanceof DataConstraint) {
            DataConstraint dc = (DataConstraint) c;
            result = engine.getDataValue(pid, dc.getData(), dc.getField());
        }
        else if (c instanceof ActivityConstraint) {
            result = engine.getActivityPerformer(pid, ((ActivityConstraint) c).getActivityName());
        }
        else {
            throw new RuntimeException("Could not resolve the constraint");
        }

        return result;
    }
}
