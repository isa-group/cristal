package es.us.isa.cristal.team;

/**
 * RALTeamHierarchy
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class RALTeamHierarchy extends RALTeamDefinitions {
    public String thatReportsTo(String teamExpr) {
        return hierarchyBuilder(EXTENDEDTEAMREPORTSTO, teamExpr).toString();
    }

    public String thatReportsDirectlyTo(String teamExpr) {
        return hierarchyBuilder(TEAMREPORTSTO, teamExpr).toString();
    }

    public String thatIsReportedBy(String teamExpr) {
        return inverseHierarchyBuilder(EXTENDEDTEAMREPORTSTO, teamExpr).toString();
    }

    public String thatIsDirectlyReportedBy(String teamExpr) {
        return inverseHierarchyBuilder(TEAMREPORTSTO, teamExpr).toString();
    }

    public String thatCanDelegateWorkTo(String teamExpr) {
        return hierarchyBuilder(TEAMCANDELEGATEWORKTO, teamExpr).toString();
    }

    public String thatCanHaveWorkDelegatedBy(String teamExpr) {
        return inverseHierarchyBuilder(TEAMCANDELEGATEWORKTO, teamExpr).toString();
    }

    private StringBuilder inverseHierarchyBuilder(String property, String teamExpr) {
        return new StringBuilder().append("inverse(").append(property).append(") some (").append(teamExpr).append(")");
    }

    private StringBuilder hierarchyBuilder(String property, String teamExpr) {
        return new StringBuilder().append(property).append(" some (").append(teamExpr).append(")");
    }
}
