package es.us.isa.cristal.owl.mappers;

import es.us.isa.cristal.owl.DLQueryParser;
import es.us.isa.cristal.owl.OntologyHandler;
import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
* ElementMapper provides a basic algorithm to map a list of elements of the same type in a given OWL ontology.
* Specifically, for each element it adds an assertion in the ontology stating that the element is of the given type and
* it also adds an axiom stating that all elements added to the ontology are different from each other. Finally, it
* provides a couple of hooks so that additional mappings can be added.
*
* Copyright (C) 2014 Universidad de Sevilla
*
* @author resinas
*/
public abstract class ElementMapper<T> {

    private static final Logger log = Logger.getLogger(ElementMapper.class.getName());

    protected OWLOntology ontology;
    protected DLQueryParser parser;
    protected PrefixManager prefix;
    protected OWLOntologyManager manager;
    protected OWLDataFactory factory;


    public ElementMapper(OntologyHandler ontologyHandler) {
        this.ontology = ontologyHandler.getOntology();
        this.prefix = ontologyHandler.getPrefixManager();
        this.parser = ontologyHandler.createDLQueryParser();
        this.manager = ontology.getOWLOntologyManager();
        this.factory = manager.getOWLDataFactory();
    }

    /**
     * Maps the elements provided to the ontology. In particular, for each element it adds an assertion in the ontology
     * stating that the element is of the given type and it also adds an axiom stating that all elements added to the
     * ontology are different from each other.
     * @param instanceNames
     */
    public void map(List<T> instanceNames) {
        Set<OWLNamedIndividual> instances = new HashSet<OWLNamedIndividual>();
        OWLClass typeInstance = factory.getOWLClass(getType(), prefix);

        for (T i : instanceNames) {
            OWLNamedIndividual instance = factory.getOWLNamedIndividual(addPrefix(getName(i)), prefix);
            OWLAxiom classAssertion = factory.getOWLClassAssertionAxiom(typeInstance, instance);
            instances.add(instance);
            manager.addAxiom(ontology, classAssertion);

            additionalInstanceMap(i, instance);
        }

        OWLAxiom differentAxiom = factory.getOWLDifferentIndividualsAxiom(instances);
        manager.addAxiom(ontology, differentAxiom);
        additionalGlobalMap(instanceNames, instances);
    }

    /**
     * Returns the name that the given element should have in the ontology
     * @param i
     * @return
     */
    protected abstract String getName(T i);

    /**
     * Adds the prefix to the given name so that it can be used in DLQueries.
     * @param name
     * @return
     */
    protected abstract String addPrefix(String name);

    /**
     * Returns the name of the type in the ontology of the elements that are being mapped
     * @return
     */
    protected abstract String getType();

    /**
     * Allows additional mappings for each element that is being mapped
     * @param i
     * @param instance
     */
    protected void additionalInstanceMap(T i, OWLNamedIndividual instance){}

    /**
     * Allows additional mappings after all the elements have been mapped
     * @param instanceNames
     * @param instances
     */
    protected void additionalGlobalMap(List<T> instanceNames, Set<OWLNamedIndividual> instances){}

    protected void propertyAssertion(String property, OWLNamedIndividual source, OWLNamedIndividual target) {
        OWLObjectProperty prop = factory.getOWLObjectProperty(property, prefix);
        manager.addAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(prop, source, target));
    }

    protected void addTypeAxiom(T individual, String query)    {
        OWLNamedIndividual instance = factory.getOWLNamedIndividual(addPrefix(getName(individual)), prefix);
        OWLClassExpression typeInstance = parser.parseClassExpression(query);
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(typeInstance, instance);
        log.info(classAssertion.toString());
        manager.addAxiom(ontology, classAssertion);
    }

    protected void addEquivAxiom(String query, List<String> elements) {
        OWLAxiom axiom;
        if (elements != null && !elements.isEmpty()) {
            axiom = parser.parseAxiom(query + " EquivalentTo: {" + joinWith(elements) + "}");
        } else {
            OWLClassExpression expr = parser.parseClassExpression(query);
            OWLClass nothing = factory.getOWLNothing();
            axiom = factory.getOWLSubClassOfAxiom(expr, nothing);
        }
        log.info(axiom.toString());
        manager.addAxiom(ontology, axiom);
    }

    protected String joinWith(Iterable<String> elements) {
        StringBuilder result = new StringBuilder();

        Iterator<String> it = elements.iterator();

        if (it.hasNext()) {
            result.append(addPrefix(it.next()));
        }

        while (it.hasNext()) {
            result.append(",").append(addPrefix(it.next()));
        }

        return result.toString();
    }

}
