package es.us.isa.cristal.performance.tester.data;

import es.us.isa.cristal.performance.tester.data.OperationExecutionResult;
import es.us.isa.cristal.performance.tester.data.QueryExecutionResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * OperationExecutionAggregator
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class OperationExecutionAggregator extends OperationExecutionResult {
    private List<OperationExecutionResult> results;
    private Comparator<OperationExecutionResult> resultComparator = new Comparator<OperationExecutionResult>() {
        @Override
        public int compare(OperationExecutionResult o1, OperationExecutionResult o2) {
            return Long.compare(o1.getExecutionTime(), o2.getExecutionTime());
        }
    };

    public OperationExecutionAggregator() {
        super();
        this.results = new ArrayList<OperationExecutionResult>();
    }

    public final OperationExecutionAggregator add(OperationExecutionResult operationExecutionResult) {
        results.add(operationExecutionResult);
        updateParameters(operationExecutionResult);
        return this;
    }

    private void updateParameters(OperationExecutionResult operationExecutionResult) {
        operationName = operationExecutionResult.getOperationName();
        result = operationExecutionResult.getResult();
        additionalInfo = operationExecutionResult.getAdditionalInfo();
        executionTime = getAverageExecutionTime();
    }

    public List<OperationExecutionResult> getResults() {
        return results;
    }

    public final Long getAverageExecutionTime(){
        Long avg = 0L;
        long size = (long) results.size();

        for(OperationExecutionResult result: results){
            avg+=result.getExecutionTime();
        }

        if (results.size() >= 5) {
            avg -= getMax();
            avg -= getMin();
            size = (size - 2);
        }

        avg=avg / size;

        return avg;
    }

    public final Long getMax() {
        return Collections.max(results, resultComparator).getExecutionTime();
    }

    public final Long getMin() {
        return Collections.min(results, resultComparator).getExecutionTime();
    }

}
