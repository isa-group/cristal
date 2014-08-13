package es.us.isa.cristal.organization.model.util;

public class CypherUtil {
	
	public static String getId(String name){
        String id = name.replaceAll("\\W", "").toLowerCase();
        return id;
	}
	
	public static String cypherCreateEdgeQuery(String from, String to, String type){
		return "CREATE " + CypherUtil.getId(from) + "-" + type + "->" + CypherUtil.getId(to);
	}
	
}
