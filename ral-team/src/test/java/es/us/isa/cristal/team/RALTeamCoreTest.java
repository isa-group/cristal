package es.us.isa.cristal.team;

import es.us.isa.cristal.owl.OntologyNamespaces;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import org.junit.Before;
import org.junit.Test;

/**
 * RALTeamCoreTest
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class RALTeamCoreTest {
    private OntologyNamespaces namespaces;

    @Before
    public void setUp() throws Exception {
        namespaces = new OntologyNamespaces();
        namespaces.setPerson("teams-example", "teams-example-iri");
        namespaces.setGroup("teams-example", "teams-example-iri");
        namespaces.setActivity("test", "teams-example-iri");
    }

    @Test
    public void testCore() throws Exception {
        RALTeamCore r = new RALTeamCore(new IdMapper(namespaces));

        System.out.println(r.and(r.ofType("type1"), r.createdBy("p1")));

    }
}
