package es.us.isa.cristal.owl.analysers;


import com.complexible.common.openrdf.model.Graphs;
import com.complexible.stardog.StardogException;
import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.api.IO;
import com.complexible.stardog.api.SelectQuery;
import com.complexible.stardog.api.admin.AdminConnection;
import com.complexible.stardog.api.admin.AdminConnectionConfiguration;
import com.complexible.stardog.api.reasoning.ReasoningConnection;
import com.complexible.stardog.db.DatabaseOptions;
import com.complexible.stardog.protocols.snarl.SNARLProtocolConstants;
import com.complexible.stardog.reasoning.api.ReasoningType;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.DLHelper;
import es.us.isa.cristal.owl.DLQueryEngine;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.RALOntologyManager;
import es.us.isa.cristal.owl.mappers.ral.designtimesc.DTSubClassAssignmentOntology;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.InstanceTaskDutyMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.TaskDutyMapper;
import org.openrdf.model.Graph;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.rio.RDFFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class DTStardogRALAnalyser extends AbstractRALAnalyser {

    private static final Logger log = Logger.getLogger(DTStardogRALAnalyser.class.getName());
    private static String SERVER = "http://localhost:5820/";

    private TaskDutyMapper taskDutyMapper;
    private Set<String> organizationPeople;
    private DTSubClassAssignmentOntology.ActivityMapper activityMapper;
    private DefaultPrefixManager prefixManager;

    public DTStardogRALAnalyser(DLQueryEngine engine, IdMapper mapper, DefaultPrefixManager prefixManager, DTSubClassAssignmentOntology.ActivityMapper activityMapper) {
		super(engine, mapper);
        this.activityMapper = activityMapper;
        this.prefixManager = prefixManager;

        OWLReasoner reasoner = engine.getReasoner();
        OWLOntology ontology = reasoner.getRootOntology();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();



        try {
//            Server aServer = Stardog
//                    .buildServer()
//                    .bind(SNARLProtocolConstants.EMBEDDED_ADDRESS)
//                    .start();

            AdminConnection aAdminConnection = AdminConnectionConfiguration.toServer(SERVER)
                    .credentials("admin", "admin")
                    .connect();

            if (aAdminConnection.list().contains("reasoningExampleTest")) {
                aAdminConnection.drop("reasoningExampleTest");
            }

            // Convenience function for creating a non-persistent in-memory database with all the default settings.
            aAdminConnection.disk("reasoningExampleTest").create();

            // *ALWAYS* close your connections!
            aAdminConnection.close();

            ReasoningConnection aReasoningConn = ConnectionConfiguration
                    .to("reasoningExampleTest")
                    .server(SERVER)
                    .credentials("admin", "admin")
                    .reasoning(ReasoningType.SL)
                    .connect()
                    .as(ReasoningConnection.class);

            aReasoningConn.begin();

            IO aAdder = aReasoningConn.add().io().format(RDFFormat.RDFXML);

            Set<OWLOntology> importsClosure = ontology.getImportsClosure();
            for (OWLOntology ont : importsClosure) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                manager.saveOntology(ont, outputStream);
                InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                aAdder.stream(inputStream);
            }

            ByteArrayOutputStream assignmentStream = new ByteArrayOutputStream();
            manager.saveOntology(ontology, assignmentStream);
            InputStream assignments = new ByteArrayInputStream(assignmentStream.toByteArray());

            aAdder.stream(assignments);

            aReasoningConn.commit();

            URI orgPeople = ValueFactoryImpl.getInstance().createURI(prefixManager.getIRI(Definitions.ORGANIZATIONPEOPLE).toString());

            SelectQuery aQuery = aReasoningConn.select("SELECT ?x WHERE {\n" +
                            "?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type\n" +
                            "}").reasoning(ReasoningType.SL);
            aQuery.parameter("type", orgPeople);
            TupleQueryResult result = aQuery.execute();

            log.info("org people: " + StardogHelper.getElements(result, "x"));

            log.info("consistent: " + aReasoningConn.isConsistent());
            log.info("unstaisf: " + aReasoningConn.getUnsatisfiableClasses());

            SelectQuery criticalQuery = aReasoningConn.select("SELECT ?x WHERE {\n" +
                    "?x <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?type\n" +
                    "}").reasoning(ReasoningType.SL);

            URI critical = ValueFactoryImpl.getInstance().createURI(Definitions.ORGANIZATION_IRI+"#critical");
            criticalQuery.parameter("type", critical);
            result = criticalQuery.execute();
            log.info("critical: " + StardogHelper.getElements(result, "x"));


            aReasoningConn.begin();
            result = aQuery.execute();

            URI aContext = ValueFactoryImpl.getInstance().createURI("urn:test:context");
            URI type = ValueFactoryImpl.getInstance().createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
            URI a8 = ValueFactoryImpl.getInstance().createURI("bpi:a8");
            URI hasResponsible = ValueFactoryImpl.getInstance().createURI(prefixManager.getIRI(Definitions.HASRESPONSIBLE).toString());
            URI act = ValueFactoryImpl.getInstance().createURI("http://localhost/bpperformance#activity8");
            Statement a8statement = ValueFactoryImpl.getInstance().createStatement(a8, type, act);
            aReasoningConn.add().statement(a8statement);

            aReasoningConn.commit();

            while (result.hasNext()) {
                aReasoningConn.begin();

                URI r = ValueFactoryImpl.getInstance().createURI(result.next().getValue("x").stringValue());
                aReasoningConn.add().statement(r, hasResponsible, a8, aContext);

                aReasoningConn.commit();
                log.info("cons instancias: " + aReasoningConn.isConsistent());

                aReasoningConn.begin();
                aReasoningConn.remove().context(aContext);
                aReasoningConn.commit();
            }


            aReasoningConn.close();

        } catch (StardogException e) {
            throw new RuntimeException(e);
        } catch (OWLOntologyStorageException e) {
            throw new RuntimeException(e);
        } catch (QueryEvaluationException e) {
            e.printStackTrace();
        }


//        List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
////        gens.add(new InferredClassAssertionAxiomGenerator());
//        gens.add(new InferredSubClassAxiomGenerator());
////        gens.add(new InferredInverseObjectPropertiesAxiomGenerator());

//        InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, gens);
//        iog.fillOntology(manager, ontology);

//        try {
//            manager.saveOntology(ontology, System.out);
//        } catch (OWLOntologyStorageException e) {
//            e.printStackTrace();
//        }


//        OWLClass orgPeople = factory.getOWLClass(Definitions.ORGANIZATIONPEOPLE, prefixManager);
//        Set<OWLIndividual> individuals = orgPeople.getIndividuals(ontology);
//        organizationPeople = DLHelper.mapFromOwlIndividual(individuals);
//
//        organizationPeople = DLHelper.mapFromOwl(engine.getInstances(Definitions.ORGANIZATIONPEOPLE, false));
//
//        log.info("org people: " + organizationPeople);

        taskDutyMapper = new InstanceTaskDutyMapper();
    }

    private void testQuery(String query) {
        System.out.println(query);
        Set<OWLNamedIndividual> a = engine.getInstances(query, false);
        System.out.println(a);

    }


    private Set<String> classicPP(String activityName, TaskDuty duty) {
        String hasDuty =  taskDutyMapper.map(duty);
        String expressionString = Definitions.ORGANIZATIONPEOPLE + " and not ( inverse("+hasDuty + ") some (" + activityMapper.mapActivity(activityName)+"))";
        log.info("PotentialParticipant: " + expressionString);

        Set<String> notParticipants = DLHelper.mapFromOwl(engine.getInstances(expressionString, false));

//        OWLReasoner reasoner = engine.getReasoner();
//        OWLOntology ontology = reasoner.getRootOntology();
//        OWLOntologyManager manager = ontology.getOWLOntologyManager();
//        OWLDataFactory factory = manager.getOWLDataFactory();
//        OWLClass assign = factory.getOWLClass(activityMapper.mapAssignment(activityName), prefixManager);
//        Set<String> notParticipants = DLHelper.mapFromOwlIndividual(assign.getIndividuals(ontology));
        log.info("Not potential participants: " + notParticipants);

        Set<String> result = new HashSet<String>(organizationPeople);
        result.removeAll(notParticipants);

        return result;

    }

    @Override
    public Set<String> potentialParticipants(String activityName, TaskDuty duty) {
//        return classicPP(activityName, duty);
        return peoplePP(activityName, duty);
    }

    private Set<String> peoplePP(String activityName, TaskDuty duty) {
        String hasDuty =  taskDutyMapper.map(duty);
        OWLReasoner reasoner = engine.getReasoner();
        OWLOntology ontology = reasoner.getRootOntology();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();

        OWLClassExpression classExpression = factory.getOWLObjectSomeValuesFrom(
                factory.getOWLObjectInverseOf(factory.getOWLObjectProperty(hasDuty, prefixManager)),
                factory.getOWLClass(activityMapper.mapActivity(activityName), prefixManager));

        Set<OWLNamedIndividual> result = new HashSet<OWLNamedIndividual>();

        Set<OWLNamedIndividual> potential = engine.getInstances(activityMapper.mapAssignment(activityName), false);



        // Hope for the best
        for (OWLNamedIndividual p: potential) {
            OWLAxiom axiom = factory.getOWLClassAssertionAxiom(classExpression, p);
            manager.addAxiom(ontology,axiom);
        }
        reasoner.flush();

        if (!reasoner.isConsistent()) {
            for (OWLNamedIndividual p: potential) {
                OWLAxiom axiom = factory.getOWLClassAssertionAxiom(classExpression, p);
                manager.removeAxiom(ontology,axiom);
            }
            reasoner.flush();
            for (OWLNamedIndividual p : potential) {
                OWLAxiom axiom = factory.getOWLClassAssertionAxiom(classExpression, p);

                log.info(axiom.toString());

                manager.addAxiom(ontology, axiom);
                reasoner.flush();

                if (reasoner.isConsistent()) {
                    result.add(p);
                } else {
                    manager.removeAxiom(ontology, axiom);
                    reasoner.flush();
                }

            }
        } else {
            result = potential;
        }


        return DLHelper.mapFromOwl(result);
    }

    @Override
    public Set<String> potentialActivities(String personName, TaskDuty duty) {
        String isPotentialDuty = taskDutyMapper.map(duty);
        String expressionString = Definitions.ACTIVITYINSTANCE + " and not(" +isPotentialDuty+" value "+ idMapper.mapPerson(personName)+")";
        log.info("PotentialActivities: " + expressionString);

        Set<String> notActivities = DLHelper.mapClassesFromOwl(engine.getSubClasses(expressionString, false));
        log.info("Not potential activities: " + notActivities);

        Set<String> activities = DLHelper.mapClassesFromOwl(engine.getSubClasses(Definitions.ACTIVITYINSTANCE, false));
        activities.removeAll(notActivities);

        return activities;
    }
	
	public Set<String> nonParticipants(Iterable<String> activities, TaskDuty duty) {
        String hasDuty =  taskDutyMapper.map(duty);
        List<String> act = new ArrayList<String>();
        for (String a : activities) {
            act.add(idMapper.mapActivity(a));
        }

        String expressionString = Definitions.ORGANIZATIONPEOPLE + " and not (inverse("+hasDuty + ") some (" + Definitions.ISOFTYPE + " some { " + DLHelper.joinWith(act, ",") +"}))";
        log.info("NonParticipants: " + expressionString);
        return DLHelper.mapFromOwl(engine.getInstances(expressionString, false));
    }

    public Set<String> permanentParticipants(Iterable<String> activities, TaskDuty duty) {
        String hasDuty = taskDutyMapper.map(duty);
        List<String> act = new ArrayList<String>();
        for (String a : activities) {
//            act.add(isPotentialDuty + " value " + idMapper.mapActivity(a));
            act.add("("+Definitions.PERSON + " and not (inverse(" + hasDuty + ") some (" + Definitions.ISOFTYPE + " value " + idMapper.mapActivity(a) + ")))");

        }

        String query = DLHelper.joinWith(act, " or ");
        log.info("PermanentParticipants: " + query);
        Set<String> notParticipants = DLHelper.mapFromOwl(engine.getInstances(query, false));
        log.info("Not potential participants: " + notParticipants);

        Set<String> participants = new HashSet<String>(organizationPeople);
        participants.removeAll(notParticipants);

        return participants;
    }

    public Set<String> criticalActivities(String person, TaskDuty duty) {
        String hasDuty = taskDutyMapper.map(duty);

        String query = hasDuty + " value " + idMapper.mapPerson(person);
        log.info("CriticalActivities query: " + query);

        Set<String> criticalActivities = DLHelper.mapClassesFromOwl(engine.getSubClasses(query, false));
        log.info("CriticalActivities result: " + criticalActivities);

        return criticalActivities;
    }

    public Set<String> notInvolved(String person, TaskDuty duty) {
        String hasDuty = taskDutyMapper.map(duty);
        String expressionString = Definitions.ACTIVITYINSTANCE + " and not(" + hasDuty + " value " + idMapper.mapPerson(person) + ")";
        log.info("notInvolved query: " + expressionString);

        Set<String> notActivities = DLHelper.mapClassesFromOwl(engine.getSubClasses(expressionString, false));
        log.info("notInvolved result: " + notActivities);

        return notActivities;
    }

    public Set<String> criticalActivities(Iterable<String> activities, TaskDuty duty) {
        String hasDuty = taskDutyMapper.map(duty);

        String q = Definitions.ACTIVITYINSTANCE + " and " + hasDuty + " exactly 1 " + Definitions.ORGANIZATIONPEOPLE;
        log.info("Critical activities query:" + q);

        Set<String> criticalActivities = DLHelper.mapClassesFromOwl(engine.getSubClasses(activityMapper.mapActivity("critical"), false));
        log.info("Critical activities result: " + criticalActivities);

        return criticalActivities;
    }

	public Set<String> criticalParticipants(Iterable<String> activities, TaskDuty duty) {
        Set<String> criticalActivities = criticalActivities(activities, duty);
        String isPotentialDuty = taskDutyMapper.map(duty);
        List<String> act = new ArrayList<String>();
        for (String a : criticalActivities) {
            act.add(isPotentialDuty + " value " + idMapper.mapActivity(a));
        }

        String query = DLHelper.joinWith(act, " or ");
        log.info("CriticalParticipants: " + query);
        return DLHelper.mapFromOwl(engine.getInstances(query, false));
    }

    public Set<String> indispensableParticipants(Iterable<String> activities, TaskDuty duty) {
        Set<String> mandatoryActivities = mandatoryActivities(activities);
        return criticalParticipants(mandatoryActivities, duty);
    }

    public Set<String> mandatoryActivities(Iterable<String> activities) {
        List<String> act = new ArrayList<String>();
        for (String a : activities) {
            act.add(idMapper.mapActivity(a));
        }

        String activitiesSet = "{" + DLHelper.joinWith(act, ", ") + "}";
        String query = "inverse(" + Definitions.MANDATORY + ") some (inverse(" + Definitions.INITIALACTIVITY + ") some ( " + Definitions.HASACTIVITY + " some " + activitiesSet + "))";
        testQuery(query);

        return DLHelper.mapFromOwl(engine.getInstances(query + " and " + activitiesSet, false));
    }

    public boolean basicConsistency(String activity, TaskDuty duty) {
        OWLReasoner reasoner = engine.getReasoner();

        Set<OWLClass> inconsistentClasses = reasoner.getUnsatisfiableClasses().getEntitiesMinusBottom();
        log.info("inc: " + inconsistentClasses);

        return inconsistentClasses.isEmpty();
    }

}
