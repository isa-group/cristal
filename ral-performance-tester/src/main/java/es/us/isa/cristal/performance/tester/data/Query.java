package es.us.isa.cristal.performance.tester.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Manuel Leon
 *
 */
public class Query {
	
	private String activity;
	
	private String query;
	
	private List<QueryExecutionResult> executions;
	
	

	public final String getActivity() {
		return activity;
	}

	public final void setActivity(String activity) {
		this.activity = activity;
	}

	public final String getQuery() {
		return query;
	}

	public final void setQuery(String query) {
		this.query = query;
	}

	

	

	public final List<QueryExecutionResult> getExecutions() {
		return executions;
	}

	public final void addExecution(String finalExpr, String result, Long time) {
		if(this.executions == null){
			executions = new ArrayList<QueryExecutionResult>();
		}
		executions.add(new QueryExecutionResult(finalExpr, result, time));
	}

	public final Long getAverageExecutionTime(){
		Long avg = new Long(0);
		for(QueryExecutionResult result: executions){
			avg+=result.getExecutionTime();
		}
		avg=avg/(new Long(executions.size()));
		return avg;
	}
	

	
	
	
	
}
