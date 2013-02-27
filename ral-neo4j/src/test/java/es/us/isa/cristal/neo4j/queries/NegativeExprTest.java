package es.us.isa.cristal.neo4j.queries;

import org.junit.Test;
import org.neo4j.cypher.ExecutionResult;

/**
 * User: resinas
 * Date: 27/02/13
 * Time: 10:35
 */
public class NegativeExprTest extends AbstractBaseTest {

    @Test
    public void shouldQueryNegative() {
            // when
/*
        ExecutionResult result = engine.execute( "START jim = node:node_auto_index(name='Jim'), southwark = node:node_auto_index(borough='Southwark'), indian = node:node_auto_index(cuisine='Indian') " +
                "MATCH jim-[:FRIEND]->(friend)-[:LIKES]->restaurant-[:IN]->()-[:IN]->southwark, restaurant-[:CUISINE]->indian " +
                "WHERE friend-[:FRIEND]->jim " +
                "RETURN restaurant" );
*/
        ExecutionResult result = engine.execute("START p=node:node_auto_index('name:*'),r=node:node_auto_index(role='Account Administrator') WHERE NOT p-[:POSITION]->()-[:ROLE]->r RETURN p");
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
}
