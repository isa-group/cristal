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
public class DelegateExprTest extends AbstractBaseTest {

    @Test
    public void shouldQueryDelegateToPosition() {
        RALExpr expr = RALParser.parse("CAN DELEGATE WORK TO POSITION THEOS PhD Student");
        ExecutionResult executionResult = doQuery(expr);
        Set<String> result = getSetFromResult(executionResult);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Anthony", "Charles")), result);
    }

    @Test
    public void shouldQueryHaveDelegateByPosition() {
        RALExpr expr = RALParser.parse("CAN HAVE WORK DELEGATED BY POSITION THEOS Project Coordinator");
        ExecutionResult executionResult = doQuery(expr);
        Set<String> result = getSetFromResult(executionResult);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Charles", "Betty", "Anna", "Daniel", "Anthony" )), result);
    }
}
