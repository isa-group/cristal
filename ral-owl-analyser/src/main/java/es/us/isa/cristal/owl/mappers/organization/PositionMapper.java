package es.us.isa.cristal.owl.mappers.organization;

import es.us.isa.cristal.Organization;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.mappers.ElementMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.*;

/**
* PositionMapper
* Copyright (C) 2014 Universidad de Sevilla
*
* @author resinas
*/
public class PositionMapper extends OrgElementMapper<Organization.Position> {
    private Map<String, List<String>> canBeDelegatedWorkBy = new HashMap<String, List<String>>();
    private Map<String, String> reportsTo = new HashMap<String, String>();

    public PositionMapper(OntologyHandler ontologyHandler, IdMapper mapper) {
        super(ontologyHandler, mapper);
    }

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
        mapParticipatesIn(i,instance);

        loadCanBeDelegatedWorkBy(i);
    }


    private void mapIsMemberOf(Organization.Position i, OWLNamedIndividual instance) {
        OWLNamedIndividual unit = factory.getOWLNamedIndividual(mapper.mapGroup(i.getMemberOf()), prefix);
        propertyAssertion(Definitions.ISMEMBEROF, instance, unit);
    }

    private void mapParticipatesIn(Organization.Position i, OWLNamedIndividual instance) {
        for (String roleName : i.getParticipatesIn()) {
            OWLNamedIndividual role = factory.getOWLNamedIndividual(mapper.mapGroup(roleName), prefix);
            propertyAssertion(Definitions.PARTICIPATESIN, instance, role);
        }

        String classExpression = Definitions.PARTICIPATESIN + " only {" + joinWith(i.getParticipatesIn()) + "}";
        addTypeAxiom(i, classExpression);
//        String classExpression = "(inverse(" + Definitions.PARTICIPATESIN + ") value " + mapper.mapGroup(i.getName()) + ")";
//        addEquivAxiom(classExpression, i.getParticipatesIn());
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
//            mapIsReportedBy(p);
//            mapCanDelegateWorkTo(p);
//            mapCanBeDelegatedWorkBy(p);
        }
    }

    private void mapIsReportedBy(Organization.Position i) {
//            if (i.getReportedBy() != null && ! i.getReportedBy().isEmpty()) {
        String query = "(" + Definitions.REPORTSTO + " value " + mapper.mapGroup(i.getName()) + ")";
        addEquivAxiom(query, i.getReportedBy());
//            } else {
//                String query = Definitions.
//            }
    }

    private void mapCanDelegateWorkTo(Organization.Position i) {
        String classExpression = "(inverse(" + Definitions.CANDELEGATEWORKTO + ") value " + mapper.mapGroup(i.getName()) + ")";
        addEquivAxiom(classExpression, i.getCanDelegateWorkTo());
    }

    private void mapCanBeDelegatedWorkBy(Organization.Position p) {
        String classExpression = "(" + Definitions.CANDELEGATEWORKTO + " value " + mapper.mapGroup(p.getName()) + ")";
        addEquivAxiom(classExpression, canBeDelegatedWorkBy.get(p.getName()));
    }
}
