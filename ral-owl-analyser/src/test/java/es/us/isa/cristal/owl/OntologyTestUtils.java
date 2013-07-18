package es.us.isa.cristal.owl;

import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.util.HashSet;
import java.util.Set;

/**
 * User: resinas
 * Date: 16/07/13
 * Time: 09:25
 */
public class OntologyTestUtils {
    public static Set<String> toFragments(Set<OWLNamedIndividual> individuals) {
        Set<String> result = new HashSet<String>();
        for (OWLNamedIndividual i : individuals) {
            result.add(i.getIRI().getFragment());
        }

        return result;
    }

    public static void testQuery(DLQueryEngine engine, String query) {
        System.out.println(query);
        Set<OWLNamedIndividual> a = engine.getInstances(query, false);
        System.out.println(a);

    }

    public static void printOntology(OWLOntology ont) {
        OWLOntologyManager manager = ont.getOWLOntologyManager();
        try {
            manager.saveOntology(ont, new StreamDocumentTarget(System.out));
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }
    }

}
