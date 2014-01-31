package es.us.isa.cristal.neo4j.analyzer;

import org.junit.Assert;
import org.junit.Test;
import org.neo4j.cypher.ExecutionResult;


/**
 * Unit test for simple App.
 */
public class CypherQueryTest extends AbstractBaseTest{

	    @Test
	    public void shouldQueryPersonByName() {
	    	
	    	String query = "START person=node:node_auto_index('name:*') RETURN DISTINCT person.name";
	    	
	    	 ExecutionResult result = engine.execute(query);
	    	
	        Assert.assertEquals(7, result.size());

	        

	    }

	    
	}
