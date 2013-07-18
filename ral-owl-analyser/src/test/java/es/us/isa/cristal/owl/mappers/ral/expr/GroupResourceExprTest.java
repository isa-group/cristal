package es.us.isa.cristal.owl.mappers.ral.expr;

import es.us.isa.cristal.BPEngineMock;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.OntologyNamespaces;
import es.us.isa.cristal.owl.mappers.ral.InstanceOwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.parser.RALParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static es.us.isa.cristal.owl.HiringResolutionScenario.BP_HIRING_RESOLUTION_IRI;
import static es.us.isa.cristal.owl.HiringResolutionScenario.ORGANIZATION_IAAP_IRI;

/**
 * User: resinas
 * Date: 03/07/13
 * Time: 10:44
 */
public class GroupResourceExprTest {

    private OntologyNamespaces namespaces;
    private OwlRalMapper owlRalMapper;

    @Before
    public void setUp() throws Exception {
        namespaces = new OntologyNamespaces();
        namespaces.setPerson("organization-iaap", ORGANIZATION_IAAP_IRI.toString());
        namespaces.setGroup("organization-iaap", ORGANIZATION_IAAP_IRI.toString());
        namespaces.setActivity("bp-hiring-resolution", BP_HIRING_RESOLUTION_IRI.toString());
        owlRalMapper = new InstanceOwlRalMapper(new IdMapper(namespaces), new BPEngineMock());
    }

    @Test
    public void shouldMapPositionExpr() {
        RALExpr expr = RALParser.parse("HAS POSITION AssistantOfTheConsultativeBoard");
        String result = owlRalMapper.map(expr, 0);
        Assert.assertEquals("organization:occupies some ({organization-iaap:AssistantOfTheConsultativeBoard})", result);
    }

    @Test
    public void shouldMapUnits() {
        RALExpr expr = RALParser.parse("HAS UNIT ConsultativeBoard");
        String result = owlRalMapper.map(expr, 0);
        Assert.assertEquals("organization:occupies some (organization:isMemberOf some ({organization-iaap:ConsultativeBoard}))", result);
    }

    @Test
    public void shouldMapRoles() {
        RALExpr expr = RALParser.parse("HAS ROLE Accountable");
        String result = owlRalMapper.map(expr, 0);
        Assert.assertEquals("organization:occupies some (organization:participatesIn some ({organization-iaap:Accountable}))", result);
    }


}
