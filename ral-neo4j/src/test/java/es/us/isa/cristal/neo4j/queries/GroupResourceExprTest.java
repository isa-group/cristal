package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.parser.RALParser;
import org.junit.Assert;
import org.junit.Test;
import org.neo4j.cypher.ExecutionResult;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * User: resinas
 * Date: 27/02/13
 * Time: 08:38
 */
public class GroupResourceExprTest extends AbstractBaseTest {

    @Test
    public void shouldQueryPositions() {
        RALExpr expr = RALParser.parse("HAS POSITION THEOS Responsible for Work Package");
        ExecutionResult result = doQuery(expr);

        Set<String> l = getSetFromResult(result);

        Assert.assertEquals(2, l.size());
        Assert.assertEquals(new HashSet<String>(Arrays.asList("Charles", "Anthony")), l);
    }

    @Test
    public void shouldQueryRoles() {
        RALExpr expr = RALParser.parse("HAS ROLE Account Administrator");
        ExecutionResult result = doQuery(expr);

        Set<String> l = getSetFromResult(result);

        Assert.assertEquals(2, l.size());
        Assert.assertEquals(new HashSet<String>(Arrays.asList("Anthony", "Betty")), l);
    }

    @Test
    public void shouldQueryUnits() {
        RALExpr expr = RALParser.parse("HAS UNIT Theos");
        ExecutionResult executionResult = doQuery(expr);
        Set<String> result = getSetFromResult(executionResult);
        Assert.assertEquals(7, result.size());
        Assert.assertEquals(new HashSet<String>(Arrays.asList("Anthony", "Betty", "Charles", "Christine", "Adele", "Anna", "Daniel")), result);

    }

}
