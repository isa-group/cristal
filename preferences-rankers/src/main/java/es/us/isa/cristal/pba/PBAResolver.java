package es.us.isa.cristal.pba;

import es.isa.puri.Ranking;
import es.isa.puri.RankingMechanism;
import es.isa.puri.UnsupportedPreferenceTerm;
import es.isa.puri.model.PreferenceTerm;
import es.us.isa.cristal.pba.rankers.BPHistory;
import es.us.isa.cristal.pba.rankers.Person;
import es.us.isa.cristal.pba.rankers.RankersFactory;
import es.us.isa.cristal.pba.rankers.TaskEngine;
import es.us.isa.cristal.resolver.RALResolver;
import org.neo4j.cypher.ExecutionEngine;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.URI;

import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/**
 * User: resinas
 * Date: 07/04/13
 * Time: 12:58
 */
public class PBAResolver {

    private PBARankingMechanismFactory factory;

    public PBAResolver(BPHistory history, TaskEngine taskEngine, RALResolver ralResolver, ExecutionEngine orgEngine) {
        RankersFactory rankersFactory = new RankersFactory(history, taskEngine, ralResolver, orgEngine);
        factory = new PBARankingMechanismFactory();
        factory.registerFactoryMechanism(rankersFactory.getURIs(), rankersFactory);
    }

    public Ranking<Person> resolve(Reader preferences, Set<Person> persons) {
        // create the RDF2GO Model
        Model model = RDF2Go.getModelFactory().createModel();
        model.open();
        try {
            model.readFrom(preferences, Syntax.Turtle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PreferenceTerm pref = PreferenceTerm.getAllInstances_as(model).firstValue();
        URI u = pref.getAllPrefHasRankingMechanism_as().firstValue().asURI();

        RankingMechanism<Person> rankingMechanism = factory.create(u);

        try {
            return rankingMechanism.rank(persons, pref);
        } catch (UnsupportedPreferenceTerm unsupportedPreferenceTerm) {
            throw new RuntimeException(unsupportedPreferenceTerm);
        }

    }


}
