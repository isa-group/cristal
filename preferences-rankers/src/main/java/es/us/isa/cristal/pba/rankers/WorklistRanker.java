package es.us.isa.cristal.pba.rankers;

import es.isa.puri.RankingMechanism;
import es.isa.puri.model.PreferenceTerm;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.URIImpl;

/**
 * User: resinas
 * Date: 06/04/13
 * Time: 18:25
 */
public class WorklistRanker extends BPEngineQuantitativeRanker implements RankingMechanism<Person> {

    public static final String MECHANISM = "http://www.isa.us.es/cristal/ranking#WorklistRanker";

    private TaskEngine taskEngine;

    public WorklistRanker(TaskEngine taskEngine) {
        this.taskEngine = taskEngine;
    }

    protected long getValueForPerson(Person p, PreferenceTerm preferenceTerm) {
        return taskEngine.countTasks(p.getName());
    }

    @Override
    public boolean supportsPreferenceTerm(PreferenceTerm preferenceTerm) {
        return false;
    }

    @Override
    public URI getMechanismURI() {
        return new URIImpl(MECHANISM, false);
    }
}
