package es.us.isa.cristal.pba.rankers;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import es.isa.puri.Ranking;
import es.isa.puri.RankingMechanism;
import es.isa.puri.UnsupportedPreferenceTerm;
import es.isa.puri.model.HighestPreference;
import es.isa.puri.model.PreferenceTerm;
import es.isa.puri.ranking.Poset;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.helpers.collection.IteratorUtil;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.URIImpl;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: resinas
 * Date: 05/04/13
 * Time: 09:54
 */
public class ResourceInfoRanker implements RankingMechanism<Person> {

    public static final String MECHANISM = "http://www.isa.us.es/cristal/ranking#ResourceInfoRanker";

    private ExecutionEngine neo4jEngine;

    public ResourceInfoRanker(ExecutionEngine executionEngine) {
        neo4jEngine = executionEngine;
    }

    @Override
    public Ranking<Person> rank(Set<Person> persons, PreferenceTerm preferenceTerm) throws UnsupportedPreferenceTerm {
        Ranking<Person> ranking = new Poset<Person>();

        String propertyName = preferenceTerm.getAllPrefRefersTo_asNode_().firstValue().asURI().asJavaURI().getFragment();
        String query = buildQuery(persons, propertyName, preferenceTerm);
        ExecutionResult result = neo4jEngine.execute(query);
        List<String> orderedList = getArrayFromResult(result, "person.name");
        Map<String, Person> personByName = Maps.uniqueIndex(persons, new Function<Person, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Person person) {
                return person.getName();
            }
        });


        for (int i = 0; i < orderedList.size(); i++) {
            for (int j = i + 1; j < orderedList.size(); j++) {
                ranking.addRank(personByName.get(orderedList.get(i)), personByName.get(orderedList.get(j)));
            }
        }


        return ranking;
    }



    private <T> List<T> getArrayFromResult(ExecutionResult result, String columnName) {
        List<T> l = new ArrayList<T>();
        for(Object p: IteratorUtil.asIterable(result.javaColumnAs(columnName))) {
            l.add((T)p);
        }
        return l;
    }


    private String buildQuery(Set<Person> persons, String propertyName, PreferenceTerm term) {
        StringBuilder sb = new StringBuilder("START person=node:node_auto_index('name:*') WHERE ");

        sb.append(" has(person.").append(propertyName).append(") AND ");
        sb.append(" person.name IN ['");
        Joiner.on("', '").appendTo(sb, persons);
        sb.append("'] ");
//        Iterator<Person> it = persons.iterator();
//        while (it.hasNext()) {
//            Person p = it.next();
//            sb.append(" person.name!='").append(p.getName()).append("' ");
//            if (it.hasNext()) {
//                sb.append(" OR ");
//            }
//        }
//        sb.append(") ");

        sb.append(" RETURN person.name ORDER BY person.").append(propertyName);

        if (term.isInstanceof(HighestPreference.RDFS_CLASS)) {
            sb.append(" DESC ");
        }

        return sb.toString();
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
