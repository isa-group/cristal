package es.us.isa.cristal.team;

import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.parser.RALParser;

/**
 * RALTeamMember
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class RALTeamMember extends RALTeamDefinitions{

    private IdMapper idMapper;
    private OwlRalMapper owlRalMapper;
    private String pid;

    public RALTeamMember(IdMapper idMapper, OwlRalMapper owlRalMapper, String pid) {
        this.idMapper = idMapper;
        this.owlRalMapper = owlRalMapper;
        this.pid = pid;
    }

    public class PeopleSelection {
        private String query;

        private PeopleSelection(String query) {
            this.query = query;
        }

        private String getQuery() {
            return query;
        }
    }

    private class TCardinality {
        private String query;

        private TCardinality(String query) {
            this.query = query;
        }

        private String getQuery() {
            return query;
        }
    }

    public String createdBySomeone(PeopleSelection who) {
        return new StringBuilder().
                append(HASCREATOR).append(" SOME ").append("(").append(who.getQuery()).append(")").
                toString();
    }

    public String whoseMembersInclude(String personId) {
        StringBuilder sb = membersIncludeBuilder(HASMEMBER, "value", idMapper.mapPerson(personId));
        return sb.toString();
    }

    public String whoseMembersIncludePlayingRoleType(String personId, String teamRoleTypeId) {
        return membersIncludeBuilder(
                idMapper.mapGroup(teamRoleTypeId),
                "value",
                idMapper.mapPerson(personId)).toString();
    }

    public String whoseMembersInclude(TCardinality cardinality, PeopleSelection who) {
        return membersIncludeBuilder(
                HASMEMBER,
                cardinality.getQuery(),
                who.getQuery()).toString();
    }

    public String whoseMembersIncludePlayingRoleType(TCardinality cardinality, PeopleSelection who, String teamRoleTypeId) {
        return membersIncludeBuilder(
                idMapper.mapGroup(teamRoleTypeId),
                cardinality.getQuery(),
                who.getQuery()).toString();
    }

    public TCardinality onlyPeople() {
        return new TCardinality("only");
    }

    public TCardinality atLeast(int num) {
        return new TCardinality("min " + num);
    }

    public TCardinality atMost(int num) {
        return new TCardinality("max " + num);
    }

    public TCardinality exactly(int num) {
        return new TCardinality("exactly " + num);
    }

    public PeopleSelection who(String ralExpr) {
        RALExpr expr = RALParser.parse(ralExpr);
        return new PeopleSelection(owlRalMapper.map(expr, pid));
    }

    public PeopleSelection whoMemberOf(String teamExpr) {
        StringBuilder sb = new StringBuilder().
                append("inverse(").append(HASMEMBER).append(") some (").append(teamExpr).append(")");

        return new PeopleSelection(sb.toString());
    }

    public PeopleSelection whoNotMemberOf(String teamExpr) {
        StringBuilder sb = new StringBuilder().
                append(PERSON).append(" and ").
                append(" not(").append(whoMemberOf(teamExpr).getQuery()).append(")");
        return new PeopleSelection(sb.toString());
    }

    private StringBuilder membersIncludeBuilder(String property, String cardinality, String who) {
        return new StringBuilder().
                append(TEAM).append(" and ").
                append(property).append(" ").append(cardinality).append(" ").append("(").append(who).append(")");
    }
}
