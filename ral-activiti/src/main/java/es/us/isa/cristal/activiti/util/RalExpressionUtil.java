package es.us.isa.cristal.activiti.util;

/**
 * 
 * @author Manuel Leon
 *
 */
public class RalExpressionUtil {
	
	public static final String RAL_PREFIX="RAL(";
	
	public static final String RAL_SUFIX =")";
	
	public static boolean isRALExpression(String expression){
		expression = expression.trim();
		if(expression.startsWith(RalExpressionUtil.RAL_PREFIX) && expression.endsWith(RalExpressionUtil.RAL_SUFIX)){
			return true;
		}else{
			return false;
		}
	}
	
	public static String extractRalExpression(String name) {
		name=name.trim();
		String ralExp = name.substring(RalExpressionUtil.RAL_PREFIX.length(), name.length() - RalExpressionUtil.RAL_SUFIX.length()).trim();
		return ralExp;
	}
	
}
