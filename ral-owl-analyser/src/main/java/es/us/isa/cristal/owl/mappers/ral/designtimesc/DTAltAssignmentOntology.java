package es.us.isa.cristal.owl.mappers.ral.designtimesc;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.RALResourceAssignment;
import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.*;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.designtime.DTClassicOwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.ActivityMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.ActivityMapperSubClass;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.ontologyhandlers.AssignmentOntology;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;

import java.util.Set;
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
    protected Set<String> organizationPeople = null;


    private boolean precomputed = false;

    public DTAltAssignmentOntology(OntologyHandler ontologyHandler, IdMapper idMapper, BPEngine engine, ActivityMapper activityMapper, OwlRalMapper owlRalMapper, OwlRalMapper classicRalMapper) {
        super(ontologyHandler);

        manager = ontology.getOWLOntologyManager();
        factory = manager.getOWLDataFactory();
        this.idMapper = idMapper;
        parser = createDLQueryParser();
        this.activityMapper = activityMapper;
        this.classicRalMapper = classicRalMapper;
        this.owlRalMapper = owlRalMapper;
        this.engine = createDLQueryEngine();

    }

    @Override
    public void precompute(RALResourceAssignment assignment, boolean full) {
        super.precompute(assignment, full);
        precomputed = true;

        for (RALResourceAssignment.Assignment a : assignment.getAll()) {
            loadActivitySkeleton(a.getActivity(), a.getDuty(), Cardinality.EXACTLY);
        }

        loadOverall(assignment);

        if (full) {
            engine.getReasoner().flush();
//            this.engine.getReasoner().precomputeInferences(InferenceType.CLASS_HIERARCHY, InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_ASSERTIONS, InferenceType.DIFFERENT_INDIVIDUALS, InferenceType.SAME_INDIVIDUAL);
            organizationPeople = DLHelper.mapFromOwl(engine.getInstances(Definitions.ORGANIZATIONPEOPLE, false));
            log.info("org people: " + organizationPeople);
        }

    }

    @Override
    public void buildOntology(RALResourceAssignment assignment) {
        log.info(engine.getReasoner().getBufferingMode().toString());
        log.info(engine.getReasoner().getPrecomputableInferenceTypes().toString());


        if (!precomputed) {
            for (RALResourceAssignment.Assignment a : assignment.getAll()) {
                loadActivitySkeleton(a.getActivity(), a.getDuty(), Cardinality.EXACTLY);
            }

            loadOverall(assignment);
        }

        for (RALResourceAssignment.Assignment<RALExpr> a : assignment.getAll()) {
            addAssignment(a.getActivity(), a.getExpr(), a.getDuty());
        }


//        engine.getReasoner().flush();
    }

    protected void loadOverall(RALResourceAssignment assignment){}

    protected abstract void loadActivitySkeleton(String activityName, TaskDuty duty, Cardinality cardinality);

    protected abstract void addAssignment(String activityName, RALExpr expr, TaskDuty duty);

    public abstract RALAnalyser createAnalyser();

    public enum Cardinality {
        MIN, EXACTLY, MAX
    }


}
