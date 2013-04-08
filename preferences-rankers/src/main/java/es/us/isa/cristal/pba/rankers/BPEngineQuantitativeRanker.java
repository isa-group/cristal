package es.us.isa.cristal.pba.rankers;

import com.google.common.collect.Lists;
import es.isa.puri.Ranking;
import es.isa.puri.RankingMechanism;
import es.isa.puri.UnsupportedPreferenceTerm;
import es.isa.puri.model.LowestPreference;
import es.isa.puri.model.PreferenceTerm;
import es.isa.puri.ranking.Poset;

import java.util.*;

/**
 * User: resinas
 * Date: 07/04/13
 * Time: 11:43
 */
public abstract class BPEngineQuantitativeRanker implements RankingMechanism<Person> {
    public Ranking<Person> rank(Set<Person> persons, PreferenceTerm preferenceTerm) throws UnsupportedPreferenceTerm {
        Ranking<Person> ranking = new Poset<Person>();
        SortedMap<Long, List<Person>> countTasks = new TreeMap<Long, List<Person>>();

        for (Person p: persons) {
            long value = getValueForPerson(p, preferenceTerm);
            if (countTasks.containsKey(value)) {
                countTasks.get(value).add(p);
            } else {
                countTasks.put(value, Lists.newArrayList(p));
            }
        }

        List<Person> orderedList = new ArrayList<Person>();

        for (List<Person> lp : countTasks.values()) {
            orderedList.addAll(lp);
        }

        for (int i = 0; i < orderedList.size(); i++) {
            for (int j = i + 1; j < orderedList.size(); j++) {
                if (preferenceTerm.isInstanceof(LowestPreference.RDFS_CLASS)) {
                    ranking.addRank(orderedList.get(i), orderedList.get(j));
                } else {
                    ranking.addRank(orderedList.get(j), orderedList.get(i));
                }
            }
        }

        return ranking;
    }

    protected abstract long getValueForPerson(Person p, PreferenceTerm preferenceTerm);
}
