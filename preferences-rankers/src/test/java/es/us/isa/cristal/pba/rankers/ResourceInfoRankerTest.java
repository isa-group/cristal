package es.us.isa.cristal.pba.rankers;

import com.google.common.collect.Sets;
import es.isa.puri.Ranking;
import es.isa.puri.RankingMechanism;
import es.isa.puri.UnsupportedPreferenceTerm;
import es.isa.puri.model.PreferenceTerm;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.util.StringLogger;
import org.neo4j.test.TargetDirectory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;
import org.openrdf.rdf2go.RepositoryModelFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * User: resinas
 * Date: 06/04/13
 * Time: 12:21
 */
public class ResourceInfoRankerTest {
    private static final String GRAPH_DESCRIPTION = "CREATE anthony = { name : 'Anthony', degree: 'PhD', cost: 10 }\n" +
            "CREATE betty = { name : 'Betty', cost:5 }\n" +
            "CREATE daniel = { name : 'Daniel', cost:4 }\n" +
            "CREATE anna = { name : 'Anna' }\n" +
            "CREATE charles = { name : 'Charles', degree: 'PhD', cost: 8 }\n" +
            "CREATE christine = { name : 'Christine', degree: 'MsC', cost:6 }\n" +
            "CREATE adele = { name : 'Adele', cost:6 }\n";

    protected ExecutionEngine engine;

    @Before
    public void setUpNeo4jDB() {
        // given
        File storeDir = TargetDirectory.forTest(this.getClass()).directory( System.currentTimeMillis() + "-test" );

        GraphDatabaseService graphDb = new GraphDatabaseFactory().
                newEmbeddedDatabaseBuilder( storeDir.getAbsolutePath() ).
                setConfig( GraphDatabaseSettings.node_keys_indexable, "name" ).
                setConfig( GraphDatabaseSettings.node_auto_indexing, "true" ).
                newGraphDatabase();

        engine = new ExecutionEngine( graphDb,
                StringLogger.logger(new File(storeDir.getAbsolutePath() + "/logger")) );
        engine.execute( GRAPH_DESCRIPTION );
    }

    @Test
    public void shouldOpenN3File() {
        // create the RDF2GO Model
        RDF2Go.register(new RepositoryModelFactory());
        Model model = RDF2Go.getModelFactory().createModel();
        model.open();
        InputStream source = getClass().getResourceAsStream("resourceInfo.rdf.n3");

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

            System.out.println(p.getAllPrefRefersTo_asNode_().firstValue().asURI().asJavaURI().getFragment());
        }
    }

    @Test
    public void shouldRankByResourceInfo() throws UnsupportedPreferenceTerm {
        // create the RDF2GO Model
        Model model = RDF2Go.getModelFactory().createModel();
        model.open();
        InputStream source = getClass().getResourceAsStream("resourceInfo.rdf.n3");

        try {
            model.readFrom(source, Syntax.Turtle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PreferenceTerm pref = PreferenceTerm.getAllInstances_as(model).firstValue();

        RankingMechanism<Person> rankingMechanism = new ResourceInfoRanker(engine);

        Ranking<Person> ranking = rankingMechanism.rank(
                Sets.newHashSet(new Person("Anthony"),
                        new Person("Charles"),
                        new Person("Christine"),
                        new Person("Adele"),
                        new Person("Betty"),
                        new Person("Anna"),
                        new Person("Daniel")),
                pref);

        List<Person> rankedList = ranking.getResultsAsList();
        System.out.println(rankedList);

    }



}
