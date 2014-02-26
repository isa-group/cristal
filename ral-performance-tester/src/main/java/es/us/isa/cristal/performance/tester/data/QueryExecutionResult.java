package es.us.isa.cristal.performance.tester.data;

/**
 * 
 * @author Manuel Leon
 *
 */
public class QueryExecutionResult {
	
	private String executedExpression;
	
	private Long executionTime;
	
	private String queryResult;

	
	
	public QueryExecutionResult(String executedExpression, 
			String queryResult, Long executionTime) {
		super();
		this.executedExpression = executedExpression;
		this.executionTime = executionTime;
		this.queryResult = queryResult;
	}

	public final String getExecutedExpression() {
		return executedExpression;
	}

	public final void setExecutedExpression(String executedExpression) {
		this.executedExpression = executedExpression;
	}

	public final Long getExecutionTime() {
		return executionTime;
	}

	public final void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}

	public final String getQueryResult() {
		return queryResult;
	}

	public final void setQueryResult(String queryResult) {
		this.queryResult = queryResult;
	}
	
	
	
	
}
