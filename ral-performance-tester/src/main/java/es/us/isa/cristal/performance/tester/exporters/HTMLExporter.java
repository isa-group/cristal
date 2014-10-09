package es.us.isa.cristal.performance.tester.exporters;


import es.us.isa.cristal.organization.model.gson.OrganizationalModel;
import es.us.isa.cristal.performance.tester.data.ExecutionData;
import es.us.isa.cristal.performance.tester.data.Query;
import es.us.isa.cristal.performance.tester.data.QueryExecutionResult;
import es.us.isa.cristal.performance.tester.util.FileWriterUtil;

/**
 * 
 * @author Manuel Leon
 *
 */
public class HTMLExporter implements Exporter{

	@Override
	public void export(String filename, ExecutionData edata, OrganizationalModel model) {
		
		
		String html = "<!doctype html> \n" +
		"<html lang=\"en\"> \n" +
		"   <head> \n" + 
		"      <meta charset=\"utf-8\"> \n" + 
		"      <title>Execution Results</title> \n" + 
		"      <link rel=\"stylesheet\" href=\"http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css\"> \n" + 
		"      <script src=\"http://code.jquery.com/jquery-1.9.1.js\"></script> \n" + 
		"      <script src=\"http://code.jquery.com/ui/1.10.3/jquery-ui.js\"></script> \n" +
		" 	   <style> h3{font-size:12px !important;} p{font-size:10px;} </style> " +  
		"      <script> \n" +  
		"         $(function() { \n" + 
		"            $( \"#accordion\" ).accordion({ collapsible: true }); \n" +
		"         }); \n" +
		"      </script> \n" +
		"   </head> \n" +
		"   <body> \n" +
		"      <div id=\"accordion\"> \n" + 
		"          <h3>CREATE SCRIPT</h3> \n" + 
		"          <div><pre>\n" +
		"" + model.getCypherCreateQuery() + " \n" +
		"          </pre></div> \n";   
		for(Query q: edata.getQueryList()){
			html+="          <h3>(AVG-TIME: " + q.getAverageExecutionTime() + ") " + q.getActivity() + ": " + q.getQuery() + "</h3> \n" + 
			"          <div>\n";
			for(QueryExecutionResult ex: q.getExecutions()){
				html+="<p><span style=\"color:blue\">TIME: " + ex.getExecutionTime() + " # EXPR: " + ex.getExecutedExpression() + " # RESULT: </span>" + ex.getQueryResult() + "</p> \n";
			}
			html+="          </div> \n";   
		}
		
		
		html+="      </div> \n" + 
		"   </body> \n" +
		"</html>";
		
		FileWriterUtil.writeStringToFile(filename, html);
		
	}

	@Override
	public String getExtensionIdentifier() {
		return ".html";
	}

	
	
}
