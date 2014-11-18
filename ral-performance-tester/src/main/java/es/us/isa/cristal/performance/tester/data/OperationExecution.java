package es.us.isa.cristal.performance.tester.data;

import es.us.isa.cristal.performance.tester.meters.TimeMeter;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Manuel Leon
 *
 */
public class OperationExecution {

    private String operationName;
    private Map<String, Object> parameters;

	private List<OperationExecutionResult> executions;
    private TimeMeter meter;
	

    public OperationExecution(String operationName) {
        this.operationName = operationName;
        this.meter = new TimeMeter();
        executions = new ArrayList<OperationExecutionResult>();
        parameters = new HashMap<String, Object>();
    }

    public String getOperationName() {
        return operationName;
    }

    public Object run(OperationRun runner) {
        meter.start();
        Object result = runner.run();
        meter.stop();

        addExecution(result, meter.getResult());

        return result;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public final List<OperationExecutionResult> getExecutions() {
		return executions;
	}

	public final void addExecution(Object result, Long time, InfoPair... additionalInfo) {
        executions.add(new OperationExecutionResult(operationName, result, time, Arrays.asList(additionalInfo)));
    }

	public final Long getAverageExecutionTime(){
		Long avg = new Long(0);
		for(OperationExecutionResult result: executions){
			avg+=result.getExecutionTime();
		}
		avg=avg/(new Long(executions.size()));
		return avg;
	}
	

	
	
	
	
}
