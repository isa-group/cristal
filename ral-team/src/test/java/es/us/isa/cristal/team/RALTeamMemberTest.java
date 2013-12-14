package es.us.isa.cristal.team;

import es.us.isa.cristal.owl.OntologyNamespaces;
import es.us.isa.cristal.owl.mappers.ral.DTOwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import org.junit.Before;
import org.junit.Test;

/**
 * RALTeamMemberTest
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class RALTeamMemberTest {
    private OntologyNamespaces namespaces;

    @Before
    public void setUp() throws Exception {
        namespaces = new OntologyNamespaces();
        namespaces.setPerson("teams-example", "teams-example-iri");
        namespaces.setGroup("teams-example", "teams-example-iri");
        namespaces.setActivity("test", "teams-example-iri");
    }

    @Test
    public void testMember() throws Exception {
        IdMapper idMapper = new IdMapper(namespaces);
        RALTeamCore r = new RALTeamCore(idMapper);
        RALTeamMember m = new RALTeamMember(idMapper, new DTOwlRalMapper(idMapper, new BPEngineMock()), "1");

        System.out.println(
                r.and(
                        r.ofType("type1"),
                        m.createdBySomeone(m.whoMemberOf(
                                r.containing("teamType1", "teamType2")
                        ))
                )
        );

        System.out.println(
                m.whoseMembersInclude(m.onlyPeople(), m.who("HAS ROLE PhdStudent"))
        );

        System.out.println(
                m.whoseMembersIncludePlayingRoleType(m.atLeast(1), m.who("HAS POSITION Analyst"), "developer")
        );

    }

}
