package es.us.isa.cristal;

import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.parser.RALParser;

import java.lang.annotation.Target;
import java.util.*;

/**
 * User: resinas
 * Date: 13/07/13
 * Time: 10:39
 */
public class RALResourceAssignment extends ResourceAssignment<RALExpr> {

    public RALResourceAssignment() {
        super();
    }


    public void addFromString(ResourceAssignment<String> assignments) {
        for (Target t : assignments.assignments.keySet()) {
            add(t.getActivity(), t.getDuty(), RALParser.parse(assignments.assignments.get(t)));
        }
    }

}
