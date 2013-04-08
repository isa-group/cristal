package es.us.isa.cristal.pba.rankers;

import es.isa.puri.RankableItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: resinas
 * Date: 21/03/13
 * Time: 19:15
 */
public class Person implements RankableItem {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public static Set<Person> fromNames(Set<String> names ) {
        Set<Person> persons = new HashSet<Person>();
        for (String name : names) {
            persons.add(new Person(name));
        }

        return persons;
    }

    public static List<String> toStrings(List<Person> persons) {
        List<String> names = new ArrayList<String>();
        for (Person person: persons) {
            names.add(person.getName());
        }

        return names;
    }

}
