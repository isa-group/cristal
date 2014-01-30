package es.us.isa.cristal.pba.rankers;

import com.google.common.collect.Sets;

import es.isa.puri.Ranking;
import es.isa.puri.RankingMechanism;
import es.isa.puri.UnsupportedPreferenceTerm;
import es.isa.puri.model.FavoritesPreference;
import es.isa.puri.model.PreferenceTerm;
import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;
import es.us.isa.cristal.parser.RALParser;

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
import org.ontoware.rdf2go.model.node.DatatypeLiteral;
import org.openrdf.rdf2go.RepositoryModelFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * User: resinas
 * Date: 21/03/13
 * Time: 20:47
 */
public class RALRankerTest {

    private static final String GRAPH_DESCRIPTION = "CREATE anthony = { name : 'Anthony', degree: 'PhD' }\n" +
            "CREATE betty = { name : 'Betty' }\n" +
            "CREATE daniel = { name : 'Daniel' }\n" +
            "CREATE anna = { name : 'Anna' }\n" +
            "CREATE charles = { name : 'Charles', degree: 'PhD' }\n" +
            "CREATE christine = { name : 'Christine', degree: 'MsC' }\n" +
            "CREATE adele = { name : 'Adele' }\n" +
            "\n" +
            "CREATE theos = { unit : 'Theos' }\n" +
            "\n" +
            "CREATE theos_coordinator = {position : 'THEOS Project Coordinator' }\n" +
            "CREATE theos_account = {position : 'THEOS Account Delegate'}\n" +
            "CREATE theos_technician = { position : 'THEOS Technician'}\n" +
            "CREATE theos_assistant = {position : 'THEOS Administrative Assistant'}\n" +
            "CREATE theos_responsible = {position : 'THEOS Responsible for Work Package'}\n" +
            "CREATE theos_student = {position : 'THEOS PhD Student'}\n" +
            "\n" +
            "CREATE coordinator = {role : 'Project Coordinator'}\n" +
            "CREATE administrator = {role: 'Account Administrator'}\n" +
            "CREATE resourcemanager = {role: 'Resource Manager'}\n" +
            "CREATE responsible = {role: 'Responsible for Work Package'}\n"+
            "CREATE researcher = {role: 'Researcher'}\n"+
            "CREATE student = {role: 'PhD Student'}\n" +
            "CREATE clerk = {role: 'Clerk'}\n" +
            "CREATE technician = {role: 'Technician'}\n" +
            "\n" +
            "CREATE anthony-[:POSITION]->theos_coordinator \n" +
            "CREATE anthony-[:POSITION]->theos_responsible \n" +
            "CREATE betty-[:POSITION]->theos_account \n" +
            "CREATE daniel-[:POSITION]->theos_technician \n" +
            "CREATE anna-[:POSITION]->theos_assistant \n"+
            "CREATE christine-[:POSITION]->theos_student \n"+
            "CREATE adele-[:POSITION]->theos_student \n" +
            "CREATE charles-[:POSITION]->theos_responsible \n" +
            "\n" +
            "CREATE theos_coordinator-[:UNIT]->theos \n"+
            "CREATE theos_account-[:UNIT]->theos \n"+
            "CREATE theos_technician-[:UNIT]->theos \n"+
            "CREATE theos_assistant-[:UNIT]->theos \n"+
            "CREATE theos_responsible-[:UNIT]->theos \n"+
            "CREATE theos_student-[:UNIT]->theos \n"+
            "\n" +
            "CREATE theos_coordinator-[:ROLE]->coordinator \n"+
            "CREATE theos_coordinator-[:ROLE]->administrator \n"+
            "CREATE theos_coordinator-[:ROLE]->resourcemanager \n"+
            "CREATE theos_responsible-[:ROLE]->responsible \n"+
            "CREATE theos_responsible-[:ROLE]->researcher \n"+
            "CREATE theos_student-[:ROLE]->researcher \n"+
            "CREATE theos_student-[:ROLE]->student \n"+
            "CREATE theos_account-[:ROLE]->administrator \n"+
            "CREATE theos_technician-[:ROLE]->technician \n"+
            "CREATE theos_assistant-[:ROLE]->clerk \n"+
            "\n" +
            "CREATE theos_coordinator-[:DELEGATES]->theos_account \n"+
            "CREATE theos_coordinator-[:DELEGATES]->theos_technician \n"+
            "CREATE theos_coordinator-[:DELEGATES]->theos_assistant \n" +
            "CREATE theos_coordinator-[:DELEGATES]->theos_responsible \n" +
            "CREATE theos_responsible-[:DELEGATES]->theos_student \n " +
            "CREATE theos_coordinator<-[:REPORTS]-theos_account \n"+
            "CREATE theos_coordinator<-[:REPORTS]-theos_technician \n"+
            "CREATE theos_coordinator<-[:REPORTS]-theos_assistant \n" +
            "CREATE theos_coordinator<-[:REPORTS]-theos_responsible \n" +
            "CREATE theos_responsible<-[:REPORTS]-theos_student";

    protected ExecutionEngine engine;

    @Before
    public void setUpNeo4jDB() {
        // given
        File storeDir = TargetDirectory.forTest(this.getClass()).directory( System.currentTimeMillis() + "-test" );

        GraphDatabaseService graphDb = new GraphDatabaseFactory().
                newEmbeddedDatabaseBuilder( storeDir.getAbsolutePath() ).
                setConfig( GraphDatabaseSettings.node_keys_indexable, "name, position, role, unit" ).
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

        RankingMechanism<Person> rankingMechanism = new RALRanker(new Neo4JRalResolver(new BPEngineMock(), engine));

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

    public class BPEngineMock implements BPEngine {
        @Override
        public String getDataValue(Object pid, String data, String property) {
            return "Anna";
        }

        @Override
        public List<String> getActivityPerformer(Object pid, String activityName) {
            return Arrays.asList("Charles");
        }

        @Override
        public RALExpr getResourceExpression(Object processDefinitionID, String activityName) {
            return RALParser.parse("HAS ROLE Account Administrator");
        }

		@Override
		public RALExpr getResourceExpressionByProcessDefinitionId(
				Object processDefinitionId, String activityId) {
			return RALParser.parse("HAS ROLE Account Administrator");
		}

		@Override
		public RALExpr getResourceExpressionByProcessInstanceId(
				Object processInstanceId, String activityId) {
			return RALParser.parse("HAS ROLE Account Administrator");
		}
    }
}
