package es.us.isa.cristal.performance.tester.exporters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import es.us.isa.cristal.organization.model.gson.OrganizationalModel;
import es.us.isa.cristal.performance.tester.data.ExperimentConfiguration;
import es.us.isa.cristal.performance.tester.data.OperationExecutionAggregator;
import es.us.isa.cristal.performance.tester.data.OperationExecutionResult;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JSONExporter
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class JSONExporter implements Exporter {
    private Gson gson;

    public JSONExporter() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void export(String filename, ExperimentConfiguration edata, OrganizationalModel model) {
    }

    @Override
    public void export(OutputStream outputStream, List<OperationExecutionAggregator> executions, Map<String, String> parameters) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        writer.setIndent("  ");

        PerformanceTestInfo info = new PerformanceTestInfo(parameters, executions);

        gson.toJson(info, PerformanceTestInfo.class, writer);

        writer.close();
    }

    @Override
    public String getExtensionIdentifier() {
        return "json";
    }

    public static class PerformanceTestInfo {
        private Map<String, String> parameters;
        private List<ExecutionInfo> executions;

        private PerformanceTestInfo(Map<String, String> parameters, List<OperationExecutionAggregator> executions) {
            this.parameters = parameters;
            this.executions = new ArrayList<>(executions.size());
            for (OperationExecutionAggregator ex : executions) {
                this.executions.add(new ExecutionInfo(ex));
            }
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public List<ExecutionInfo> getExecutions() {
            return executions;
        }
    }

    public static class ExecutionInfo {

        private String operationName;
        private long avg;
        private long max;
        private long min;
        private Object result;
        private Map<String, Object> additionalInfo;
        private List<OperationExecutionResult> results;

        public ExecutionInfo(OperationExecutionAggregator aggregator) {
            this.operationName = aggregator.getOperationName();
            this.result = aggregator.getResult();
            this.avg = aggregator.getAverageExecutionTime();
            this.max = aggregator.getMax();
            this.min = aggregator.getMin();
            this.additionalInfo = aggregator.getAdditionalInfo();
            this.results = aggregator.getResults();
        }

        public String getOperationName() {
            return operationName;
        }

        public long getAvg() {
            return avg;
        }

        public long getMax() {
            return max;
        }

        public long getMin() {
            return min;
        }

        public Object getResult() {
            return result;
        }

        public Map<String, Object> getAdditionalInfo() {
            return additionalInfo;
        }

        public List<OperationExecutionResult> getResults() {
            return results;
        }
    }
}
