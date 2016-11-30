# RAM2BPMN

RAM2BPMN is a framework that enables current BPMSs to execute BPMN processes in which people with different responsibilities collaborate to complete process activities. The core idea is to take a BPMN model without resource-related information and a [Responsibility Assignment Matrix](https://en.wikipedia.org/wiki/Responsibility_assignment_matrix) as inputs and to automatically generate a new BPMN model in which the only responsibility defined for each activity is Responsible, but which includes new activities to model the semantics conveyed by the other responsibilities included in the matrix.

More specifically, RAM2BPMN turns every activity for which some type of responsibility different than Responsible is defined into a subprocess. We refer to the subprocesses created during the transformation as RAM subprocesses. A RAM subprocess is a regular BPMN subprocess that includes the specific tasks for all the responsibilities that people may have during the execution of the activity of the original process. RAM subprocesses are created from collaboration templates. A collaboration template specifies how people with different responsibilities interact with each other to carry out an activity of a process. The collaboration template used is chosen at design-time amongst a library of templates depending on the specific requirements of the activity.

## Demo

A demo online of the functionality of RAM2BPMN is available at https://calm-brushlands-61585.herokuapp.com. The source code for that demo is available at http://github.com/isa-group/cristal-showcase.

## How to use it

RAM2BPMN can be used as follows:

```java
// Creates the repository
TemplateRepository repository = new FileTemplateRepository(new File("repository_folder"));

// Loads the input bpmn document
Bpmn20ModelHandler handler = new Bpmn20ModelHandlerImpl();
handler.load(new FileInputStream("inputBpmnDocument.xml"));

// Creates the RAM
RAM matrix = new RAM();
matrix.add(
        new BoundedRole(new Activity("Write Paper"), RASCI.responsible, new Role("r1")),
        new BoundedRole(new Activity("Write Paper"), RASCI.support, new Role("r2")),
        new BoundedRole(new Activity("Write Paper"), RASCI.accountable, new Role("r3")),
        new BoundedRole(new Activity("Submit Paper"), RASCI.responsible, new Role("r2"))
);

// Creates the template assignment
Map<String, String> assignment = new HashMap<>();
assignment.put("Write Paper", "fragment-rasci");
assignment.put("Submit Paper", "static-rasci");

// Runs the algorithm
Ram2Bpmn ram2Bpmn = new Ram2Bpmn();
ram2Bpmn.transformProcess(matrix, handler, repository.buildAssignmentFrom(request.getTempl()));

// Outputs the result to console
handler.save(System.out);
```

In this example, we assume that the input bpmn document is called "inputBpmnDocument.xml" and it contains two activities: Write Paper and Submit Paper. We also define a RAM using [RASCI responsibilities](https://en.wikipedia.org/wiki/Responsibility_assignment_matrix#RASCI) for three organizational roles, namely r1, r2, and r3 as follows:

| Activity | r1 | r2 | r3 |
|---|---|---|---|
|Write Paper| R | S | A |
|Submit Paper|  | R | |

Finally, we assign template "fragment-rasci" to activity Write Paper and template "static-rasci" to activity Submit Paper.

The result of RAM2BPMN is a process model like "inputBpmnDocument.xml" in which activities Write Paper and Submit Paper have been transformed into subprocesses and in which each subprocess is the result of the instantiation of the template for the matrix and the parameters of each activity. Note that the current implemention does not consider the graphical representation of the output model. This means that the output models are executable by BPMS like [Camunda](http://camunda.org), but the content of the subprocesses cannot be represented graphically using a BPMN editor.

A complete example on the use of RAM2BPMN is in [CRISTAL-showcase](http://github.com/isa-group/cristal-showcase).

## Definition of templates

The most important part of RAM2BPMN is the definition of the templates that will be used to generate the output model. The current implementation supports two types of templates: static templates and fragment templates. However, new types of templates can be easily integrated into the framework. One just need to extend class [AbstractTemplate](src/main/java/es/us/isa/cristal/ram2bpmn/templates/AbstractTemplate.java). 

Both static and fragment templates are defined as JSON files, which share three common aspects:
- A description of the template in natural language.
- The responsibilities and the cardinalities supported by the template. Each template may support different responsibilities and, even with the same responsibilities, they may support different cardinalities for each responsibility. For instance, one template may support RASCI responsibility "consult" for only one role while another template may support that responsibility for several roles (meaning you could have several people giving support at the same time for the same task).
- The mapping that will be used in the template to map the matrix to specific resource assignments in the business process model. Currently RAM2BPMN supports two mappings: RAL and Camunda.

Next we detail the structure of static and fragment templates.

### Static templates

A static template is a process model defined in BPMN that details the interaction between people with different responsibilities within an activity with just two peculiarities:
- Placeholders are used in the resource assignments and the names of the activities that will be replaced with values obtained from the matrix and/or the BPMN model during instantiation.
- XOR gateways are included in the process for enabling or disabling the activities specific to a responsibility. To this end, placeholders can also be used in the conditions of the gateways.

The instantiation mechanism of these static templates just involves the replacement of all the placeholders that appear in the template.

A static template is defined as a JSON file with the following structure:

```json
{
  "static" : {
    "description": "Static-based template for RASCI responsibilities",
    "cardinalities" : {
      "support" : {
        "min" : 0,
        "max" : 1
      },
      "consult" : {
        "min" : 0,
        "max" : 1
      },
      "informed" : {
        "min" : 0,
        "max" : null
      },
      "accountable" : {
        "min" : 0,
        "max" : 1
      },
      "responsible" : {
        "min" : 1,
        "max" : 1
      }
    },
    "xml" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n...",
    "mapping" : {
      "es.us.isa.cristal.ram.mappings.RalMapping" : { }
    }
  }
}
```

So, appart from the three common attributes described above, a static template includes an xml attribute that contains the xml code of the BPMN that represents the template. The placeholders that can be used in this xml code are the following ones:

|Placeholder name | Replacement of the placeholder |
|---|---|
|```@{activityName}```|Name of the activity obtained from the BPMN model |
|```@{activityId}```|Id of the activity obtained from the BPMN model |
|```@{responsibleBoD}```|Resource assignment expression that represents the person responsible for the activity |
|```@{f.duties()}```|Returns all responsibilities assigned to the activity at hand |
|```@{f.anyTD()}```|Returns whether there is any responsibility assigned to the activity at hand |
|```@{f.hasTD(r)}```|Returns whether responsibility r is assigned to the activity at hand |
|```@{f.onlyTD(r)}```|Returns whether only responsibility r is assigned to the activity at hand |
|```@{f.filter(r)}```|Returns the bounded roles assigned to to the activity at hand for responsibility r |
|```@{f.map(br)}```|Returns the resource assignment for BoundedRole br |
|```@{f.orMap(r)}```|Returns the resource assignment for responsibility r with joining all possible organizational roles with an or |
|```@{f.andMap(r)}```|Returns the resource assignment for responsibility r with joining all possible organizational roles with an and |

In addition, ```@{rasci.X}``` where X is either responsible, accountable, consulted, informed or support can be used as a variable that can be used as a parameter for the functions of the placeholders. Furthermore, for a BoundedRole br, one can access to the following properties:
```
// The activity of the bounded role
br.activity.name

// The responsibility of the bounded role
br. responsibility

// The organizational role of the bounded role
br.role.name
```

A fragment of an XML that could be used as a static template could be as follows (the full example can be found [here](src/test/resources/es/us/isa/cristal/ram2bpmn/templates/statictemplates/fulltemplate.xml):
```xml
<userTask id="sid-0B0F60C1-BB29-42C3-A92F-74F5D5D47379" name="Provide information for  @{activityName}" startQuantity="1">
  <potentialOwner>
     <resourceAssignmentExpression>
        <formalExpression>@{f.orMap(rasci.consulted)}</formalExpression>
     </resourceAssignmentExpression>
  </potentialOwner>
</userTask>
<userTask id="sid-AF9F4952-7B26-4883-BA4D-9DD5365E5C0A" name="Assess information for  @{activityName}" startQuantity="1">
  <potentialOwner>
     <resourceAssignmentExpression>
        <formalExpression>@{responsibleBoD}</formalExpression>
     </resourceAssignmentExpression>
  </potentialOwner>
  <incoming>sid-94B22D2C-8888-440B-A668-7F8033E1D7E2</incoming>
  <outgoing>sid-69F84B90-58A9-4FDA-BB57-ECDE4D614525</outgoing>
</userTask>
```

### Fragment templates

Fragment templates are made up of two different elements that can change from template to template, namely: a set of process fragments, at least one for each responsibility and a composition algorithm that is used for putting together all those fragments and for enabling or disabling the tasks specific to a responsibility.

Fragments must be subprocess graphs with single entry and single exit nodes (also de-noted as hammocks in graph literature) that represent the tasks that are necessary to carry out a given responsibility. Not only the same placeholders as with static templates but also ad-hoc placeholders can be used in the fragments. The value for these placeholders must be provided by the composition algorithm.

Concerning the composition algorithm, the idea is to start with a process that contains only a start event and an end event and to insert in it all process fragments, combining them in a meaningful way based on the information provided by the matrix and the BPMN model. To describe how the fragments are composed together, change patterns are used. Currently, the following three change patterns have been implemented:

| Change operation | Meaning |
| --- | --- |
| `f1.insertSequential(f2)` | Inserts fragment f2 just after fragment f1 and returns f1 modified |
| `f1.insertSequential(f2, a)` | Inserts fragment f2 sequentially after element `a` of fragment f1 and returns f2 |
| `f1.insertParallel(f2)` | Inserts fragment f2 in parallel to fragment f1 |
| `f1.embedInLoop(loop, exit)` | Embeds the fragment in a loop. Loop and exit are the loop condition and exit condition, respectively |

A fragment template is defined as a JSON document with the following structure:
```json
{
  "fragment" : {
    "cardinalities" : {
      "consult" : {
        "min" : 0,
        "max" : 1
      },
      "informed" : {
        "min" : 0,
        "max" : null
      },
      "accountable" : {
        "min" : 0,
        "max" : 1
      },
      "responsible" : {
        "min" : 1,
        "max" : 1
      },
      "support" : {
        "min" : 0,
        "max" : 1
      }
    },
    "code" : "...",
    "fragments" : {
      "f1" : "...",
      "f2" : "...",
      "..." : "..."
    },
    "mapping" : {
      "es.us.isa.cristal.ram.mappings.RalMapping" : { }
    }
  }
}
```

So, appart from the three common attributes described above, a fragment template includes a code attribute that contains the code of the composition algorithm and a fragments attribute that contains the process fragments as BPMN documents. 

The code of the composition algorithm can use the placeholders mentioned above, the change operations and a function `fragments.replace("fragmentName", ["k1": "v1", "k2": "v2"])` that instantiates and returns the fragment "fragmentName". The second parameter are optional parameters that can be provided for the template instantiation. An example of composition algorithm that uses five fragments (one for each RASCI responsibility) is as follows:

```
base = fragments.replace("responsible");
if (f.hasTD(rasci.support)) {
    base.insertParallel(fragments.replace("support"));
}
if (f.hasTD(rasci.consulted)) {
    base.insertParallel(fragments.replace("consult"));
}
if (f.hasTD(rasci.accountable)) {
    base.insertSequential(fragments.replace("accountable"));
    base.embedInLoop("${!approve}", "${approve}");
}
if (f.hasTD(rasci.informed)) {
    informRoles = f.filter(rasci.informed);
    informFragment = null;
    foreach(role: informRoles) {
        if (informFragment == null) {
            informFragment = fragments.replace("inform", ["informed": role.role.name]);
        } else {
            informFragment.insertParallel(fragments.replace("inform", ["informed": role.role.name]));
        }

    }
    base.insertSequential(informFragment);

}
return base;
```

This code is interpreted by [MVEL](https://github.com/mvel/mvel). Therefore, its syntax should be that of [MVEL2](http://mvel.documentnode.com). It starts by instantiating the fragment for responsible and using it as "base". After that, fragments for all other responsibilities are added either in parallel or sequentially. Furthermore, the fragment for accountable is also embedded in a loop.

Finally, as for the fragments, they are regular BPMN models with the exception that the fragment must start with a sequence flow (without source) and end with a sequence flow (without target). The placeholders that can be used here are the same as those in static templates. An example of fragment for responsibility accountable is:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:camunda="http://camunda.org/schema/1.0/bpmn"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             exporter="Signavio Process Editor, http://www.signavio.com" exporterVersion=""
             expressionLanguage="http://www.w3.org/1999/XPath" id="sid-c703ffd3-a9e1-4b6f-9c14-e1120a3f5e25"
             targetNamespace="http://www.signavio.com/bpmn20" typeLanguage="http://www.w3.org/2001/XMLSchema"
             xsi:schemaLocation="http://www.omg.org/spec/BPMN/20100524/MODEL http://www.omg.org/spec/BPMN/2.0/20100501/BPMN20.xsd">
<process id="sid-f30ec2c1-1e66-4749-ba76-2b24c1711020" isExecutable="false">
   <userTask completionQuantity="1" id="sid-8FC733FC-1008-418C-BA7D-8212474BAB86" isForCompensation="false" name="Approve @{activityName}" startQuantity="1">
      <potentialOwner>
         <resourceAssignmentExpression>
            <formalExpression>@{f.orMap(rasci.accountable)}</formalExpression>
         </resourceAssignmentExpression>
      </potentialOwner>
      <extensionElements>
         <camunda:formData>
            <camunda:formField
                    id="approve" label="Approve @{activityName}?" type="boolean">
            </camunda:formField>
         </camunda:formData>
      </extensionElements>
   </userTask>
   <sequenceFlow id="sid-F1C0F9A6-4BEE-47A4-8E24-9A23BA90B332" name="" targetRef="sid-8FC733FC-1008-418C-BA7D-8212474BAB86"/>
   <sequenceFlow id="sid-BBA8AFEA-62FC-4734-8A99-810C0B1F079A" name="" sourceRef="sid-8FC733FC-1008-418C-BA7D-8212474BAB86"/>
</process>
</definitions>
```
