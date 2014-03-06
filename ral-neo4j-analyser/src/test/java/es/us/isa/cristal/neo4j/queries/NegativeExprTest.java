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
public class NegativeExprTest extends AbstractBaseTest {

    @Test
    public void shouldQueryNegativePersons() {
        RALExpr expr = RALParser.parse("NOT IS Charles");
        ExecutionResult executionResult = doQuery(expr);
        Set<String> result = getSetFromResult(executionResult);

        Assert.assertEquals(6, result.size());
        Assert.assertEquals(new HashSet<String>(Arrays.asList("Anthony", "Christine", "Adele", "Anna", "Daniel", "Betty")), result);
    }

    @Test
    public void shouldQueryNegativeRoles() {
        RALExpr expr = RALParser.parse("NOT HAS ROLE Account Administrator");
        ExecutionResult executionResult = doQuery(expr);
        Set<String> result = getSetFromResult(executionResult);

        Assert.assertEquals(5, result.size());
        Assert.assertEquals(new HashSet<String>(Arrays.asList("Charles", "Christine", "Adele", "Anna", "Daniel")), result);
    }
}
