package es.us.isa.cristal.performance.tester;

import es.us.isa.cristal.organization.model.gson.OrganizationalModel;
import es.us.isa.cristal.performance.tester.data.*;
import es.us.isa.cristal.performance.tester.exporters.*;
import es.us.isa.cristal.performance.tester.meters.TimeMeter;
import scala.actors.threadpool.Arrays;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * AbstractPerformanceTest
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public abstract class AbstractPerformanceTest {
    protected TimeMeter meter;
    protected void useTimeMeter() {
        this.meter = new TimeMeter();
    }

    private int numOfIterations = 1;
    protected void iterations(int numOfIterations) {
        this.numOfIterations = numOfIterations;
    }

    private Map<String, String> parameters = new HashMap<>();
    protected void addParameter(String key, String value) {
        parameters.put(key, value);
    }

    private List<OperationExecutionAggregator> executionResults = new ArrayList<>();
    public List<OperationExecutionAggregator> getExecutionResults() {
        return executionResults;
    }

    private List<InfoPair> listInfo;
    protected void initInfo(List<InfoPair> init) {
        listInfo = new ArrayList<InfoPair>(init);
    }
    protected void addInfo(String key, Object value) {
        listInfo.add(p(key, value));
    }
    protected InfoPair p(String key, Object value) {
        return new InfoPair(key, value);
    }

    private OperationExecutionResult internalRun(String operationName, OperationRun operationRun, InfoPair... additionalInfo) {
        initInfo(java.util.Arrays.asList(additionalInfo));
        meter.start();
        operationRun.setup();
        meter.stop();
        addInfo("Setup time: ", meter.getResult());

        meter.start();
        Object result = operationRun.run();
        meter.stop();

        OperationExecutionResult executionResult = new OperationExecutionResult(operationName, result, meter.getResult(), listInfo);

        return executionResult;
    }

    private OperationExecutionAggregator multiRun(int iterations, String operationName, OperationRun operationRun, InfoPair... additionalInfo) {
        OperationExecutionAggregator aggregator = new OperationExecutionAggregator();
        for (int i = 0; i < iterations; i++) {
            aggregator.add(internalRun(operationName, operationRun, additionalInfo));
        }

        executionResults.add(aggregator);

        return aggregator;
    }

    protected OperationExecutionAggregator run(String operationName, OperationRun operationRun, InfoPair... additionalInfo) {
        return multiRun(numOfIterations, operationName, operationRun, additionalInfo);
    }

    protected OperationExecutionAggregator runSingle(String operationName, OperationRun operationRun, InfoPair... additionalInfo) {
        return multiRun(1, operationName, operationRun, additionalInfo);
    }

    protected Map<String, String> buildParameters() {
        parameters.put("iterations", Long.toString(numOfIterations));

        return parameters;
    }

    private void export(OutputStream output, Exporter exporter) throws IOException {
        System.out.println("Exporting results...");
        exporter.export(output, getExecutionResults(), buildParameters());
        System.out.println("Results exported...");
    }

    protected void exportHtml(OutputStream output) throws IOException {
        export(output, new HTMLExporter());
        output.close();
    }

    protected void exportTxt(OutputStream output) throws IOException {
        export(output, new TxtExporter());
        output.close();
    }

    protected void exportJson(OutputStream output) throws IOException {
        export(output, new JSONExporter());
        output.close();
    }

    protected OutputStream file(String filename) throws FileNotFoundException {
        return new FileOutputStream(filename);
    }

    protected OutputStream console() {
        return System.out;
    }

    protected String mapToString(Map<String, Long> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            sb.append("{").append(entry.getKey()).append(", ").append(entry.getValue()).append("} ");
        }
        sb.append("]");
        return sb.toString();
    }

    protected String nowString() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        return df.format(Calendar.getInstance().getTime());
    }



}
