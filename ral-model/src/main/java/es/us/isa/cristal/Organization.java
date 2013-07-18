package es.us.isa.cristal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: resinas
 * Date: 16/07/13
 * Time: 12:25
 */
public class Organization {
    private List<String> units;
    private List<String> roles;
    private List<Position> positions;
    private List<Person> persons;

    public static class Position {
        private String name;
        private List<String> participatesIn;
        private String isMemberOf;
        private List<String> isReportedBy;
        private List<String> canDelegateWorkTo;

        public Position(String name, List<String> participatesIn, String memberOf, List<String> reportedBy, List<String> canDelegateWorkTo) {
            this.name = name;
            this.participatesIn = participatesIn;
            isMemberOf = memberOf;
            isReportedBy = reportedBy;
            this.canDelegateWorkTo = canDelegateWorkTo;
        }

        public String getName() {
            return name;
        }

        public List<String> getParticipatesIn() {
            return participatesIn;
        }

        public String getMemberOf() {
            return isMemberOf;
        }

        public List<String> getReportedBy() {
            return isReportedBy;
        }

        public List<String> getCanDelegateWorkTo() {
            return canDelegateWorkTo;
        }
    }

    public static class Person {
        private String name;
        private List<String> occupies;

        private Person(String name, List<String> occupies) {
            this.name = name;
            this.occupies = occupies;
        }

        public String getName() {
            return name;
        }

        public List<String> getOccupies() {
            return occupies;
        }
    }

    public Organization() {
        units = new ArrayList<String>();
        roles = new ArrayList<String>();
        positions = new ArrayList<Position>();
        persons = new ArrayList<Person>();
    }

    public Organization units(String ... u) {
        for (String unit : u) {
            units.add(unit);
        }
        return this;
    }

    public Organization roles(String ... r) {
        for (String role: r) {
            roles.add(role);
        }
        return this;
    }

    public Organization positions(Position ... p) {
        for (Position pos : p) {
            positions.add(pos);
        }
        return this;
    }

    public static Position pos(String name, String memberOf, List<String> participatesIn, List<String> isReportedBy, List<String> canDelegateWorkTo) {
        return new Position(name, participatesIn, memberOf, isReportedBy, canDelegateWorkTo);
    }

    public Organization persons(Person ... p) {
        for (Person per : p) {
            persons.add(per);
        }
        return this;
    }

    public static Person person(String name, String ... occupies) {
        return new Person(name, Arrays.asList(occupies));
    }

    public List<String> getUnits() {
        return units;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<Position> getPositions() {
        return positions;
    }


    public List<Person> getPersons() {
        return persons;
    }
}
