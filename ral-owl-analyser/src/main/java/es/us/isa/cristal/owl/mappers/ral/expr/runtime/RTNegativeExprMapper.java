package es.us.isa.cristal.owl.mappers.ral.expr.runtime;

import es.us.isa.cristal.model.constraints.Constraint;
import es.us.isa.cristal.model.constraints.PersonWhoDidActivityConstraint;
import es.us.isa.cristal.model.expressions.NegativeExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.DLQueryEngine;
import es.us.isa.cristal.owl.ontologyhandlers.LogOntologyHandler;
import es.us.isa.cristal.owl.mappers.ral.ExprMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.LogMapper;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static es.us.isa.cristal.owl.Definitions.*;


/**
 * User: resinas
 * Date: 04/07/13
 * Time: 16:35
 */
public class RTNegativeExprMapper implements ExprMapper {
    private OwlRalMapper mapper;
    private LogOntologyHandler logOntologyHandler;
    private IdMapper idMapper;

    public RTNegativeExprMapper(OwlRalMapper mapper, IdMapper idMapper, LogOntologyHandler logOntologyHandler) {
        this.mapper = mapper;
        this.idMapper = idMapper;
        this.logOntologyHandler = logOntologyHandler;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return NegativeExpr.class;
    }

    @Override
    public String map(RALExpr expr, Object pid) {
        NegativeExpr e = (NegativeExpr) expr;
        String map;

        if (e.hasRuntimeConstraint()) {
            map = PERSON;

            List<PersonWhoDidActivityConstraint> constraints = filterACConstraint(e.getConstraints());
            for (PersonWhoDidActivityConstraint c : constraints) {
//                if (activityAlreadyAllocatedAndNotInLoop(c.getActivityName(), pid)) {
                    String activity = idMapper.mapActivity(c.getActivityName());
                    String currentInstance = new LogMapper().map(pid.toString());
                    map =  PERSON + " AND not(inverse("+HASRESPONSIBLE + ") some ( inverse("+HASACTIVITYINSTANCE+") value "+ currentInstance + " AND " + ISOFTYPE + " value " + activity + " AND "+ HASSTATE +" some " + AFTERALLOCATION + "))";
//                }
            }

        }
        else
            map = PERSON + " and ( not ( " + mapper.map(e.getExprObject(), pid) + "))";

        return map;
    }

    private List<PersonWhoDidActivityConstraint> filterACConstraint(Constraint[] constraints) {
        List<PersonWhoDidActivityConstraint> result = new ArrayList<PersonWhoDidActivityConstraint>();
        for (Constraint c : constraints) {
            if (c instanceof PersonWhoDidActivityConstraint) {
                result.add((PersonWhoDidActivityConstraint)c);
            }
        }

        return result;
    }

    private boolean activityAlreadyAllocatedAndNotInLoop(String activityName, Object pid) {
        String activity = idMapper.mapActivity(activityName);
        String currentInstance = new LogMapper().map(pid.toString());
        DLQueryEngine engine = logOntologyHandler.createDLQueryEngine();

        String activitiesNotInLoop = "not(" + WEAKORDER + " some Self)";
        String activitiesAlreadyStarted = "inverse(" + ISOFTYPE + ") some (inverse (" + HASACTIVITYINSTANCE + ") value " + currentInstance + " and " + HASSTATE + " some " + AFTERALLOCATION + ")";
        Set<OWLNamedIndividual> i = engine.getInstances("{" + activity + "} AND " + activitiesAlreadyStarted + " AND " + activitiesNotInLoop, false);
        return i.size() > 0;
    }

}
