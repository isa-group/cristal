package es.us.isa.cristal.resolver;

import java.util.List;

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
        	// added toString() invocation to force getting a string. 
        	//TODO: is necessary to deal with objects?
            DataConstraint dc = (DataConstraint) c;
            result = engine.getDataValue(pid, dc.getData(), dc.getField()).toString();
        }
        else if (c instanceof ActivityConstraint) {
        	//modified to return the first result of the list<String>
        	//TODO: make a decision:
        	//      1. build the correct result string based on the list of performers (build the query)
        	//      2. change the resolve method to return a complex data structure which allows dealing
        	//         with string, list<string> or different structures.
            List<String> performers = engine.getActivityPerformer(pid, ((ActivityConstraint) c).getActivityName());
            result = !performers.isEmpty() ? performers.get(0) : null;
        }
        else {
            throw new RuntimeException("Could not resolve the constraint");
        }

        return result;
    }
}
