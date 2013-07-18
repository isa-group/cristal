package es.us.isa.cristal.model.expressions;

import es.us.isa.cristal.model.constraints.Constraint;

public abstract class RALExpr {
    public abstract boolean hasRuntimeConstraint();
    public abstract Constraint[] getConstraints();
}
