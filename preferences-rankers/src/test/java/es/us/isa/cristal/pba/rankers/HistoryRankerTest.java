package es.us.isa.cristal.pba.rankers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import es.isa.puri.Ranking;
import es.isa.puri.RankingMechanism;
import es.isa.puri.UnsupportedPreferenceTerm;
import es.isa.puri.model.PreferenceTerm;
import org.junit.Assert;
import org.junit.Test;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.Variable;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.ontoware.rdfreactor.schema.rdfs.Resource;
import org.openrdf.rdf2go.RepositoryModelFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * User: resinas
 * Date: 07/04/13
 * Time: 12:05
 */
public class HistoryRankerTest {

    private Person anthony = new Person("Anthony");
    private Person charles = new Person("Charles");
    private Person christine = new Person("Christine");
    private Person adele = new Person("Adele");


    @Test
    public void shouldOpenN3File() {
        // create the RDF2GO Model
        RDF2Go.register(new RepositoryModelFactory());
        Model model = RDF2Go.getModelFactory().createModel();
        model.open();
        InputStream source = getClass().getResourceAsStream("history.rdf.n3");

        if (source == null)
            throw new RuntimeException();
        try {
            model.readFrom(source, Syntax.Turtle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        List<? extends PreferenceTerm> prefs = PreferenceTerm.getAllInstances_as(model).asList();

        for (PreferenceTerm p : prefs) {
            System.out.println(p);
            System.out.println(p.getAllType_as().firstValue());

            Resource r = p.getAllPrefHasOperands_as().firstValue();

            ClosableIterator<Statement> found;
            found = r.getModel().findStatements(r, new URIImpl("http://www.isa.us.es/ontologies/AbstractBP.owl#belongsToProcess", true), Variable.ANY);

            while (found.hasNext()) {
                Statement next = found.next();
                System.out.println(next);
                System.out.println(next.getObject().asURI().asJavaURI().getFragment());
            }

            System.out.println(r.asURI().asJavaURI().getFragment());
            System.out.println(r.getAllType_as().asList());
        }
    }

    @Test
    public void shouldRankByHistory() throws UnsupportedPreferenceTerm {
        Model model = RDF2Go.getModelFactory().createModel();
        model.open();
        InputStream source = getClass().getResourceAsStream("history.rdf.n3");

        try {
            model.readFrom(source, Syntax.Turtle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PreferenceTerm pref = PreferenceTerm.getAllInstances_as(model).firstValue();

        RankingMechanism<Person> rankingMechanism = new HistoryRanker(new BPHistoryMock());
        Ranking<Person> ranking = rankingMechanism.rank(
                Sets.newHashSet(anthony, charles, christine, adele),
                pref);

        List<Person> rankedList = ranking.getResultsAsList();
        Assert.assertEquals(Lists.newArrayList(anthony, christine, charles, adele), rankedList);

    }

    private class BPHistoryMock implements BPHistory {
        @Override
        public long countActivityByPerson(String user, String activity, String processId) {
            Assert.assertEquals("CreateResolutionProposalDraft", activity);
            Assert.assertEquals("iaapProcess", processId);

            if ("Anthony".equals(user)) return 15;
            else if ("Charles".equals(user)) return 8;
            else if ("Christine".equals(user)) return 10;
            else if ("Adele".equals(user)) return 6;
            else Assert.assertTrue("User not valid", false);

            return 0;
        }
    }
}
