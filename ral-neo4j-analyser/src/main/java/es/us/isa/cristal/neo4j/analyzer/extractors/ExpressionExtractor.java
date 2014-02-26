package es.us.isa.cristal.neo4j.analyzer.extractors;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;

/**
 * 
 * @author Manuel Leon
 *
 */
public interface ExpressionExtractor {
	
	RALExpr extractExpression(BPEngine engine, Object processId, String activityName);
	
}
