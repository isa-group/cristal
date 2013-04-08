package es.us.isa.cristal.pba.rankers;

import es.isa.puri.Ranking;
import es.isa.puri.RankingMechanism;
import es.isa.puri.UnsupportedPreferenceTerm;
import es.isa.puri.model.DislikesPreference;
import es.isa.puri.model.FavoritesPreference;
import es.isa.puri.model.PreferenceTerm;
import es.isa.puri.ranking.Poset;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.parser.RALParser;
import es.us.isa.cristal.resolver.RALResolver;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.URIImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * User: resinas
 * Date: 21/03/13
 * Time: 19:16
 */
public class RALRanker implements RankingMechanism<Person> {

    public static final String MECHANISM = "http://www.isa.us.es/cristal/ranking#RALRanker";

    private RALResolver resolver;

    public RALRanker(RALResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public Ranking<Person> rank(Set<Person> persons, PreferenceTerm preferenceTerm) throws UnsupportedPreferenceTerm {
        preferenceTerm.getModel().open();
        Ranking<Person> ranking = new Poset<Person>();

        if (preferenceTerm.isInstanceof(FavoritesPreference.RDFS_CLASS)) {
            ranking = rankFavorites(persons, preferenceTerm);
        }
        else if (preferenceTerm.isInstanceof(DislikesPreference.RDFS_CLASS)) {
            ranking = rankDislikes(persons, preferenceTerm);
        }

        return ranking;
    }

    private Ranking<Person> rankDislikes(Set<Person> persons, PreferenceTerm preferenceTerm) {
        Ranking<Person> ranking = new Poset<Person>();
        DislikesPreference fav = DislikesPreference.getInstance(preferenceTerm.getModel(), preferenceTerm.asResource());
        String ralFav = fav.getAllPrefHasDislikes_asNode_().firstValue().asDatatypeLiteral().getValue();

        RALClassification classification = new RALClassification(ralFav, persons);
        List<Person> dislikes = classification.getContained();
        List<Person> other = classification.getNotContained();

        addRank(ranking, other, dislikes);

        return ranking;
    }

    private Ranking<Person> rankFavorites(Set<Person> persons, PreferenceTerm preferenceTerm) {
        Ranking<Person> ranking = new Poset<Person>();
        FavoritesPreference fav = FavoritesPreference.getInstance(preferenceTerm.getModel(), preferenceTerm.asResource());
        String ralFav = fav.getAllPrefHasFavorites_asNode_().firstValue().asDatatypeLiteral().getValue();

        RALClassification classification = new RALClassification(ralFav, persons);
        List<Person> favorites = classification.getContained();
        List<Person> other = classification.getNotContained();

        addRank(ranking, favorites, other);

        return ranking;
    }

    private void addRank(Ranking<Person> ranking, List<Person> favorites, List<Person> other) {
        for (Person personFavorite: favorites) {
            for (Person personOther: other) {
                ranking.addRank(personFavorite, personOther);
            }
        }
    }

    private Collection<String> resolveRal(String ral) {
        RALExpr ralExpr = RALParser.parse(ral);
        return resolver.resolve(ralExpr, 0L);
    }

    private class RALClassification {
        private List<Person> contained;
        private List<Person> notContained;

        public RALClassification (String ral, Set<Person> persons) {
            contained = new ArrayList<Person>();
            notContained = new ArrayList<Person>();

            Collection<String> people = resolveRal(ral);

            for (Person p : persons) {
                if (people.contains(p.getName()))
                    contained.add(p);
                else
                    notContained.add(p);
            }

        }

        public List<Person> getContained() {
            return contained;
        }

        public List<Person> getNotContained() {
            return notContained;
        }
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
