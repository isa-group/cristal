package es.us.isa.cristal.owl.mappers.organization;

import es.us.isa.cristal.Organization;
import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.util.logging.Logger;

/**
 * User: resinas
 * Date: 16/07/13
 * Time: 19:21
 */
public class OrganizationOWLMapper {

    private OntologyHandler ontologyHandler;
    private IdMapper mapper;


    public OrganizationOWLMapper(OntologyHandler ontologyHandler, IdMapper mapper) {
        this.ontologyHandler = ontologyHandler;
        this.mapper = mapper;
    }

    public void map(Organization org) {

        new UnitMapper(ontologyHandler, mapper).map(org.getUnits());
        OWLReasoner reasoner = ontologyHandler.createReasoner();

        OWLOntology ont = reasoner.getRootOntology();
        System.out.println("unit ontology consistent: " + reasoner.isConsistent());

        new RoleMapper(ontologyHandler, mapper).map(org.getRoles());
        reasoner = ontologyHandler.createReasoner();
        System.out.println("role ontology consistent: " + reasoner.isConsistent());

        new PositionMapper(ontologyHandler, mapper).map(org.getPositions());
        reasoner = ontologyHandler.createReasoner();
        System.out.println("position ontology consistent: " + reasoner.isConsistent());

        new PersonMapper(ontologyHandler, mapper).map(org.getPersons());
        reasoner = ontologyHandler.createReasoner();
        System.out.println("person ontology consistent: " + reasoner.isConsistent());

    }


}
