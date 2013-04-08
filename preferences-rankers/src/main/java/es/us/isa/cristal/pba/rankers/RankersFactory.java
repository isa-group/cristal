package es.us.isa.cristal.pba.rankers;

import com.google.common.collect.Lists;
import es.isa.puri.RankingMechanism;
import es.isa.puri.mechanism.RankingMechanismFactory;
import es.us.isa.cristal.resolver.RALResolver;
import org.neo4j.cypher.ExecutionEngine;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.URIImpl;

import java.util.Collection;

/**
 * User: resinas
 * Date: 07/04/13
 * Time: 16:27
 */
public class RankersFactory implements RankingMechanismFactory<Person> {
    private static URI URIS[] = {
            new URIImpl(RALRanker.MECHANISM),
            new URIImpl(ResourceInfoRanker.MECHANISM),
            new URIImpl(WorklistRanker.MECHANISM),
            new URIImpl(HistoryRanker.MECHANISM)};

    private BPHistory history;
    private TaskEngine taskEngine;
    private RALResolver ralResolver;
    private ExecutionEngine orgEngine;

    public RankersFactory(BPHistory history, TaskEngine taskEngine, RALResolver ralResolver, ExecutionEngine orgEngine) {
        this.history = history;
        this.taskEngine = taskEngine;
        this.ralResolver = ralResolver;
        this.orgEngine = orgEngine;
    }

    @Override
    public RankingMechanism<Person> create(URI uri) {
        RankingMechanism<Person> mechanism;
        String uriString = uri.toString();

        if (RALRanker.MECHANISM.equals(uriString))
            mechanism = new RALRanker(ralResolver);
        else if (ResourceInfoRanker.MECHANISM.equals(uriString))
            mechanism = new ResourceInfoRanker(orgEngine);
        else if (WorklistRanker.MECHANISM.equals(uriString))
            mechanism = new WorklistRanker(taskEngine);
        else if (HistoryRanker.MECHANISM.equals(uriString))
            mechanism = new HistoryRanker(history);
        else
            throw new RuntimeException("Not valid mechanism - "+uri);

        return mechanism;
    }

    public Collection<URI> getURIs() {
        return Lists.newArrayList(URIS);
    }
}
