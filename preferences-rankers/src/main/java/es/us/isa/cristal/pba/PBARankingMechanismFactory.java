package es.us.isa.cristal.pba;

import es.isa.puri.RankingMechanism;
import es.isa.puri.mechanism.AbstractRankingMechanismFactory;
import es.isa.puri.mechanism.RankingMechanismFactory;
import es.isa.puri.mechanism.impl.DefaultParetoImpl;
import es.isa.puri.mechanism.impl.DefaultPrioritizedImpl;
import es.us.isa.cristal.pba.rankers.Person;
import org.ontoware.rdf2go.model.node.URI;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: resinas
 * Date: 07/04/13
 * Time: 13:03
 */
public class PBARankingMechanismFactory extends AbstractRankingMechanismFactory<Person> {

    private Map<URI, RankingMechanismFactory<Person>> factoryMechanisms;

    public PBARankingMechanismFactory() {
        mechanisms = new HashMap<URI, Class<RankingMechanism<Person>>>();
        compositeMechanisms = new HashMap<URI, Class<RankingMechanism<Person>>>();
        factoryMechanisms = new HashMap<URI, RankingMechanismFactory<Person>>();
        initializeDefaultMechanisms();
    }

    @Override
    protected RankingMechanismFactory<Person> getFactory() {
        return this;
    }

    @Override
    public RankingMechanism<Person> create(URI uri) {
        if (factoryMechanisms.containsKey(uri)) {
            return factoryMechanisms.get(uri).create(uri);
        }

        return super.create(uri);
    }

    @Override
    protected void initializeDefaultMechanisms() {
        registerCompositeMechanism(new DefaultParetoImpl<Person>(this));
        registerCompositeMechanism(new DefaultPrioritizedImpl<Person>(this));

    }

    public void registerFactoryMechanism(Collection<URI> uris, RankingMechanismFactory<Person> mechanism) {
        for (URI uri : uris) {
            if (!factoryMechanisms.containsKey(uri)) {
                factoryMechanisms.put(uri, mechanism);
            }
        }
    }

}
