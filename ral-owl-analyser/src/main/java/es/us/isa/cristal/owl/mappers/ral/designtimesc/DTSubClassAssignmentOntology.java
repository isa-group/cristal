package es.us.isa.cristal.owl.mappers.ral.designtimesc;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.ResourceAssignment;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.*;
import es.us.isa.cristal.owl.analysers.DTSubClassRALAnalyser;
import es.us.isa.cristal.owl.mappers.ral.designtime.DTOwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.InstanceTaskDutyMapper;
import es.us.isa.cristal.owl.ontologyhandlers.AssignmentOntology;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import java.util.logging.Logger;

/**
* User: resinas
* Date: 13/07/13
* Time: 11:01
*/
public class DTSubClassAssignmentOntology extends AssignmentOntology {

    private static final Logger log = Logger.getLogger(DTSubClassAssignmentOntology.class.getName());

    private OwlRalMapper owlRalMapper;
    private IdMapper idMapper;

    private OWLOntologyManager manager;
    private final OWLDataFactory factory;


    public DTSubClassAssignmentOntology(OntologyHandler ontologyHandler, IdMapper idMapper, BPEngine engine) {
        super(ontologyHandler);
        this.idMapper = idMapper;
        owlRalMapper = new DTSubClassOwlRalMapper(idMapper, engine);
        manager = ontology.getOWLOntologyManager();
        factory = manager.getOWLDataFactory();
    }

    @Override
    public void buildOntology(ResourceAssignment assignment) {
        for (ResourceAssignment.Assignment a : assignment.getAll()) {
            addParticipant(a.getActivity(), a.getExpr(), a.getDuty());
        }

    }



    private void addParticipant(String activityName, RALExpr expr, TaskDuty duty) {
        String hasDuty = new InstanceTaskDutyMapper().map(duty);
        DLQueryParser parser = createDLQueryParser();
        OWLAxiom ax = factory.getOWLEquivalentClassesAxiom(
                factory.getOWLClass(RALOntologyManager.DTASSIGNMENT + activityName, prefixManager),
                parser.parseClassExpression(Definitions.ISOFTYPE + " value " + idMapper.mapActivity(activityName)));
        manager.addAxiom(ontology, ax);
        log.info("Add axiom: " + ax);

        OWLSubClassOfAxiom owlSubClassOfAxiom = factory.getOWLSubClassOfAxiom(
                factory.getOWLClass(RALOntologyManager.DTASSIGNMENT + activityName, prefixManager),
                factory.getOWLClass(Definitions.ACTIVITYINSTANCE, prefixManager)
        );
        manager.addAxiom(ontology, owlSubClassOfAxiom);
        log.info("Add axiom: " + owlSubClassOfAxiom);

        String axiom = "(" + Definitions.ISOFTYPE +" value " + idMapper.mapActivity(activityName) + ") SubClassOf: (" + hasDuty + " some " + Definitions.ORGANIZATIONPEOPLE + " and " + hasDuty + " only " + Definitions.ORGANIZATIONPEOPLE + " and " + hasDuty + " min 1 " + Definitions.ORGANIZATIONPEOPLE + ")";
        addAxiom(axiom);

        String subClass = "( inverse(" + hasDuty + ") some (" + Definitions.ISOFTYPE + " value " + idMapper.mapActivity(activityName) + ")) SubClassOf: (" + owlRalMapper.map(expr, 0) + ")";
//        String subClass = "(" + Definitions.ISOFTYPE + " value " + idMapper.mapActivity(activityName) + ") SubClassOf: (" + hasDuty + " some (" + owlRalMapper.map(expr, 0) + "))";

//        String subClass= "(inverse(" + hasDuty+") some "

        addAxiom(subClass);
    }

    public RALAnalyser createAnalyser() {
        DLQueryEngine engine = createDLQueryEngine();
        RALAnalyser analyser = new DTSubClassRALAnalyser(engine, idMapper);
        return analyser;
    }

}
