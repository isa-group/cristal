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
public class ReportExprTest extends AbstractBaseTest {

    @Test
    public void shouldQueryReportsToPosition() {
        RALExpr expr = RALParser.parse("REPORTS TO POSITION THEOS Project Coordinator");
        ExecutionResult executionResult = doQuery(expr);
        Set<String> result = getSetFromResult(executionResult);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Anthony", "Betty", "Anna", "Daniel", "Christine", "Adele", "Charles")), result);
    }

    @Test
    public void shouldQueryReportsToPositionDirectly() {
        RALExpr expr = RALParser.parse("REPORTS TO POSITION THEOS Project Coordinator DIRECTLY");
        ExecutionResult executionResult = doQuery(expr);
        Set<String> result = getSetFromResult(executionResult);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Anthony", "Betty", "Anna", "Daniel", "Charles")), result);
    }

    @Test
    public void shouldQueryIsReportedByPosition() {
        RALExpr expr = RALParser.parse("IS REPORTED BY POSITION THEOS PhD Student");
        ExecutionResult executionResult = doQuery(expr);
        Set<String> result = getSetFromResult(executionResult);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Charles", "Anthony" )), result);
    }
}
