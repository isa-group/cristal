ral-performance-tester
======================

**ral-performance-tester** is a small project that provides a simple mechanism to test the performance of resource-aware
analysis operations.

Usage
-----

The project provides an abstract class called `AbstractRALPerformanceTest` that provides a set of abstractions that
are useful to build your own performance testers. These abstractions include:

`generateModel(30)`: Returns an organisational model randomly generated with 30 positions, 30 x 3/2 people, and 30 x 3/20 roles.

`loadAssignment(m, "a1", "HAS ROLE #RandomRole", "a2", "HAS POSITION #RandomPosition"...)`: Returns a resource assignment for
organisational model `m` in which activity `a1` has expression `HAS ROLE #RandomRole` and activity `a2` has expression
`HAS POSITION #RandomPosition`. The RAL queries have expressions, for instance `#RandomRole` that are processed while
loading the assignments. Those expressions mentioned above can be created following a simple rule:
`#(First | Last | Random) + (Person | Role | Unit | Position)#`. A few examples are `#FirstPerson# #LastPosition# #RandomUnit#`.

`owlAnalyser(model, assignment, true, metrics)`: Returns an owlAnalyser for organisational model `model` and resource
 assignment `assignment`. The third parameter specifies whether a precompute (`true`) or not (`false`) of the organisational
 model should be done before running the operations. Finally, the last parameter, which is optional, is a `Map<String,Long>`
 that is used to store the measures of the different steps of the OWL analyser building process.

`neo4JAnalyser(model, assignment)`: Returns a Neo4J analyser for organisational model `model` and resource assignment
`assignment`.

`iterations(n)`: Sets the number of iterations done for each performance analysis to `n`.

`useTimeMeter()`: Defines the time meter to use. Only one time meter is available right now.

`exportTxt(output)`, `exportHtml(output)`, `exportJson(output)`: Exports the results of the performance analysis to `output`
in the specified format: txt, HTML or JSON, respectively.

`file(fileName)`: Returns an output for file named `fileName`.

`console()`: Returns an output for the console.

Example
--------

The executable class [`es.us.isa.cristal.performance.tester.App`](src/main/java/es/us/isa/cristal/performance/tester/App.java) is an example of how performance evaluation can be done
using this abstraction.

It first defines 6 different resource assignments attending to the size of the process model and the type of expressions
used in the model:

```java
    static final String[] simpleShort = {
            "activity1", "HAS ROLE #RandomRole#",
            "activity2", "HAS POSITION #RandomPosition#",
            "activity3", "HAS UNIT #RandomUnit#",
            "activity4", "HAS ROLE #RandomRole#",
            "activity5", "HAS POSITION #RandomPosition#"
    };

    static final String[] simpleLong = {
            "activity1", "HAS ROLE #RandomRole#",
            "activity2", "HAS POSITION #RandomPosition#",
            "activity3", "HAS UNIT #RandomUnit#",
            "activity4", "HAS ROLE #RandomRole#",
            "activity5", "HAS ROLE #RandomRole#",
            "activity6", "HAS POSITION #RandomPosition#",
            "activity7", "HAS UNIT #RandomUnit#",
            "activity8", "HAS ROLE #RandomRole#",
            "activity9", "HAS ROLE #RandomRole#",
            "activity10", "HAS POSITION #RandomPosition#",
            "activity11", "HAS UNIT #RandomUnit#",
            "activity12", "HAS ROLE #RandomRole#",
            "activity13", "HAS ROLE #RandomRole#",
            "activity14", "HAS POSITION #RandomPosition#",
            "activity15", "HAS UNIT #RandomUnit#",
            "activity16", "HAS ROLE #RandomRole#",
            "activity17", "HAS ROLE #RandomRole#",
            "activity18", "HAS POSITION #RandomPosition#",
            "activity19", "HAS UNIT #RandomUnit#",
            "activity20", "HAS ROLE #RandomRole#"};

    static final String[] shortComposite = {
            "activity2", "HAS POSITION #RandomPosition#",
            "activity1", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#",
            "activity4", "HAS POSITION #RandomPosition# AND HAS UNIT #RandomUnit#",
            "activity5", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#",
            "activity7", "HAS POSITION #RandomPosition# AND HAS UNIT #RandomUnit#"};

    static final String[] longComposite = {
            "activity2","HAS POSITION #RandomPosition#" ,
            "activity1","HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity4","HAS POSITION #RandomPosition# AND HAS UNIT #RandomUnit#" ,
            "activity5","HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity7","HAS POSITION #RandomPosition# AND HAS UNIT #RandomUnit#" ,
            "activity8","HAS POSITION #RandomPosition#" ,
            "activity9","HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity11", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity21", "HAS POSITION #RandomPosition# OR HAS ROLE #RandomRole#" ,
            "activity24", "HAS POSITION #RandomPosition# AND HAS UNIT #RandomUnit#" ,
            "activity25", "HAS ROLE #RandomRole# AND HAS ROLE #RandomRole#" ,
            "activity29", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity211", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity310", "NOT HAS POSITION #RandomPosition#" ,
            "activity311", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity322", "HAS POSITION #RandomPosition#" ,
            "activity324", "HAS ROLE #RandomRole# AND HAS UNIT #RandomUnit#" ,
            "activity321", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity324", "HAS ROLE #RandomRole# AND HAS UNIT #RandomUnit#" ,
            "activity325", "NOT HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#"};

    static final String[] shortAC = {
            "activity2", "HAS POSITION #RandomPosition#" ,
            "activity1", "HAS ROLE #RandomRole#" ,
            "activity6", "IS PERSON WHO DID ACTIVITY activity1" ,
            "activity10", "NOT IS PERSON WHO DID ACTIVITY activity6" ,
            "activity5", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#"};

    static final String[] longAC = {
            "activity2", "HAS POSITION #RandomPosition#" ,
            "activity1", "HAS ROLE #RandomRole#" ,
            "activity6", "IS PERSON WHO DID ACTIVITY activity1" ,
            "activity10", "NOT IS PERSON WHO DID ACTIVITY activity6" ,
            "activity5", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#",
            "activity28", "IS PERSON WHO DID ACTIVITY activity10 AND HAS POSITION #RandomPosition#" ,
            "activity4", "HAS POSITION #RandomPosition# AND HAS UNIT #RandomUnit#" ,
            "activity7", "HAS POSITION #RandomPosition# AND HAS UNIT #RandomUnit#" ,
            "activity8", "HAS POSITION #RandomPosition#" ,
            "activity9", "IS PERSON WHO DID ACTIVITY activity1" ,
            "activity3", "HAS ROLE #RandomRole# AND HAS UNIT #RandomUnit#",
            "activity25", "IS PERSON WHO DID ACTIVITY activity29" ,
            "activity29", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity211", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity310", "NOT HAS POSITION #RandomPosition#" ,
            "activity311", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity322", "HAS POSITION #RandomPosition#" ,
            "activity324", "IS PERSON WHO DID ACTIVITY activity2" ,
            "activity321", "HAS ROLE #RandomRole# OR HAS ROLE #RandomRole#" ,
            "activity324", "NOT IS PERSON WHO DID ACTIVITY activity5" };
```

Then, it defines runners for the different operations. The following code shows an example for critical participants
operation.

```java
    private OperationRun criticalParticipantsOperation(final OrganizationalModel org1, final RawResourceAssignment q2) {
        return new OperationRun() {
            DTSubClassRALAnalyser analyser;

            @Override
            public void setup() {
                Map<String, Long> setupMeasures = new HashMap<>();
                analyser = (DTSubClassRALAnalyser) owlAnalyser(org1, q2, false, setupMeasures);
                addInfo("Setup measure", mapToString(setupMeasures));
            }

            @Override
            public Object run() {
                return analyser.criticalParticipants(TaskDuty.RESPONSIBLE);

            }
        };
    }
```

Finally, it puts everything together. The following code shows an example for running a performance test for the
 operation critical participants with organizational models with sizes between 100 and 400 positions in increments of 10,
 using resource assignment `shortAC` and printing the results in the console as well as exporting them to files in HTML
 and JSON format.

```java
        useTimeMeter();
        iterations(15);

        for (int i = 100; i <= 400; i += 10) {
            final OrganizationalModel org1 = generateModel(i);
            final RawResourceAssignment qActive = loadAssignment(org1, shortAC);

            System.out.println("Running for " + i + "-------");

            run("criticalParticipants",
                    criticalParticipantsOperation(org1, qActive),
                    p("TaskDuty", TaskDuty.RESPONSIBLE),
                    p("Size ", i),
                    p("Assignment", toString(qActive)));
        }

        exportTxt(console());
        exportHtml(file(testName + "-" + nowString() + ".html"));
        exportJson(file(testName + "-" + nowString() + ".json"));
```

More
----------------

This project is part of CRISTAL (Collection of Resource-centrIc Supporting Tools And Languages).
More information about CRISTAL can be found at [http://www.isa.us.es/cristal/](http://www.isa.us.es/cristal/).
