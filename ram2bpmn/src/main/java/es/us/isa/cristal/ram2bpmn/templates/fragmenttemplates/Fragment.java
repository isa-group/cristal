package es.us.isa.cristal.ram2bpmn.templates.fragmenttemplates;

import es.us.isa.bpmn.xmlClasses.bpmn20.*;
import es.us.isa.cristal.ram2bpmn.templates.IdGenerator;

import javax.xml.bind.JAXBElement;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Fragment
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class Fragment {
    private Collection<TFlowElement> flowElements;
    private Collection<JAXBElement<? extends TFlowElement>> jaxbElements;
    private TSequenceFlow start;
    private TSequenceFlow end;
    private ObjectFactory objectFactory;

    private Fragment(List<JAXBElement<? extends TFlowElement>> jaxbElements, TSequenceFlow start, TSequenceFlow end) {
        this.jaxbElements = jaxbElements;
        this.flowElements = IdGenerator.makeUniqueIds(jaxbElements.stream().map(jaxbElement -> jaxbElement.getValue()).collect(Collectors.toSet()));

        this.start = start;
        this.end = end;

        this.objectFactory = new ObjectFactory();
    }

    public static Fragment fromProcess(TProcess process) {

        Collection<TFlowElement> elementSet = process.getFlowElement().stream().map(jaxbElement -> jaxbElement.getValue()).collect(Collectors.toSet());


        TSequenceFlow start = elementSet.stream()
                .filter(flowElement -> flowElement instanceof TSequenceFlow)
                .map(flowElement -> (TSequenceFlow) flowElement)
                .filter(elem -> elem.getSourceRef() == null)
                .findAny()
                .orElse(null);
        TSequenceFlow end = elementSet.stream()
                .filter(flowElement -> flowElement instanceof TSequenceFlow)
                .map(flowElement -> (TSequenceFlow) flowElement)
                .filter(elem -> elem.getTargetRef() == null)
                .findAny()
                .orElse(null);

        return new Fragment(process.getFlowElement(), start, end);
    }


    public Collection<TFlowElement> getFlowElements() {
        return flowElements;
    }

    public Collection<JAXBElement<? extends TFlowElement>> getJaxbElements() {
        return jaxbElements;
    }

    public TSequenceFlow getStart() {
        return start;
    }

    public TSequenceFlow getEnd() {
        return end;
    }

    private Stream<TSequenceFlow> sequenceFlows() {
        return this.flowElements.stream()
                .filter(flowElement -> flowElement instanceof TSequenceFlow)
                .map(flowElement -> (TSequenceFlow) flowElement);
    }

    private TSequenceFlow findWithSource(TFlowElement element) {
        return sequenceFlows()
                .filter(sequenceFlow -> sequenceFlow.getSourceRef().equals(element))
                .findAny()
                .orElse(null);
    }

    public Fragment insertSequential(Fragment f, TFlowElement element) {
        mergeFragmentElements(f);

        TSequenceFlow after = findWithSource(element);
        f.getEnd().setTargetRef(after.getTargetRef());
        after.setTargetRef(f.elementStart());
        removeFragmentElement(f.getStart());
        f.removeFragmentElement(f.getStart());
        f.addFragmentElement(objectFactory.createSequenceFlow(after));
        f.start = after;

        return f;
    }

    public Fragment insertSequential(Fragment f) {
        mergeFragmentElements(f);
        end.setTargetRef(f.elementStart());
        end = f.getEnd();
        removeFragmentElement(f.getStart());

        return this;
    }

    private Object elementStart() {
        return getStart().getTargetRef();
    }

    private Object elementEnd() {
        return getEnd().getSourceRef();
    }

    private void removeFragmentElement(TFlowElement flow) {
        jaxbElements.removeIf(jaxbElement -> jaxbElement.getValue().equals(flow));
        flowElements.remove(flow);
    }

    private boolean mergeFragmentElements(Fragment f) {
        jaxbElements.addAll(f.jaxbElements);
        return flowElements.addAll(f.getFlowElements());
    }

    private void addFragmentElement(JAXBElement<? extends TFlowElement> flow) {
        jaxbElements.add(flow);
        flowElements.add(flow.getValue());
    }

    private TSequenceFlow addSequenceFlow(TFlowElement source, TFlowElement target) {
        TSequenceFlow newAfter = new TSequenceFlow();
        newAfter.setId(IdGenerator.random());
        newAfter.setSourceRef(source);
        newAfter.setTargetRef(target);

        addFragmentElement(objectFactory.createSequenceFlow(newAfter));

        if (source == null) {
            start = newAfter;
        }

        if (target == null) {
            end = newAfter;
        }

        return newAfter;
    }

    private TSequenceFlow addSequenceFlow(TFlowElement source, TFlowElement target, String condition) {
        TSequenceFlow seq = addSequenceFlow(source, target);
        TFormalExpression formalExpression = new TFormalExpression();
        formalExpression.getContent().add(condition);
        seq.setConditionExpression(formalExpression);

        return seq;
    }

    public Fragment insertParallel(Fragment toInsert) {
        TParallelGateway openGateway;
        TParallelGateway closeGateway;

        mergeFragmentElements(toInsert);

        if (elementStart() instanceof TParallelGateway) {
            openGateway = (TParallelGateway) elementStart();
        } else {
            openGateway = addGateway(TParallelGateway::new, objectFactory::createParallelGateway, TGatewayDirection.DIVERGING);
            start.setSourceRef(openGateway);
            addSequenceFlow(null, openGateway);
        }

        if (elementEnd() instanceof TParallelGateway) {
            closeGateway = (TParallelGateway) elementEnd();
        } else {
            closeGateway = addGateway(TParallelGateway::new, objectFactory::createParallelGateway, TGatewayDirection.CONVERGING);
            end.setTargetRef(closeGateway);
            addSequenceFlow(closeGateway, null);
        }

        toInsert.getStart().setSourceRef(openGateway);
        toInsert.getEnd().setTargetRef(closeGateway);

        return this;
    }



    private <T extends TGateway> T addGateway(Supplier<T> builder, Function<T,JAXBElement<T>> jaxbBuilder, TGatewayDirection direction) {
        T gateway = builder.get();
        gateway.setId(IdGenerator.random());
        gateway.setGatewayDirection(direction);
        addFragmentElement(jaxbBuilder.apply(gateway));

        return gateway;
    }

    // It should have at least three elements (start, middle, end) so we do not change start and end,
    // but just wrap middle
    public Fragment embedInLoop(String loopCondition, String exitCondition) {
        TExclusiveGateway openGateway = addGateway(TExclusiveGateway::new, objectFactory::createExclusiveGateway, TGatewayDirection.CONVERGING);
        TExclusiveGateway closeGateway = addGateway(TExclusiveGateway::new, objectFactory::createExclusiveGateway,  TGatewayDirection.DIVERGING);

        start.setSourceRef(openGateway);
        end.setTargetRef(closeGateway);

        addSequenceFlow(null, openGateway);
        addSequenceFlow(closeGateway, null, exitCondition);
        addSequenceFlow(closeGateway, openGateway, loopCondition);

        return this;
    }

    public TSubProcess toSubprocess() {
        TSubProcess subprocess = new TSubProcess();
        subprocess.getFlowElement().addAll(getJaxbElements());

        TStartEvent startEvent = new TStartEvent();
        startEvent.setId(IdGenerator.random());
        TEndEvent end = new TEndEvent();
        end.setId(IdGenerator.random());
        subprocess.getFlowElement().add(objectFactory.createStartEvent(startEvent));
        subprocess.getFlowElement().add(objectFactory.createEndEvent(end));

        getStart().setSourceRef(startEvent);
        getEnd().setTargetRef(end);

        return subprocess;
    }

}
