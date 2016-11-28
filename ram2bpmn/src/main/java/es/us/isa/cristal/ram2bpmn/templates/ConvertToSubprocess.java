package es.us.isa.cristal.ram2bpmn.templates;

import es.us.isa.bpmn.xmlClasses.bpmn20.*;
import es.us.isa.cristal.ram2bpmn.templates.fragmenttemplates.Fragment;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ConvertToSubprocess
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class ConvertToSubprocess {
    private ObjectFactory objectFactory;
    private TSubProcess subprocess;

    private ConvertToSubprocess() {
        subprocess = new TSubProcess();
        objectFactory = new ObjectFactory();
    }

    public static ConvertToSubprocess fromProcess(TProcess process) {
        ConvertToSubprocess convertToSubprocess = new ConvertToSubprocess();
        convertToSubprocess.copyFromProcess(process);
        return convertToSubprocess;
    }

    public static ConvertToSubprocess fromFragment(Fragment fragment) {
        ConvertToSubprocess convertToSubprocess = new ConvertToSubprocess();
        convertToSubprocess.subprocess = fragment.toSubprocess();
        return convertToSubprocess;
    }


    private  Collection<TFlowElement> removeJAXB(List<JAXBElement<? extends TFlowElement>> jaxbElements) {
        return jaxbElements.stream()
                .map(JAXBElement::getValue)
                .collect(Collectors.toList());
    }


    public TSubProcess convert(TTask task) {
        copyFromTask(task);
        return subprocess;
    }

    private void copyFromProcess(TProcess process) {
        IdGenerator.makeUniqueIds(removeJAXB(process.getFlowElement()));

        subprocess.getFlowElement().addAll(process.getFlowElement());
        subprocess.getArtifact().addAll(process.getArtifact());
        subprocess.getLaneSet().addAll(process.getLaneSet());
    }

    private void copyFromTask(TTask task) {
        subprocess.setId(task.getId());
        subprocess.setName(task.getName());
        for (QName incoming: task.getIncoming()) {
            subprocess.getIncoming().add(incoming);
        }
        for (QName outgoing: task.getOutgoing()) {
            subprocess.getOutgoing().add(outgoing);
        }
        subprocess.getResourceRole().clear();
        subprocess.getResourceRole().addAll(
                task.getResourceRole().stream().map(
                        jaxbElement -> objectFactory.createResourceRole(jaxbElement.getValue())).collect(Collectors.toList())
        );

        subprocess.setExtensionElements(task.getExtensionElements());

        subprocess.getDataInputAssociation().clear();
        subprocess.getDataInputAssociation().addAll(task.getDataInputAssociation());
        subprocess.getDataOutputAssociation().clear();
        subprocess.getDataOutputAssociation().addAll(task.getDataOutputAssociation());

        subprocess.setStartQuantity(task.getStartQuantity());
        subprocess.setAuditing(task.getAuditing());
        subprocess.setCompletionQuantity(task.getCompletionQuantity());
        subprocess.setDefault(task.getDefault());
        subprocess.setIoSpecification(task.getIoSpecification());
    }



}
