package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.resolver.BPEngineMock;
import es.us.isa.cristal.resolver.ConstraintResolver;

import org.junit.Before;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.kernel.impl.util.StringLogger;
import org.neo4j.test.TargetDirectory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * User: resinas
 * Date: 26/02/13
 * Time: 21:09
 */
public abstract class AbstractBaseTest {

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

    protected <T> Set<T> getSetFromResult(ExecutionResult result) {
        Set<T> l = new HashSet<T>();
        for(Object p: IteratorUtil.asIterable(result.javaColumnAs("person.name"))) {
            l.add((T)p);
        }
        return l;
    }

    protected ExecutionResult doQuery(RALExpr expr) {
    	BPEngine mock = new BPEngineMock();
        Neo4jQueryBuilder builder = new Neo4jQueryBuilder(mock, new ConstraintResolver(mock));
        String query = builder.build(expr, 0L);
        System.out.println(query);
        return engine.execute(query);
    }
}
