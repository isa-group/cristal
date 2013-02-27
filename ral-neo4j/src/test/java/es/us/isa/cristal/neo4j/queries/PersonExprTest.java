package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.neo4j.Neo4jQueryBuilder;
import es.us.isa.cristal.parser.RALParser;
import es.us.isa.cristal.resolver.BPEngineMock;
import es.us.isa.cristal.resolver.ConstraintResolver;
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
    public void findIndianRestaurantsInSouthwarkThatMyFriendsLike() throws Exception
    {
        // when
/*
        ExecutionResult result = engine.execute( "START jim = node:node_auto_index(name='Jim'), southwark = node:node_auto_index(borough='Southwark'), indian = node:node_auto_index(cuisine='Indian') " +
                "MATCH jim-[:FRIEND]->(friend)-[:LIKES]->restaurant-[:IN]->()-[:IN]->southwark, restaurant-[:CUISINE]->indian " +
                "WHERE friend-[:FRIEND]->jim " +
                "RETURN restaurant" );
*/
        RALExpr expr = RALParser.parse("IS PERSON IN DATA FIELD hola.mundo");
        Neo4jQueryBuilder builder = new Neo4jQueryBuilder();
        ConstraintResolver res = new ConstraintResolver(new BPEngineMock(), 0L);
        String query = builder.build(expr, res);
        System.out.println(query);
        ExecutionResult result = engine.execute(query);
//      ExecutionResult result = engine.execute("START jim = node:node_auto_index(name='Jim') MATCH jim-[:FRIEND]->friend RETURN friend.name");

        // then
//        Iterator<Node> it = result.javaColumnAs("friend");
//        while (it.hasNext()) {
//            Node n = it.next();
//            System.out.println(n.getProperty("name"));
//
//        }

        System.out.println(result.dumpToString());
    }

    @Test
    public void shouldBuildPersonExprByName() {
        RALExpr expr = RALParser.parse("IS Anthony");
        Neo4jQueryBuilder builder = new Neo4jQueryBuilder();
        ConstraintResolver res = new ConstraintResolver(new BPEngineMock(), 0L);
        String query = builder.build(expr, res);
        ExecutionResult result = engine.execute(query);

        Assert.assertEquals(1, result.size());

        for(Object p: IteratorUtil.asIterable(result.javaColumnAs("person.name"))) {
            Assert.assertEquals("Anthony", p);
        }

    }

    @Test
    public void shouldBuildPersonExprByData() {
        RALExpr expr = RALParser.parse("IS PERSON IN DATA FIELD hola.mundo");
        Neo4jQueryBuilder builder = new Neo4jQueryBuilder();
        ConstraintResolver res = new ConstraintResolver(new BPEngineMock(), 0L);
        String query = builder.build(expr, res);
        ExecutionResult result = engine.execute(query);

        Assert.assertEquals(1, result.size());
        for(Object p: IteratorUtil.asIterable(result.javaColumnAs("person.name"))) {
            Assert.assertEquals("Anna", p);
        }
    }
}
