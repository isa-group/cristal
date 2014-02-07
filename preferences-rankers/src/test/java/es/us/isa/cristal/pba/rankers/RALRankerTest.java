package es.us.isa.cristal.pba.rankers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.DatatypeLiteral;
import org.openrdf.rdf2go.RepositoryModelFactory;

import com.google.common.collect.Sets;

import es.isa.puri.Ranking;
import es.isa.puri.RankingMechanism;
import es.isa.puri.UnsupportedPreferenceTerm;
import es.isa.puri.model.FavoritesPreference;
import es.isa.puri.model.PreferenceTerm;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;
import es.us.isa.cristal.test.utils.bpEngine.MockBPEngine;
import es.us.isa.cristal.test.utils.executionengine.ExecutionEngineUtil;
import es.us.isa.cristal.test.utils.graph.GraphUtil;

/**
 * User: resinas
 * Date: 21/03/13
 * Time: 20:47
 */
public class RALRankerTest {

    

    protected ExecutionEngine engine;

    @Before
    public void setUpNeo4jDB() {
        // given
        engine = ExecutionEngineUtil.getExecutionEngine(GraphUtil.THEOS_GRAPH);
    }

    @Test
    public void shouldOpenN3File() {
        // create the RDF2GO Model
        RDF2Go.register(new RepositoryModelFactory());
        Model model = RDF2Go.getModelFactory().createModel();
        model.open();
        InputStream source = getClass().getResourceAsStream("ral.rdf.n3");

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
            if (p.isInstanceof(FavoritesPreference.RDFS_CLASS)) {
                System.out.println("instance of lowest");
                FavoritesPreference lp = FavoritesPreference.getInstance(model, p.asResource());
                DatatypeLiteral dl = lp.getAllPrefHasFavorites_asNode_().firstValue().asDatatypeLiteral();
                if (dl != null)
                    System.out.println(dl.getValue());

            }
            //System.out.println(p.getAllPrefHasOperands_asNode_().firstValue().asDatatypeLiteral().getValue());
//            try {
//                DatatypeLiteral literal = p.getAllPrefHasOperands_asNode_().firstValue().asDatatypeLiteral();
//
//                if (literal != null)
//                    System.out.println(literal.getValue());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    @Test
    public void shouldRankByRalExpr() throws UnsupportedPreferenceTerm {
        // create the RDF2GO Model
        Model model = RDF2Go.getModelFactory().createModel();
        model.open();
        InputStream source = getClass().getResourceAsStream("ral.rdf.n3");

        try {
            model.readFrom(source, Syntax.Turtle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PreferenceTerm pref = PreferenceTerm.getAllInstances_as(model).firstValue();

        RankingMechanism<Person> rankingMechanism = new RALRanker(new Neo4JRalResolver(new MockBPEngine(), engine));

        Ranking<Person> ranking = rankingMechanism.rank(
                Sets.newHashSet(new Person("Anthony"),
                        new Person("Charles"),
                        new Person("Christine"),
                        new Person("Adele"),
                        new Person("Betty")),
                pref);

        List<Person> rankedList = ranking.getResultsAsList();
        System.out.println(rankedList);

    }

}
