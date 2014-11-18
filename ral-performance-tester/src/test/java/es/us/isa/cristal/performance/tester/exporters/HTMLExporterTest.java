package es.us.isa.cristal.performance.tester.exporters;

import es.us.isa.cristal.organization.model.gson.OrganizationalModel;
import es.us.isa.cristal.performance.tester.data.InfoPair;
import es.us.isa.cristal.performance.tester.data.OperationExecutionResult;
import es.us.isa.cristal.performance.tester.data.OperationExecutionAggregator;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTMLExporterTest
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class HTMLExporterTest {
    @Test
    public void testExport() throws IOException {
        HTMLExporter exporter = new HTMLExporter();
        OperationExecutionAggregator aggregator = new OperationExecutionAggregator()
                .add(new OperationExecutionResult("op1", "result", 12L, Arrays.asList(new InfoPair("key1", "info1"), new InfoPair("key2", "info2"))));

        Map<String, String> parameters = new HashMap<>();
        parameters.put("hello", "world");

        exporter.export(System.out, Arrays.asList(aggregator), parameters);
    }
}
