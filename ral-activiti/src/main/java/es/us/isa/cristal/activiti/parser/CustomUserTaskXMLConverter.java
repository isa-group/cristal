package es.us.isa.cristal.activiti.parser;

import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.converter.UserTaskXMLConverter;
import org.activiti.bpmn.converter.XMLStreamReaderUtil;
import org.activiti.bpmn.converter.util.CommaSplitter;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.UserTask;
import org.apache.commons.lang3.StringUtils;

public class CustomUserTaskXMLConverter extends UserTaskXMLConverter {

	public void init() {
		//force to load BpmnXMLConverter.
		BpmnXMLConverter x = new BpmnXMLConverter();
		BpmnXMLConverter.addConverter(CustomUserTaskXMLConverter.getXMLType(), CustomUserTaskXMLConverter.getBpmnElementType(), CustomUserTaskXMLConverter.class);
	}

	
	public CustomUserTaskXMLConverter() {
	    super();
	    PotentialOwnerParser potentialOwnerParser = new CustomPotentialOwnerParser();
	    childElementParsers.put(potentialOwnerParser.getElementName(), potentialOwnerParser);
	  }
	
	public class CustomPotentialOwnerParser extends
			UserTaskXMLConverter.PotentialOwnerParser {
		
		@Override
		public void parseChildElement(XMLStreamReader xtr,
				BaseElement parentElement, BpmnModel model) throws Exception {
			String resourceElement = XMLStreamReaderUtil.moveDown(xtr);

			if (StringUtils.isNotEmpty(resourceElement)
					&& "resourceAssignmentExpression".equals(resourceElement)) {
				String expression = XMLStreamReaderUtil.moveDown(xtr);
				String language = xtr.getAttributeValue(null, "language");
				
				if (StringUtils.isNotEmpty(expression)
						&& "formalExpression".equals(expression)) {
					/* ADDED TO DEAL WITH RAL EXPRESSIONS */
					// check if the expression is a RAL expression.
					if (StringUtils.isNotEmpty(language)
							&& language.equalsIgnoreCase("RAL")) {
						String ral = xtr.getElementText();
						
						ral = ral.trim();
						((UserTask) parentElement).getCandidateUsers().add(
								"RAL(" + ral + ")");
					} else {
						/*
						 * there is no way to move up the cursor of the xtr
						 * (XMLStreamReader). It means we cannot go back to the
						 * previous element to do super.parseChildElement(...)
						 * It would be the best option to be sure that future
						 * versions of this converter will work properly. 
						 * TODO: investigate if using a deep clone of the xtr can
						 * solve this problem and it is not a performance  or memory issue
						 */
						List<String> assignmentList = CommaSplitter
								.splitCommas(xtr.getElementText());

						for (String assignmentValue : assignmentList) {
							if (assignmentValue == null)
								continue;
							assignmentValue = assignmentValue.trim();
							if (assignmentValue.length() == 0)
								continue;

							String userPrefix = "user(";
							String groupPrefix = "group(";
							if (assignmentValue.startsWith(userPrefix)) {
								assignmentValue = assignmentValue.substring(
										userPrefix.length(),
										assignmentValue.length() - 1).trim();
								((UserTask) parentElement).getCandidateUsers()
										.add(assignmentValue);
							} else if (assignmentValue.startsWith(groupPrefix)) {
								assignmentValue = assignmentValue.substring(
										groupPrefix.length(),
										assignmentValue.length() - 1).trim();
								((UserTask) parentElement).getCandidateGroups()
										.add(assignmentValue);
							} else {
								((UserTask) parentElement).getCandidateGroups()
										.add(assignmentValue);
							}
						}
					}
				}
			}
		}

	}

}
