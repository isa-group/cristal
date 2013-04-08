package es.us.isa.cristal.pba.rankers;

import es.isa.puri.RankingMechanism;
import es.isa.puri.model.PreferenceTerm;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.Variable;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.ontoware.rdfreactor.schema.rdfs.Resource;

/**
 * User: resinas
 * Date: 07/04/13
 * Time: 11:44
 */
public class HistoryRanker extends BPEngineQuantitativeRanker implements RankingMechanism<Person> {

    public static final String MECHANISM = "http://www.isa.us.es/cristal/ranking#HistoryRanker";
    private static final String BELONGSTOPROCESS = "http://www.isa.us.es/ontologies/AbstractBP.owl#belongsToProcess";

    private BPHistory history;

    public HistoryRanker(BPHistory history) {
        this.history = history;
    }

    protected long getValueForPerson(Person p, PreferenceTerm preferenceTerm) {
        Resource operand = preferenceTerm.getAllPrefHasOperands_as().firstValue();
        String activity = operand.asURI().asJavaURI().getFragment();
        ClosableIterator<Statement> found;
        found = operand.getModel().findStatements(operand, new URIImpl(BELONGSTOPROCESS, true), Variable.ANY);

        if (! found.hasNext()) {
            throw new RuntimeException("No process defined for activity " + activity);
        }

        Statement next = found.next();
        String process = next.getObject().asURI().asJavaURI().getFragment();

        return history.countActivityByPerson(p.getName(), activity, process);
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
