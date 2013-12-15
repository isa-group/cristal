package es.us.isa.cristal.owl;

import org.semanticweb.owlapi.model.IRI;

public class Definitions {

    public static final String CRISTAL_URL = "http://www.isa.us.es/cristal/";
	public static final String BP_URL = "http://www.isa.us.es/ontologies/";
	
	public static final IRI ORGANIZATION_IRI = IRI.create(CRISTAL_URL + "organization.owl");
	public static final IRI BPMN_IRI = IRI.create(BP_URL + "bpmn.owl");
	public static final IRI BPRELATIONSHIPS_IRI = IRI.create(BP_URL + "AbstractBP-relationships.owl");
	public static final IRI BP_IRI = IRI.create(BP_URL + "AbstractBP.owl");

    public static final String BPMN = "bpmn:";

    public static final String ACTIVE = BPMN + "active";
    public static final String COMPLETED = BPMN + "completed";
    public static final String READY = BPMN + "ready";

    // Abstract BP definitions
    public static final String ABSTRACTBP = "AbstractBP:";

    public static final String ISPOTENTIALPARTICIPANT = ABSTRACTBP + "isPotentialParticipant";
    public static final String ISPOTENTIALRESPONSIBLE = ABSTRACTBP + "isPotentialResponsible";
    public static final String ISPOTENTIALACCOUNTABLE = ABSTRACTBP + "isPotentialAccountable";
    public static final String ISPOTENTIALINFORMED = ABSTRACTBP + "isPotentialInformed";
    public static final String ISPOTENTIALSUPPORT = ABSTRACTBP + "isPotentialSupport";
    public static final String ISPOTENTIALCONSULTED = ABSTRACTBP + "isPotentialConsulted";

    public static final String HASPARTICIPANT = ABSTRACTBP + "hasParticipant";
    public static final String HASRESPONSIBLE = ABSTRACTBP + "hasResponsible";
    public static final String HASACCOUNTABLE = ABSTRACTBP + "hasAccountable";
    public static final String HASINFORMED = ABSTRACTBP + "hasInformed";
    public static final String HASSUPPORT = ABSTRACTBP +  "hasSupport";
    public static final String HASCONSULTED = ABSTRACTBP +"hasConsulted";

    public static final String POTPARTICIPANT = ABSTRACTBP + "potentialInstanceParticipant";
    public static final String POTRESPONSIBLE = ABSTRACTBP + "potentialInstanceResponsible";
    public static final String POTACCOUNTABLE = ABSTRACTBP + "potentialInstanceAccountable";
    public static final String POTINFORMED    = ABSTRACTBP + "potentialInstanceInformed";
    public static final String POTSUPPORT     = ABSTRACTBP + "potentialInstanceSupport";
    public static final String POTCONSULTED   = ABSTRACTBP + "potentialInstanceConsulted";

    public static final String ACTIVITY = ABSTRACTBP + "Activity";
    public static final String INITIALACTIVITY = ABSTRACTBP + "initialActivity";

    public static final String PROCESSINSTANCE = ABSTRACTBP + "ProcessInstance";
    public static final String ACTIVITYINSTANCE = ABSTRACTBP + "ActivityInstance";
    public static final String DATAOBJECTINSTANCE = ABSTRACTBP + "DataObjectInstance";

    public static final String HISTORY = ABSTRACTBP + "History";
    public static final String HIST = ABSTRACTBP + "hist";

    public static final String HASBPEXECUTION = ABSTRACTBP + "hasBPExecution";
    public static final String ISOFTYPE = ABSTRACTBP + "isOfType";
    public static final String HASACTIVITYINSTANCE = ABSTRACTBP + "hasActivityInstance";
    public static final String HASDATAOBJECTINSTANCE = ABSTRACTBP + "hasDataObjectInstance";
    public static final String HASSTATE = ABSTRACTBP + "hasState";
    public static final String STATE = ABSTRACTBP + "state";
    public static final String HASACTIVITY = ABSTRACTBP + "hasActivity";

    public static final String AFTERALLOCATION = ABSTRACTBP + "AfterAllocation";
    public static final String BEFOREALLOCATION = ABSTRACTBP + "BeforeAllocation";
    public static final String STARTSTATE = ABSTRACTBP + "StartState";
    public static final String ENDSTATE = ABSTRACTBP + "EndState";

    public static final String BPRELATIONSHIPS = "AbstractBP-relationships:";

    public static final String WEAKORDER = BPRELATIONSHIPS + "weakOrder";
    public static final String MANDATORY = BPRELATIONSHIPS + "mandatory";


    // Organization definitions
    public static final String ORGANIZATION = "organization:";

    public static final String OCCUPIES = ORGANIZATION + "occupies";
    public static final String ISMEMBEROF = ORGANIZATION + "isMemberOf";
    public static final String PARTICIPATESIN = ORGANIZATION + "participatesIn";

    public static final String HASCAPABILITY = ORGANIZATION + "hasCapability";

    public static final String REPORTSTO = ORGANIZATION + "reportsTo";
    public static final String EXTENDEDREPORTSTO = ORGANIZATION + "extendedReportsTo";

    public static final String CANDELEGATEWORKTO = ORGANIZATION + "canDelegateWorkTo";

    public static final String PERSON = ORGANIZATION + "Person";
    public static final String POSITION = ORGANIZATION + "Position";
    public static final String ROLE = ORGANIZATION + "Role";
    public static final String UNIT = ORGANIZATION + "OrganizationalUnit";

}
