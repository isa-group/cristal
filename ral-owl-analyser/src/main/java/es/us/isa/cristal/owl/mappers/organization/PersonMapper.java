package es.us.isa.cristal.owl.mappers.organization;

import es.us.isa.cristal.Organization;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.mappers.ElementMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
* PersonMapper
* Copyright (C) 2014 Universidad de Sevilla
*
* @author resinas
*/
public class PersonMapper extends OrgElementMapper<Organization.Person> {
    private static final Logger log = Logger.getLogger(PersonMapper.class.getName());

    private Set<String> positions;

    public PersonMapper(OntologyHandler ontologyHandler, IdMapper mapper, List<Organization.Position> positions) {
        super(ontologyHandler, mapper);
        this.positions = new HashSet<String>();
        for (Organization.Position pos: positions) {
            this.positions.add(pos.getName());
        }
    }

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

        factory.getOWLEquivalentClassesAxiom(factory.getOWLClass(Definitions.ORGANIZATION + "class" + i.getName(), prefix),
                factory.getOWLObjectOneOf(instance));

        Set<String> pos = new HashSet<String>(positions);
//        String classExpression = "(inverse(" + Definitions.OCCUPIES + ") value " + mapper.mapGroup(i.getName()) + ")";
//        addEquivAxiom(classExpression, i.getOccupies());

        for (String positionName : i.getOccupies()) {
            OWLNamedIndividual position = factory.getOWLNamedIndividual(mapper.mapGroup(positionName), prefix);
            propertyAssertion(Definitions.OCCUPIES, instance, position);
            pos.remove(positionName);
        }

        for (String positionName: pos) {
            OWLNamedIndividual position = factory.getOWLNamedIndividual(mapper.mapGroup(positionName), prefix);
            negativePropertyAssertion(Definitions.OCCUPIES, instance, position);
        }

        String classExpression = Definitions.OCCUPIES + " only {" + joinWith(i.getOccupies()) + "}";
        addTypeAxiom(i, classExpression);

    }

    @Override
    protected void additionalGlobalMap(List<Organization.Person> instanceNames, Set<OWLNamedIndividual> instances) {
        super.additionalGlobalMap(instanceNames, instances);

        OWLAxiom axiom = factory.getOWLEquivalentClassesAxiom(
                factory.getOWLClass(Definitions.ORGANIZATIONPEOPLE, prefix),
                factory.getOWLObjectOneOf(instances));
        manager.addAxiom(ontology, axiom);

        log.info("Adding organization people axiom: " + axiom);
    }
}
