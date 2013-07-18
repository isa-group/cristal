package es.us.isa.cristal.owl.mappers.ral.misc;

import es.us.isa.cristal.owl.RALOntologyManager;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * User: resinas
 * Date: 14/07/13
 * Time: 10:56
 */
public class LogMapper {

    public String map(String pid) {
        return RALOntologyManager.LOG + pid;
    }

    public String toPrefix(OWLNamedIndividual individual) {
        return RALOntologyManager.LOG + individual.getIRI().getFragment();
    }
}
