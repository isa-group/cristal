package es.us.isa.cristal.performance.tester;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import es.us.isa.cristal.RawResourceAssignment;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.organization.model.gson.OrganizationalModel;
import es.us.isa.cristal.owl.ReasonerFactory;
import es.us.isa.cristal.owl.analysers.DTSubClassRALAnalyser;
import es.us.isa.cristal.performance.tester.data.ExperimentConfiguration;
import es.us.isa.cristal.performance.tester.data.OperationRun;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.kernel.impl.util.StringLogger;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.neo4j.queries.Neo4jQueryBuilder;
import es.us.isa.cristal.organization.generator.ConfigurationFactory;
import es.us.isa.cristal.organization.generator.OrganizationGenerator;
import es.us.isa.cristal.organization.model.util.IOUtil;
import es.us.isa.cristal.parser.RALParser;
import es.us.isa.cristal.performance.tester.data.Query;
import es.us.isa.cristal.performance.tester.exporters.ExporterFactory;
import es.us.isa.cristal.performance.tester.meters.Meter;
import es.us.isa.cristal.performance.tester.meters.TimeMeter;
import es.us.isa.cristal.performance.tester.util.PerformanceTesterBPEngine;
import es.us.isa.cristal.performance.tester.util.QueryProcessor;
import es.us.isa.cristal.resolver.ConstraintResolver;


/**
 * 
 * @author Manuel Leon
 *
 */
public class App extends AbstractRALPerformanceTest {

    static final String[] simpleShort = {
            "activity1", "HAS ROLE #RandomRole#",
            "activity2", "HAS POSITION #RandomPosition#",
            "activity3", "HAS UNIT #RandomUnit#",
            "activity4", "HAS ROLE #RandomRole#",
            "activity5", "HAS POSITION #RandomPosition#"
    };

    static final String[] simpleLong = {
            "activity1", "HAS ROLE #RandomRole#",
            "activity2", "HAS POSITION #RandomPosition#",
            "activity3", "HAS UNIT #RandomUnit#",
            "activity4", "HAS ROLE #RandomRole#",
            "activity5", "HAS ROLE #RandomRole#",
            "activity6", "HAS POSITION #RandomPosition#",
            "activity7", "HAS UNIT #RandomUnit#",
            "activity8", "HAS ROLE #RandomRole#",
            "activity9", "HAS ROLE #RandomRole#",
            "activity10", "HAS POSITION #RandomPosition#",
            "activity11", "HAS UNIT #RandomUnit#",
            "activity12", "HAS ROLE #RandomRole#",
            "activity13", "HAS ROLE #RandomRole#",
            "activity14", "HAS POSITION #RandomPosition#",
            "activity15", "HAS UNIT #RandomUnit#",
            "activity16", "HAS ROLE #RandomRole#",
            "activity17", "HAS ROLE #RandomRole#",
            "activity18", "HAS POSITION #RandomPosition#",
            "activity19", "HAS UNIT #RandomUnit#",
            "activity20", "HAS ROLE #RandomRole#"};

    static final String[] shortComposite = {
            "activity2", "HAS POSITION #RandomPosition#",
            "activity1", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#",
            "activity4", "HAS POSITION #RandomPosition# AND HAS UNIT #RandomUnit#",
            "activity5", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#",
            "activity7", "HAS POSITION #RandomPosition# AND HAS UNIT #RandomUnit#"};

    static final String[] longComposite = {
            "activity2","HAS POSITION #RandomPosition#" ,
            "activity1","HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity4","HAS POSITION #RandomPosition# AND HAS UNIT #RandomUnit#" ,
            "activity5","HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity7","HAS POSITION #RandomPosition# AND HAS UNIT #RandomUnit#" ,
            "activity8","HAS POSITION #RandomPosition#" ,
            "activity9","HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity11", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity21", "HAS POSITION #RandomPosition# OR HAS ROLE #RandomRole#" ,
            "activity24", "HAS POSITION #RandomPosition# AND HAS UNIT #RandomUnit#" ,
            "activity25", "HAS ROLE #RandomRole# AND HAS ROLE #RandomRole#" ,
            "activity29", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity211", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity310", "NOT HAS POSITION #RandomPosition#" ,
            "activity311", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity322", "HAS POSITION #RandomPosition#" ,
            "activity324", "HAS ROLE #RandomRole# AND HAS UNIT #RandomUnit#" ,
            "activity321", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity324", "HAS ROLE #RandomRole# AND HAS UNIT #RandomUnit#" ,
            "activity325", "NOT HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#"};

    static final String[] shortAC = {
            "activity2", "HAS POSITION #RandomPosition#" ,
            "activity1", "HAS ROLE #RandomRole#" ,
            "activity6", "IS PERSON WHO DID ACTIVITY activity1" ,
            "activity10", "NOT IS PERSON WHO DID ACTIVITY activity6" ,
            "activity5", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#"};

    static final String[] longAC = {
            "activity2", "HAS POSITION #RandomPosition#" ,
            "activity1", "HAS ROLE #RandomRole#" ,
            "activity6", "IS PERSON WHO DID ACTIVITY activity1" ,
            "activity10", "NOT IS PERSON WHO DID ACTIVITY activity6" ,
            "activity5", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#",
            "activity28", "IS PERSON WHO DID ACTIVITY activity10 AND HAS POSITION #RandomPosition#" ,
            "activity4", "HAS POSITION #RandomPosition# AND HAS UNIT #RandomUnit#" ,
            "activity7", "HAS POSITION #RandomPosition# AND HAS UNIT #RandomUnit#" ,
            "activity8", "HAS POSITION #RandomPosition#" ,
            "activity9", "IS PERSON WHO DID ACTIVITY activity1" ,
            "activity3", "HAS ROLE #RandomRole# AND HAS UNIT #RandomUnit#",
            "activity25", "IS PERSON WHO DID ACTIVITY activity29" ,
            "activity29", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity211", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity310", "NOT HAS POSITION #RandomPosition#" ,
            "activity311", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity322", "HAS POSITION #RandomPosition#" ,
            "activity324", "IS PERSON WHO DID ACTIVITY activity2" ,
            "activity321", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity324", "NOT IS PERSON WHO DID ACTIVITY activity5" };

    public static void main( String[] args ) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        System.out.println("Loading...");
        String opType="consistency";
        App app = new App();
//        app.basicTest(opType + "SimpleShort", simpleShort );
//        app.basicTest(opType + "SimpleLong", simpleLong );
//        app.basicTest(opType + "ShortComposite", shortComposite);
//        app.basicTest(opType + "LongComposite", longComposite);
        app.basicTest(opType + "shortAC", shortAC);
//        app.basicTest(opType + "longAC", longAC);
    }
    
    

    public void basicTest(String testName, String[] assignment) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        useTimeMeter();
        iterations(5);
//        ReasonerFactory.setReasonerFactory(PelletReasonerFactory.getInstance());

        for (int i = 100; i <= 100; i += 10) {

            final OrganizationalModel org1 = generateModel(i);

            final RawResourceAssignment qActive = loadAssignment(org1, assignment);

            System.out.println("Running for " + i + "-------");

            runAllPotentialParticipants(org1, qActive);

//            run("criticalParticipants",
//                    criticalParticipantsOperation(org1, qActive),
//                    p("TaskDuty", TaskDuty.RESPONSIBLE),
//                    p("Size ", i),
//                    p("Assignment", toString(qActive)));

//              run("consistency",
//                    basicConsistencyOperation(org1, qActive),
//                    p("TaskDuty", TaskDuty.RESPONSIBLE),
//                    p("Size ", i),
//                    p("Assignment", toString(qActive)));

        }

        exportTxt(console());
        exportHtml(file(testName + "-" + nowString() + ".html"));
        exportJson(file(testName + "-" + nowString() + ".json"));
    }

    private OperationRun criticalParticipantsOperation(final OrganizationalModel org1, final RawResourceAssignment q2) {
        return new OperationRun() {
            DTSubClassRALAnalyser analyser;

            @Override
            public void setup() {
                Map<String, Long> setupMeasures = new HashMap<>();
                analyser = (DTSubClassRALAnalyser) owlAnalyser(org1, q2, false, setupMeasures);
                addInfo("Setup measure", mapToString(setupMeasures));
            }

            @Override
            public Object run() {
                return analyser.criticalParticipants(TaskDuty.RESPONSIBLE);

            }
        };
    }


    private OperationRun basicConsistencyOperation(final OrganizationalModel org1, final RawResourceAssignment q2) {
        return new OperationRun() {
            DTSubClassRALAnalyser analyser;

            @Override
            public void setup() {
                Map<String, Long> setupMeasures = new HashMap<>();
                analyser = (DTSubClassRALAnalyser) owlAnalyser(org1, q2, false, setupMeasures);
                addInfo("Setup measure", mapToString(setupMeasures));
            }

            @Override
            public Object run() {
                return analyser.basicConsistency(null, TaskDuty.RESPONSIBLE);

            }
        };
    }

    private void runAllPotentialParticipants(final OrganizationalModel org1, final RawResourceAssignment q2) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, Long> setupMeasures = new HashMap<>();
        boolean fullPrecompute = true;
        String method = "classicPotentialParticipants";
        final DTSubClassRALAnalyser analyser = (DTSubClassRALAnalyser) owlAnalyser(org1, q2, fullPrecompute, setupMeasures);
        analyser.getClass().getMethod(method).invoke(analyser);
//        analyser.peoplePotentialParticipants();

        for (final String activity : q2.getActivities()) {
            run("potentialPerformers",
                    new OperationRun() {
                        @Override
                        public Object run() {
                            return analyser.potentialParticipants(activity, TaskDuty.RESPONSIBLE);
                        }
                    },
                    p("activity", activity),
                    p("query", q2.get(activity, TaskDuty.RESPONSIBLE)),
                    p("TaskDuty", TaskDuty.RESPONSIBLE),
                    p("Size", org1.getPersons().size()),
                    p("Assignment", toString(q2)),
                    p("Setup measure", mapToString(setupMeasures)),
                    p("Full precompute", fullPrecompute),
                    p("Type", method)
            );
        }
    }
    
}
