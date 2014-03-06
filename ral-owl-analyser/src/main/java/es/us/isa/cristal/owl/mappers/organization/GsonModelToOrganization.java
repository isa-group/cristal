package es.us.isa.cristal.owl.mappers.organization;

import es.us.isa.cristal.Organization;
import es.us.isa.cristal.organization.model.gson.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GsonModelToOrganization
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class GsonModelToOrganization {

    public Organization map(Model model) {
        Organization org = new Organization()
                .persons(mapPersons(model))
                .roles(mapRoles(model))
                .positions(mapPositions(model))
                .units(mapUnits(model));

        return org;
    }

    private Organization.Person[] mapPersons(Model model) {
        List<Organization.Person> persons = new ArrayList<Organization.Person>();
        Map<String, List<String>> occupies = new HashMap<String, List<String>>();

        for (Unit u : model.getUnits()) {
            for (Position pos : u.getPositions()) {
                for (String personName : pos.getOccupiedBy()) {
                    List<String> positions = occupies.get(personName);
                    if (positions == null) {
                        positions = new ArrayList<String>();
                        occupies.put(personName, positions);
                    }
                    positions.add(pos.getName());
                }
            }
        }

        for (Person p : model.getPersons()) {
            List<String> positions = occupies.get(p.getName());
            if (positions != null) {
                persons.add(Organization.person(p.getName(), positions.toArray(new String[positions.size()])));
            } else {
                persons.add(Organization.person(p.getName()));
            }
        }

        return persons.toArray(new Organization.Person[persons.size()]);
    }

    private Organization.Position[] mapPositions(Model model) {
        List<Organization.Position> positions = new ArrayList<Organization.Position>();
        for (Unit u : model.getUnits()) {
            for (Position pos : u.getPositions()) {
                positions.add(Organization.pos(
                        pos.getName(),
                        u.getName(),
                        pos.getRoles(),
                        positionToString(pos.getReportedBy()),
                        positionToString(pos.getDelegates())
                ));
            }
        }

        return positions.toArray(new Organization.Position[positions.size()]);
    }

    private List<String> positionToString(List<Position> positions) {
        List<String> names = new ArrayList<String>();
        for (Position p : positions) {
            names.add(p.getName());
        }
        return names;
    }

    private String[] mapRoles(Model model) {
        List<String> roles = new ArrayList<String>();
        for (Role r : model.getRoles()) {
            roles.add(r.getName());
        }
        return roles.toArray(new String[roles.size()]);
    }

    private String[] mapUnits(Model model) {
        List<String> units = new ArrayList<String>();
        for (Unit u : model.getUnits()) {
            units.add(u.getName());
        }
        return units.toArray(new String[units.size()]);
    }


}
