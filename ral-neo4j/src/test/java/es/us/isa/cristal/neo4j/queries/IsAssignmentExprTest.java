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
public class IsAssignmentExprTest extends AbstractBaseTest {

    @Test
    public void shouldQueryActivityAssignment() {
        RALExpr expr = RALParser.parse("IS ASSIGNMENT IN ACTIVITY Submit Account");
        ExecutionResult result = doQuery(expr);

        Set<String> l = getSetFromResult(result);

        Assert.assertEquals(new HashSet<String>(Arrays.asList("Anthony", "Betty")), l);
    }
}
