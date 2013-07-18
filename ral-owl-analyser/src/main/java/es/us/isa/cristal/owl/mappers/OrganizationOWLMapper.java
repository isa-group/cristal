package es.us.isa.cristal.owl.mappers;

import es.us.isa.cristal.Organization;
import es.us.isa.cristal.owl.DLQueryParser;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 * User: resinas
 * Date: 16/07/13
 * Time: 19:21
 */
public class OrganizationOWLMapper {

    private Organization org;
    private OWLOntology ontology;
    private DLQueryParser parser;
    private IdMapper mapper;
    private PrefixManager prefix;
    private OWLOntologyManager manager;
    private OWLDataFactory factory;


    public OrganizationOWLMapper(Organization org, OWLOntology ontology, DLQueryParser parser, IdMapper mapper, PrefixManager prefix) {
        this.org = org;
        this.ontology = ontology;
        this.parser = parser;
        this.mapper = mapper;
        this.prefix = prefix;
        this.manager = ontology.getOWLOntologyManager();
        this.factory = manager.getOWLDataFactory();
    }

    public OWLOntology getOntology() {
        return ontology;
    }

    public void map() {
        new UnitMapper().map(org.getUnits());
        new RoleMapper().map(org.getRoles());
        new PositionMapper().map(org.getPositions());
        new PersonMapper().map(org.getPersons());
    }

    private void propertyAssertion(String property, OWLNamedIndividual source, OWLNamedIndividual target) {
        OWLObjectProperty prop = factory.getOWLObjectProperty(property, prefix);
        manager.addAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(prop, source, target));
    }

    private String joinWith(Iterable<String> elements) {
        StringBuilder result = new StringBuilder();

        Iterator<String> it = elements.iterator();

        if (it.hasNext()) {
            result.append(mapper.mapGroup(it.next()));
        }

        while (it.hasNext()) {
            result.append(",").append(mapper.mapGroup(it.next()));
        }

        return result.toString();
    }

    private void addEquivAxiom(String query, List<String> elements) {
        OWLAxiom axiom;
        if (elements != null && !elements.isEmpty()) {
            axiom = parser.parseAxiom(query + " EquivalentTo: {" + joinWith(elements) + "}");
        } else {
            OWLClassExpression expr = parser.parseClassExpression(query);
            OWLClass nothing = factory.getOWLNothing();
            axiom = factory.getOWLEquivalentClassesAxiom(expr, nothing);
        }
        manager.addAxiom(ontology, axiom);
    }


    private abstract class ElementMapper<T> {
        protected void map(List<T> instanceNames) {
            Set<OWLNamedIndividual> instances = new HashSet<OWLNamedIndividual>();
            OWLClass typeInstance = factory.getOWLClass(getType(), prefix);

            for (T i : instanceNames) {
                OWLNamedIndividual instance = factory.getOWLNamedIndividual(mapper.mapGroup(getName(i)), prefix);
                OWLAxiom classAssertion = factory.getOWLClassAssertionAxiom(typeInstance, instance);
                instances.add(instance);
                manager.addAxiom(ontology, classAssertion);

                additionalInstanceMap(i, instance);
            }

            OWLAxiom differentAxiom = factory.getOWLDifferentIndividualsAxiom(instances);
            manager.addAxiom(ontology, differentAxiom);
            additionalGlobalMap(instanceNames, instances);
        }

        protected abstract String getName(T i);
        protected abstract String getType();
        protected void additionalInstanceMap(T i, OWLNamedIndividual instance){}
        protected void additionalGlobalMap(List<T> instanceNames, Set<OWLNamedIndividual> instances){}
    }

    private class UnitMapper extends ElementMapper<String> {
        @Override
        protected String getName(String i) {
            return i;
        }

        @Override
        protected String getType() {
            return Definitions.UNIT;
        }
    }

    private class RoleMapper extends ElementMapper<String> {
        @Override
        protected String getName(String i) {
            return i;
        }

        @Override
        protected String getType() {
            return Definitions.ROLE;
        }
    }

    private class PositionMapper extends ElementMapper<Organization.Position> {
        private Map<String, List<String>> canBeDelegatedWorkBy = new HashMap<String, List<String>>();

        @Override
        protected String getName(Organization.Position i) {
            return i.getName();
        }

        @Override
        protected String getType() {
            return Definitions.POSITION;
        }

        @Override
        protected void additionalInstanceMap(Organization.Position i, OWLNamedIndividual instance) {
            super.additionalInstanceMap(i, instance);
            mapIsMemberOf(i, instance);
            mapParticipatesIn(i);

            loadCanBeDelegatedWorkBy(i);
        }


        private void mapParticipatesIn(Organization.Position i) {
            String classExpression = "(inverse(" + Definitions.PARTICIPATESIN + ") value " + mapper.mapGroup(i.getName()) + ")";
            addEquivAxiom(classExpression, i.getParticipatesIn());
        }

        private void mapIsReportedBy(Organization.Position i) {
            String query = "(" + Definitions.REPORTSTO + " value " + mapper.mapGroup(i.getName()) + ")";
            addEquivAxiom(query, i.getReportedBy());
        }

        private void mapCanDelegateWorkTo(Organization.Position i) {
            String classExpression = "(inverse(" + Definitions.CANDELEGATEWORKTO + ") value " + mapper.mapGroup(i.getName()) + ")";
            addEquivAxiom(classExpression, i.getCanDelegateWorkTo());
        }

        private void mapIsMemberOf(Organization.Position i, OWLNamedIndividual instance) {
            OWLNamedIndividual unit = factory.getOWLNamedIndividual(mapper.mapGroup(i.getMemberOf()), prefix);
            propertyAssertion(Definitions.ISMEMBEROF, instance, unit);
        }

        private void loadCanBeDelegatedWorkBy(Organization.Position i) {
            if (i.getCanDelegateWorkTo() != null) {
                for (String position : i.getCanDelegateWorkTo()) {
                    List<String> delegate = canBeDelegatedWorkBy.get(position);
                    if (delegate == null) {
                        delegate = new ArrayList<String>();
                        canBeDelegatedWorkBy.put(position, delegate);
                    }
                    delegate.add(i.getName());
                }
            }
        }

        @Override
        protected void additionalGlobalMap(List<Organization.Position> instanceNames, Set<OWLNamedIndividual> instances) {
            super.additionalGlobalMap(instanceNames, instances);
            for (Organization.Position p : instanceNames) {
                mapIsReportedBy(p);
                mapCanDelegateWorkTo(p);
                mapCanBeDelegatedWorkBy(p);
            }
        }

        private void mapCanBeDelegatedWorkBy(Organization.Position p) {
            String classExpression = "(" + Definitions.CANDELEGATEWORKTO + " value " + mapper.mapGroup(p.getName()) + ")";
            addEquivAxiom(classExpression, canBeDelegatedWorkBy.get(p.getName()));
        }
    }

    private class PersonMapper extends ElementMapper<Organization.Person> {
        @Override
        protected String getName(Organization.Person i) {
            return i.getName();
        }

        @Override
        protected String getType() {
            return Definitions.PERSON;
        }

        @Override
        protected void additionalInstanceMap(Organization.Person i, OWLNamedIndividual instance) {
            super.additionalInstanceMap(i, instance);
            String classExpression = "(inverse(" + Definitions.OCCUPIES + ") value " + mapper.mapGroup(i.getName()) + ")";
            addEquivAxiom(classExpression, i.getOccupies());
        }
    }

}
