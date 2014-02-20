package es.us.isa.cristal.organization.model.util;

public class CypherUtil {
	
	public static String getId(String name){
		return name.replace(" ","_").toLowerCase();
	}
	
	public static String cypherCreateEdgeQuery(String from, String to, String type){
		return "CREATE " + CypherUtil.getId(from) + "-" + type + "->" + CypherUtil.getId(to);
	}
	
}
