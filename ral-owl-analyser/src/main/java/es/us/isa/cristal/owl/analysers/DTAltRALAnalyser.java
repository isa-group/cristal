package es.us.isa.cristal.owl.analysers;

import es.us.isa.cristal.owl.DLHelper;
import es.us.isa.cristal.owl.DLQueryEngine;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.mappers.ral.misc.ActivityMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.InstanceTaskDutyMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.TaskDutyMapper;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.util.Set;
import java.util.logging.Logger;

/**
 * DTAltRALAnalyser
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public abstract class DTAltRALAnalyser extends AbstractRALAnalyser {
    private static final Logger log = Logger.getLogger(DTAltRALAnalyser.class.getName());

    protected TaskDutyMapper taskDutyMapper;
    protected Set<String> organizationPeople = null;
    protected ActivityMapper activityMapper;
    protected DefaultPrefixManager prefixManager;

    public DTAltRALAnalyser(DLQueryEngine engine, IdMapper mapper, ActivityMapper activityMapper, DefaultPrefixManager prefixManager) {
        super(engine, mapper);
        taskDutyMapper = new InstanceTaskDutyMapper();
        this.activityMapper = activityMapper;
        this.prefixManager = prefixManager;

        //        engine.getReasoner().precomputeInferences(InferenceType.CLASS_ASSERTIONS, InferenceType.CLASS_HIERARCHY,
//                InferenceType.DIFFERENT_INDIVIDUALS, InferenceType.SAME_INDIVIDUAL, InferenceType.DISJOINT_CLASSES,
//                InferenceType.OBJECT_PROPERTY_ASSERTIONS);
//        Reasoner r = (Reasoner) engine.getReasoner();
//        r.classifyClasses();
//        r.classifyObjectProperties();
//        r.classifyDataProperties();

//        OWLReasoner reasoner = engine.getReasoner();
//        OWLOntology ontology = reasoner.getRootOntology();
//        OWLOntologyManager manager = ontology.getOWLOntologyManager();
//        OWLDataFactory factory = manager.getOWLDataFactory();


//        List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
////        gens.add(new InferredClassAssertionAxiomGenerator());
//        gens.add(new InferredSubClassAxiomGenerator());
////        gens.add(new InferredInverseObjectPropertiesAxiomGenerator());

//        InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, gens);
//        iog.fillOntology(manager, ontology);

//        try {
//            manager.saveOntology(ontology, System.out);
//        } catch (OWLOntologyStorageException e) {
//            e.printStackTrace();
//        }


//        precomputeOrganizationPeople(engine);


    }

    protected Set<String> getOrganizationPeople() {
        if (organizationPeople == null) {
            precomputeOrganizationPeople(engine);
        }
        return organizationPeople;
    }

    public void precompute() {
        precomputeOrganizationPeople(engine);
    }

    private void precomputeOrganizationPeople(DLQueryEngine engine) {
        try {
            if (organizationPeople == null) {
                organizationPeople = DLHelper.mapFromOwl(engine.getInstances(Definitions.ORGANIZATIONPEOPLE, false));
                log.info("org people: " + organizationPeople);
            }
        } catch (InconsistentOntologyException e) {
            log.warning("Inconsistent resource assignment");
        }
    }


    protected void testQuery(String query) {
        log.info(query);
        Set<OWLNamedIndividual> a = engine.getInstances(query, false);
        log.info(a.toString());

    }
}
