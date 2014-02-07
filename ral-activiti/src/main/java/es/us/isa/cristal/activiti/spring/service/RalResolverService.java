package es.us.isa.cristal.activiti.spring.service;

/**
 * 
 * @author Manuel Leon
 *
 */
public interface RalResolverService {
	
	String resolveRalExpression(String processId, String expression);

}
