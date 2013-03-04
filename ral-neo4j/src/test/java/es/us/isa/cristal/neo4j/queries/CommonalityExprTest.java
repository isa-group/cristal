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
 * Time: 10:35
 */
public class CommonalityExprTest extends AbstractBaseTest {

    @Test
    public void shouldQueryShareSomeRole() {
        RALExpr expr = RALParser.parse("SHARES SOME ROLE WITH Charles");
        ExecutionResult executionResult = doQuery(expr);
        Set<String> result = getSetFromResult(executionResult);

        Assert.assertEquals(4, result.size());
        Assert.assertEquals(new HashSet<String>(Arrays.asList("Anthony", "Charles", "Christine", "Adele")), result);
    }

    @Test
    public void shouldQueryShareAllRole() {
        RALExpr expr = RALParser.parse("SHARES ALL ROLE WITH Adele");
        ExecutionResult executionResult = doQuery(expr);
        Set<String> result = getSetFromResult(executionResult);

        Assert.assertEquals(2, result.size());
        Assert.assertEquals(new HashSet<String>(Arrays.asList("Adele", "Christine")), result);
    }
}
