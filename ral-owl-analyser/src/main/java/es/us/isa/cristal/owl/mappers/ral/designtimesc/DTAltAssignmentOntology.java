package es.us.isa.cristal.owl.mappers.ral.designtimesc;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.RALResourceAssignment;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.DLQueryEngine;
import es.us.isa.cristal.owl.DLQueryParser;
import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.designtime.DTClassicOwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.ActivityMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.ActivityMapperSubClass;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.ontologyhandlers.AssignmentOntology;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;

import java.util.logging.Logger;

/**
 * DTAltAssignmentOntology
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public abstract class DTAltAssignmentOntology extends AssignmentOntology {
    private static final Logger log = Logger.getLogger(DTAltAssignmentOntology.class.getName());

    protected final OwlRalMapper owlRalMapper;
    protected final OwlRalMapper classicRalMapper;
    protected final IdMapper idMapper;
    protected final OWLOntologyManager manager;
    protected final OWLDataFactory factory;
    protected  ActivityMapper activityMapper;
    protected final DLQueryParser parser;
    protected final DLQueryEngine engine;

    public DTAltAssignmentOntology(OntologyHandler ontologyHandler, IdMapper idMapper, BPEngine engine) {
        super(ontologyHandler);

        manager = ontology.getOWLOntologyManager();
        factory = manager.getOWLDataFactory();
        this.idMapper = idMapper;
        parser = createDLQueryParser();
        activityMapper = new ActivityMapperSubClass();
        classicRalMapper = new DTClassicOwlRalMapper(idMapper, engine, activityMapper);
        owlRalMapper = new DTSubClassOwlRalMapper(idMapper, engine, activityMapper);
        this.engine = createDLQueryEngine();

    }

    @Override
    public void buildOntology(RALResourceAssignment assignment) {
        log.info(engine.getReasoner().getBufferingMode().toString());
        log.info(engine.getReasoner().getPrecomputableInferenceTypes().toString());

        engine.getReasoner().precomputeInferences(InferenceType.CLASS_HIERARCHY, InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_ASSERTIONS);

        for (RALResourceAssignment.Assignment a : assignment.getAll()) {
            loadActivitySkeleton(a.getActivity(), a.getDuty(), Cardinality.EXACTLY);
        }
//        engine.getReasoner().flush();

        for (RALResourceAssignment.Assignment<RALExpr> a : assignment.getAll()) {
            addAssignment(a.getActivity(), a.getExpr(), a.getDuty());
        }

        loadOverall(assignment);

//        engine.getReasoner().flush();

        engine.getReasoner().flush();
    }

    protected void loadOverall(RALResourceAssignment assignment){}

    protected abstract void loadActivitySkeleton(String activityName, TaskDuty duty, Cardinality cardinality);

    protected abstract void addAssignment(String activityName, RALExpr expr, TaskDuty duty);

    public abstract RALAnalyser createAnalyser();

    public enum Cardinality {
        MIN, EXACTLY, MAX
    }


}
