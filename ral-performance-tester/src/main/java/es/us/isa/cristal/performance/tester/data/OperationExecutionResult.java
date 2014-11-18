package es.us.isa.cristal.performance.tester.data;

import java.util.*;

/**
 * 
 * @author Manuel Leon
 *
 */
public class OperationExecutionResult {

	protected Long executionTime;
	protected Object result;
    protected Map<String, Object> additionalInfo;
    protected String operationName;

    public OperationExecutionResult() {
        super();
    }


	public OperationExecutionResult(String operationName, Object result, Long executionTime, List<InfoPair> additionalInfo) {
		super();
        this.operationName = operationName;
        this.result = result;
        this.executionTime = executionTime;
        this.additionalInfo = InfoPair.toMap(additionalInfo);
    }

    public String getOperationName() {
        return operationName;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public Object getResult() {
        return result;
    }

    public Map<String,Object> getAdditionalInfo() {
        return additionalInfo;
    }
}
