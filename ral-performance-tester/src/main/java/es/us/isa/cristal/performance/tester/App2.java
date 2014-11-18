package es.us.isa.cristal.performance.tester;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import es.us.isa.cristal.performance.tester.exporters.JSONExporter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * App2
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class App2 {
    public static void main(String [] args) throws FileNotFoundException {
        App2 app = new App2();
        app.loadJson();

    }

    public void loadJson() throws FileNotFoundException {
        Gson gson = gson = new GsonBuilder().setPrettyPrinting().create();
        Map<Double, Map<String, Data>> result = new HashMap<>();

        JSONExporter.PerformanceTestInfo info = gson.fromJson(new JsonReader(new FileReader("/Users/resinas/Google Drive/code/cristal/ral-performance-tester/pplongAC-20141028-233902.json")), JSONExporter.PerformanceTestInfo.class);

        List<JSONExporter.ExecutionInfo> exInfoList = info.getExecutions();
        for (JSONExporter.ExecutionInfo exInfo : exInfoList) {
            Map<String, Object> parameters = exInfo.getAdditionalInfo();

            String assignment = (String) parameters.get("Assignment");
            double size = (double) parameters.get("Size");

            Map<String, Data> assignments = result.get(size);
            if (assignments == null) {
                assignments = new HashMap<>();
                result.put(size, assignments);
            }

            Data d = assignments.get(assignment);
            if (d == null) {
                d = new Data();
                assignments.put(assignment, d);
            }

            d.add(exInfo.getAvg());
        }

        StringWriter out = new StringWriter();
        JsonWriter writer = new JsonWriter(out);
        writer.setIndent("  ");

        gson.toJson(result, HashMap.class, writer);

        System.out.println(out.toString());

    }

    public static class Data {
        private long max = Long.MIN_VALUE;
        private long min = Long.MAX_VALUE;
        private long sum = 0;
        private long count = 0;
        private long avg = 0;

        public Data() {
        }

        public void add(long value) {
            if (max < value) {
                max = value;
            }

            if (min > value) {
                min = value;
            }

            sum += value;
            count += 1;

            avg = sum / count;
        }

        public long getMax() {
            return max;
        }

        public long getMin() {
            return min;
        }

        public long getAvg() {
            return sum/count;
        }
    }
}
