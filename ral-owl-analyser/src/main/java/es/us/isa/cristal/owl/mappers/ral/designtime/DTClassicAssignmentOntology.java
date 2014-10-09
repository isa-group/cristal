package es.us.isa.cristal.owl.mappers.ral.designtime;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.RALResourceAssignment;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.DLQueryEngine;
import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.analysers.DTRALAnalyser;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.ActivityMapperClassic;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.ontologyhandlers.AssignmentOntology;

/**
* User: resinas
* Date: 13/07/13
* Time: 11:01
*/
public class DTClassicAssignmentOntology extends AssignmentOntology {

    private OwlRalMapper owlRalMapper;
    private IdMapper idMapper;

    public DTClassicAssignmentOntology(OntologyHandler ontologyHandler, IdMapper idMapper, BPEngine engine) {
        super(ontologyHandler);
        this.idMapper = idMapper;
        owlRalMapper = new DTClassicOwlRalMapper(idMapper, engine, new ActivityMapperClassic(idMapper));
    }

    @Override
    public void buildOntology(RALResourceAssignment assignment) {
        for (RALResourceAssignment.Assignment<RALExpr> a : assignment.getAll()) {
            addParticipant(a.getActivity(), a.getExpr(), a.getDuty());
        }
    }

    private void addParticipant(String activityName, RALExpr expr, TaskDuty duty) {
        String isPotentialDuty = new DTClassicTaskDutyMapper().map(duty);
        String axiom = "(" + isPotentialDuty + " value " + idMapper.mapActivity(activityName) + ") EquivalentTo: (" + owlRalMapper.map(expr, 0) + ")";

        addAxiom(axiom);
    }

    public DTRALAnalyser createAnalyser() {
        DLQueryEngine engine = createDLQueryEngine();
        DTRALAnalyser analyser = new DTRALAnalyser(engine, idMapper);
        return analyser;
    }

}
