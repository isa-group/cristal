package es.us.isa.cristal.pba.rankers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import es.isa.puri.Ranking;
import es.isa.puri.RankingMechanism;
import es.isa.puri.UnsupportedPreferenceTerm;
import es.isa.puri.model.PreferenceTerm;
import org.junit.Assert;
import org.junit.Test;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * User: resinas
 * Date: 07/04/13
 * Time: 11:03
 */
public class WorklistRankerTest {

    private Person anthony = new Person("Anthony");
    private Person charles = new Person("Charles");
    private Person christine = new Person("Christine");
    private Person adele = new Person("Adele");

    @Test
    public void shouldRankByWorklist() throws UnsupportedPreferenceTerm {
        Model model = RDF2Go.getModelFactory().createModel();
        model.open();
        InputStream source = getClass().getResourceAsStream("worklist.rdf.n3");

        try {
            model.readFrom(source, Syntax.Turtle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PreferenceTerm pref = PreferenceTerm.getAllInstances_as(model).firstValue();

        RankingMechanism<Person> rankingMechanism = new WorklistRanker(new TaskEngineMock());
        Ranking<Person> ranking = rankingMechanism.rank(
                Sets.newHashSet(anthony, charles, christine, adele),
                pref);

        List<Person> rankedList = ranking.getResultsAsList();
        Assert.assertEquals(rankedList, Lists.newArrayList(adele, charles, christine, anthony));
    }

    private class TaskEngineMock implements TaskEngine {
        @Override
        public long countTasks(String user) {
            if ("Anthony".equals(user)) return 15;
            else if ("Charles".equals(user)) return 8;
            else if ("Christine".equals(user)) return 10;
            else if ("Adele".equals(user)) return 6;
            else return 0;
        }
    }

}
