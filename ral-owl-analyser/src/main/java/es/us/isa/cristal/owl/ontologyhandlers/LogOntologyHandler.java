package es.us.isa.cristal.owl.ontologyhandlers;

import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.OntologyHandler;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.InstanceTaskDutyMapper;
import org.semanticweb.owlapi.model.*;

/**
 * User: resinas
 * Date: 12/07/13
 * Time: 11:35
 */
public class LogOntologyHandler extends OntologyHandler {
    private IdMapper mapper;
    private OWLOntologyManager manager;
    private OWLDataFactory factory;


    public LogOntologyHandler(OntologyHandler logOntology, IdMapper mapper) {
        super(logOntology.getOntology(), logOntology.getPrefixManager());
        this.mapper = mapper;
        this.manager = ontology.getOWLOntologyManager();
        this.factory = manager.getOWLDataFactory();
    }

    public ProcessInstance processInstance(String processName, String pid) {
        OWLNamedIndividual process = individual(mapper.mapActivity(processName));
        OWLNamedIndividual currentInstance = individual(getLogIri(pid));
        OWLNamedIndividual hist = individual(Definitions.HIST);

        classAssertion(Definitions.PROCESSINSTANCE, currentInstance);
        propertyAssertion(Definitions.ISOFTYPE, currentInstance, process);
        propertyAssertion(Definitions.HASBPEXECUTION, hist, currentInstance);

        return new ProcessInstance(pid);
    }

    public String getTypeOf(OWLNamedIndividual activityInstance) {
        OWLObjectProperty isOfType = factory.getOWLObjectProperty(Definitions.ISOFTYPE, prefixManager);
        OWLNamedIndividual i = activityInstance.getObjectPropertyValues(isOfType, ontology).iterator().next().asOWLNamedIndividual();
        return i.getIRI().getFragment();
    }

    public void propertyAssertion(String property, OWLNamedIndividual source, OWLNamedIndividual target) {
        OWLObjectProperty prop = factory.getOWLObjectProperty(property, prefixManager);
        manager.addAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(prop, source, target));
    }

    public void noInversePropertyAssertion(String property, OWLNamedIndividual source) {
        OWLObjectPropertyExpression invp = factory.getOWLObjectProperty(property, prefixManager).getInverseProperty();
        OWLClass empty = factory.getOWLNothing();
        OWLClassExpression indiv = factory.getOWLObjectOneOf(source);
        OWLClassExpression propertyOfSource = factory.getOWLObjectSomeValuesFrom(invp, indiv);
        OWLAxiom equiv = factory.getOWLSubClassOfAxiom(propertyOfSource, empty);
        manager.addAxiom(ontology, equiv);
    }


    public void classAssertion(String type, OWLNamedIndividual instance) {
        OWLClass typeInstance = factory.getOWLClass(type, prefixManager);
        OWLAxiom classAssertion = factory.getOWLClassAssertionAxiom(typeInstance, instance);
        manager.addAxiom(ontology, classAssertion);
    }

    private IRI getLogIri(String pid) {
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI();
        return IRI.create(ontologyIRI + "#" + pid);
    }

    private OWLNamedIndividual individual(IRI iri) {
        return factory.getOWLNamedIndividual(iri);
    }

    public OWLNamedIndividual individual(String name) {
        return factory.getOWLNamedIndividual(name, prefixManager);
    }

    private void activityState(String activityName, String aid, String pid, ActivityState state) {
        OWLNamedIndividual currentActivity = individual(getLogIri(aid));
        OWLNamedIndividual currentProcess = individual(getLogIri(pid));
        OWLNamedIndividual activity = individual(mapper.mapActivity(activityName));
        OWLNamedIndividual currentState = individual(mapState(state));

        classAssertion(Definitions.ACTIVITYINSTANCE, currentActivity);
        propertyAssertion(Definitions.ISOFTYPE, currentActivity, activity);
        propertyAssertion(Definitions.HASACTIVITYINSTANCE, currentProcess, currentActivity);
        propertyAssertion(Definitions.HASSTATE, currentActivity, currentState);
    }

    private void activityParticipant(String aid, String participantName, TaskDuty duty) {
        OWLNamedIndividual currentActivity = individual(getLogIri(aid));
        OWLNamedIndividual participant = individual(mapper.mapPerson(participantName));

        String taskduty = new InstanceTaskDutyMapper().map(duty);
        propertyAssertion(taskduty, currentActivity, participant);

    }

    private void dataInstanceValue(String data, String pid, String field, String value) {
        OWLNamedIndividual currentData = individual(getLogIri(data));
        OWLNamedIndividual currentProcess = individual(getLogIri(pid));
        OWLNamedIndividual dataType = individual(mapper.mapActivity(data));
        OWLNamedIndividual valueElem = individual(mapper.mapPerson(value));

        classAssertion(Definitions.DATAOBJECTINSTANCE, currentData);
        propertyAssertion(Definitions.ISOFTYPE, currentData, dataType);
        propertyAssertion(Definitions.HASDATAOBJECTINSTANCE, currentProcess, currentData);
        propertyAssertion(mapper.mapActivity(field), currentData, valueElem);
    }

    private void dataInstanceState(String data, String pid, String state) {
        OWLNamedIndividual currentData = individual(getLogIri(data));
        OWLNamedIndividual currentProcess = individual(getLogIri(pid));
        OWLNamedIndividual currentState = individual(mapper.mapActivity(state));
        OWLNamedIndividual dataType = individual(mapper.mapActivity(data));

        classAssertion(Definitions.DATAOBJECTINSTANCE, currentData);
        propertyAssertion(Definitions.ISOFTYPE, currentData, dataType);
        propertyAssertion(Definitions.HASDATAOBJECTINSTANCE, currentProcess, currentData);
        propertyAssertion(Definitions.HASSTATE, currentData, currentState);
    }

    public enum ActivityState {
        READY,
        ALLOCATING,
        ALLOCATED,
        COMPLETING,
        COMPLETED
    }

    private String mapState(ActivityState state) {
        String map;
        switch (state) {
            case READY:
                map = Definitions.READY;
                break;
            case COMPLETED:
                map = Definitions.COMPLETED;
                break;
            default:
                map = Definitions.ACTIVE;
                break;
        }

        return map;
    }

    public class ProcessInstance {
        private String pid;

        public ProcessInstance(String pid) {
            this.pid = pid;
        }

        public ProcessInstance activity(String activity, String activityId, ActivityState state, String participant) {
            return this.activity(activity, activityId, state, participant, TaskDuty.RESPONSIBLE);
        }

        public ProcessInstance activity(String activity, String activityId, ActivityState state, String participant, TaskDuty duty) {
            activityState(activity, activityId, pid, state);
            activityParticipant(activityId, participant, duty);
            return this;
        }

        public ProcessInstance activity(String activity, String activityId, ActivityState state) {
            activityState(activity, activityId, pid, state);
            return this;
        }

        public ProcessInstance dataField(String data, String field, String value) {
            dataInstanceValue(data, pid, field, value);
            return this;
        }

        public ProcessInstance dataState(String data, String state) {
            dataInstanceState(data, pid, state);
            return this;
        }
    }
}
