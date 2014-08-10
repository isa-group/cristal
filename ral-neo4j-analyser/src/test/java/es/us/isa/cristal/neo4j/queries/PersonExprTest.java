package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.parser.RALParser;
import org.junit.Assert;
import org.junit.Test;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.helpers.collection.IteratorUtil;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 15:52
 */
public class PersonExprTest extends AbstractBaseTest{

    @Test
    public void shouldQueryPersonByName() {
        RALExpr expr = RALParser.parse("IS Anthony");
        ExecutionResult result = doQuery(expr);

        Assert.assertEquals(1, result.size());

        for(Object p: IteratorUtil.asIterable(result.javaColumnAs("person.name"))) {
            Assert.assertEquals("Anthony", p);
        }

    }

    @Test
    public void shouldQueryPersonByData() {
        RALExpr expr = RALParser.parse("IS PERSON IN DATA FIELD hola.mundo");
        ExecutionResult result = doQuery(expr);

        Assert.assertEquals(1, result.size());
        for(Object p: IteratorUtil.asIterable(result.javaColumnAs("person.name"))) {
            Assert.assertEquals("Anna", p);
        }
    }
}
