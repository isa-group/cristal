package es.us.isa.cristal.owl;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * User: resinas
 * Date: 18/07/13
 * Time: 11:15
 */
public class DLHelper {
    public static Set<String> mapFromOwl(Set<OWLNamedIndividual> individuals) {
        Set<String> result = new HashSet<String>();
        for (OWLNamedIndividual i : individuals) {
            result.add(i.getIRI().getFragment());
        }

        return result;
    }

    public static String joinWith(Iterable<String> elements, String join) {
        StringBuilder result = new StringBuilder();

        Iterator<String> it = elements.iterator();

        if (it.hasNext()) {
            result.append(it.next());
        }

        while (it.hasNext()) {
            result.append(join).append(it.next());
        }

        return result.toString();
    }

    public static Set<String> mapClassesFromOwl(Set<OWLClass> classes) {
        Set<String> result = new HashSet<String>();
        for (OWLClass c: classes) {
            if (! c.isOWLNothing())
                result.add(c.getIRI().getFragment());
        }

        return result;
    }
}
