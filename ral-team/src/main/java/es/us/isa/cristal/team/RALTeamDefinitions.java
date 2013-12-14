package es.us.isa.cristal.team;

import es.us.isa.cristal.owl.Definitions;
import org.semanticweb.owlapi.model.IRI;

/**
 * RALTeamDefinitions
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class RALTeamDefinitions extends Definitions {
    public static final IRI TEAMS_IRI = IRI.create(CRISTAL_URL + "teams.owl");

    public static final String TEAMS_PREFIX = "teams:";


    // Classes ------------------------------------------------------------------------------------------

    public static final String TEAM = TEAMS_PREFIX + "Team";
    public static final String PERMANENTTEAM = TEAMS_PREFIX + "PermanentTeam";
    public static final String TEMPORARYTEAM = TEAMS_PREFIX + "TemporaryTeam";
    public static final String SCOPE = TEAMS_PREFIX + "Scope";
    public static final String PROCESSINSTANCESCOPE = TEAMS_PREFIX + "ProcessInstanceScope";
    public static final String ACTIVITYINSTANCESCOPE = TEAMS_PREFIX + "ActivityInstanceScope";
    public static final String TEMPORALSCOPE = TEAMS_PREFIX + "TemporalScope";
    public static final String TEAMTYPE = TEAMS_PREFIX + "TeamType";
    public static final String TEAMROLETYPE = TEAMS_PREFIX + "TeamRoleType";


    // Object properties----------------------------------------------------------------------------------

    public static final String FORMEDWITHIN = TEAMS_PREFIX + "formedWithin";
    public static final String HASCREATOR = TEAMS_PREFIX + "hasCreator";
    public static final String HASMEMBER = TEAMS_PREFIX + "hasMember";
    public static final String HASTYPE = TEAMS_PREFIX + "hasType";
    public static final String ROLETYPE = TEAMS_PREFIX + "roleType";
    public static final String AI = TEAMS_PREFIX + "ai";
    public static final String PI = TEAMS_PREFIX + "pi";

    public static final String TEAMREPORTSTO = TEAMS_PREFIX + "teamReportsTo";
    public static final String EXTENDEDTEAMREPORTSTO = TEAMS_PREFIX + "extendedTeamReportsTo";
    public static final String TEAMCANDELEGATEWORKTO = TEAMS_PREFIX + "teamCanDelegateWorkTo";



    // Data properties----------------------------------------------------------------------------------

    public static final String STARTDATE = TEAMS_PREFIX + "startDate";
    public static final String ENDDATE = TEAMS_PREFIX + "endDate";

}
