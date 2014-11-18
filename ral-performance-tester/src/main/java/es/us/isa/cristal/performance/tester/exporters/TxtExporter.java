package es.us.isa.cristal.performance.tester.exporters;

import es.us.isa.cristal.organization.model.gson.OrganizationalModel;
import es.us.isa.cristal.performance.tester.data.ExperimentConfiguration;
import es.us.isa.cristal.performance.tester.data.OperationExecutionAggregator;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * TxtExporter
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class TxtExporter implements Exporter {
    @Override
    public void export(String filename, ExperimentConfiguration edata, OrganizationalModel model) {

    }

    @Override
    public void export(OutputStream outputStream, List<OperationExecutionAggregator> executions, Map<String, String> parameters) throws IOException {
        InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("txt.stg"));
        StringTemplateGroup stg = new StringTemplateGroup(reader, DefaultTemplateLexer.class);
        StringTemplate st = stg.getInstanceOf("export");
        st.setAttribute("parameters", parameters.entrySet());
        st.setAttribute("executions", executions);

        IOUtils.write(st.toString(), outputStream);

    }

    @Override
    public String getExtensionIdentifier() {
        return "txt";
    }
}
