package es.us.isa.cristal.team;

import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;

import java.util.Date;

/**
 * RALTeamCore
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class RALTeamCore extends RALTeamDefinitions{

    private IdMapper idMapper;

    public RALTeamCore(IdMapper idMapper) {
        this.idMapper = idMapper;
    }

    public enum TCardinalityConstraint {
        EXACTLY, ATLEAST, ATMOST, BETWEEN
    }

    public class ScopeConstraint {
        private String query;
        private ScopeConstraint(String query) {
            this.query = query;
        }
        private String getQuery() {
            return query;
        }
    }

    public String teamID(String teamId) {
        return "{" + idMapper.mapGroup(teamId) + "}";
    }

    public String and(String... teamExpr) {
        StringBuilder sb = compoundBuilder("and", teamExpr);
        return sb.toString();
    }

    public String or(String... teamExpr) {
        StringBuilder sb = compoundBuilder("OR", teamExpr);
        return sb.toString();
    }

    public String withSize(TCardinalityConstraint cardinality, int... num) {
        StringBuilder sb = new StringBuilder().append(TEAM).append(" AND ").append(HASMEMBER);

        switch (cardinality) {
            case EXACTLY:
                sb.append(" exactly ").append(num[0]);
                break;
            case ATLEAST:
                sb.append(" min ").append(num[0]);
                break;
            case ATMOST:
                sb.append(" max ").append(num[0]);
                break;
            case BETWEEN:
                sb.append(" min ").append(num[0]).append(" AND ").append(HASMEMBER).append(" max ").append(num[1]);
                break;
        }

        return sb.toString();
    }

    public String containing(String... teamRoleTypeList) {
        StringBuilder roleTypes = new StringBuilder();
        for (int i = 0; i < teamRoleTypeList.length - 1; i++) {
            roleTypes.append(idMapper.mapGroup(teamRoleTypeList[i])).append(", ");
        }
        roleTypes.append(idMapper.mapGroup(teamRoleTypeList[teamRoleTypeList.length - 1]));

        StringBuilder sb = new StringBuilder().append(ROLETYPE).append(" SOME ").append("{").append(roleTypes).append("}");
        return sb.toString();
    }

    public String ofType(String id) {
        StringBuilder sb = idBuilder(HASTYPE, id);
        return sb.toString();
    }

    public String ofTypeLike(String teamExpr) {
        StringBuilder sb = likeBuilder(HASTYPE, teamExpr);
        return sb.toString();
    }

    public String ofTypeUnlike(String teamExpr) {
        StringBuilder sb = unlikeBuilder(HASTYPE, teamExpr);
        return sb.toString();
    }

    public String createdBy(String personId) {
        return idBuilder(HASCREATOR, personId).toString();
    }

    public String createdByLike(String teamExpr) {
        return likeBuilder(HASCREATOR, teamExpr).toString();
    }

    public String createdByUnlike(String teamExpr) {
        return unlikeBuilder(HASCREATOR, teamExpr).toString();
    }

    public String withScope(ScopeConstraint scope) {
        StringBuilder sb = new StringBuilder().
                append(PERMANENTTEAM).append(" or ").
                append(FORMEDWITHIN).append(" some ").append("(").append(scope.getQuery()).append(")");
        return sb.toString();
    }

    public String withScopeLike(String teamExpr) {
        return likeBuilder(FORMEDWITHIN, teamExpr).toString();
    }

    public String withScopeUnlike(String teamExpr) {
        return unlikeBuilder(FORMEDWITHIN, teamExpr).toString();
    }

    public ScopeConstraint activeBetween(Date start, Date end) {
        StringBuilder sb = new StringBuilder().append(TEMPORALSCOPE).append(" AND ").
                append(STARTDATE).append(" some ").append("xsd:DateTimeStamp <= ").append(start.getTime()).append(" and ").
                append(ENDDATE).append(" some ").append("xsd:DateTimeStamp >= ").append(end.getTime());

        return new ScopeConstraint(sb.toString());
    }

    public ScopeConstraint activeDuringTheExecutionOfProcess(String pid) {
        StringBuilder sb = new StringBuilder().append(PROCESSINSTANCESCOPE).append(" and ").
                append(PI).append(" value ").append(idMapper.mapActivity(pid));
        return new ScopeConstraint(sb.toString());
    }

    public ScopeConstraint activeDuringTheExecutionOfActivity(String aid) {
        StringBuilder sb = new StringBuilder().append(ACTIVITYINSTANCESCOPE).append(" and ").
                append(AI).append(" value ").append(idMapper.mapActivity(aid));
        return new ScopeConstraint(sb.toString());
    }

    private StringBuilder compoundBuilder(String composition, String[] teamExpr) {
        StringBuilder sb = new StringBuilder();
        int i = 0;

        for (i = 0; i < teamExpr.length - 1; i++) {
            sb.append("(").append(teamExpr[i]).append(")").append(" ").append(composition).append(" ");
        }
        sb.append("(").append(teamExpr[i]).append(")");
        return sb;
    }

    private StringBuilder idBuilder(String property, String id) {
        return new StringBuilder().append(property).append(" SOME ").append("{").append(idMapper.mapGroup(id)).append("}");
    }

    private StringBuilder unlikeBuilder(String property, String teamExpr) {
        StringBuilder sb = new StringBuilder().
                append(TEAM).append(" AND ").append(" NOT (").append(likeBuilder(property, teamExpr).append(")"));
        return sb;
    }

    private StringBuilder likeBuilder(String property, String teamExpr) {
        return new StringBuilder().
                    append(property).append(" SOME ").
                    append("(").append("inverse(").append(property).append(") some (").append(teamExpr).append("))");
    }


}
