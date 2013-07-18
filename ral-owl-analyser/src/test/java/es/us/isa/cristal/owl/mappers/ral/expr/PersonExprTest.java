package es.us.isa.cristal.owl.mappers.ral.expr;

import es.us.isa.cristal.BPEngineMock;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.OntologyNamespaces;
import es.us.isa.cristal.owl.mappers.ral.InstanceOwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlRalMapper;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;
import es.us.isa.cristal.parser.RALParser;
import org.junit.Assert;
import org.junit.Test;

import static es.us.isa.cristal.owl.HiringResolutionScenario.BP_HIRING_RESOLUTION_IRI;
import static es.us.isa.cristal.owl.HiringResolutionScenario.ORGANIZATION_IAAP_IRI;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 18:26
 */
public class PersonExprTest {
    @Test
    public void shouldMapToTheRightString() {
        OntologyNamespaces namespaces = new OntologyNamespaces();
        namespaces.setPerson("organization-iaap", ORGANIZATION_IAAP_IRI.toString());
        namespaces.setGroup("organization-iaap", ORGANIZATION_IAAP_IRI.toString());
        namespaces.setActivity("bp-hiring-resolution", BP_HIRING_RESOLUTION_IRI.toString());

        RALExpr expr = RALParser.parse("IS Anna");
        OwlRalMapper owlRalMapper = new InstanceOwlRalMapper(new IdMapper(namespaces), new BPEngineMock());

        String result = owlRalMapper.map(expr, 0);

        Assert.assertEquals("{organization-iaap:Anna}", result);
    }
}
