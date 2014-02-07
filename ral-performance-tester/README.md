RAL-PERFORMANCE-TESTER
======================

RAL-PERFORMANCE-TESTER is a small project that provides a simple mechanism to test a set of RAL expressions and their performance.  

CONFIGURATION:

To run the project, execute the class App.java. To configure the execution, modify the file queryStack.json. This file contains the following structure (in json): 

{
	export: "results.html",  // the file and the format in which results will be presented.
	modelWeight: 20,       // the weight of the organizational model generated. For further information see [https://github.com/mleonrivas/cristal/tree/master/organization-generator](https://github.com/mleonrivas/cristal/tree/master/organization-generator).
	processExpressionForEachIteration: true, // see [1]
	iterations: 5, // times that every query will be executed, in order to get a set of execution measurements.
	queryList: [
			{ activity: "activity1", query: "HAS ROLE #RandomRole#" },
			{ activity: "activity2", query: "REPORTS TO POSITION #RandomPosition#"},
			{ activity: "activity3", query: "CAN DELEGATE WORK TO POSITION #RandomPosition#"},
			{ activity: "activity3", query: "CAN HAVE WORK DELEGATED BY POSITION #RandomPosition#"}
	]

}

[1] The RAL queries have expressions that must be processed before the query is executed; for instance: #RandomRole#. The parameter "processExpressionForEachIteration" setted to false means the expression will be processed only once. Afterwards the same query will be executed n times. Otherwise, if it is setted to true the query will be processed just before being parsed and executed. In this way, each execution will be different: Same query type (HAS ROLE) but different data (roleA, roleB, roleC...)
Those expressions mentioned above can be created following a simple rule: #(First | Last | Random) + (Person | Role | Unit | Position)#. A few samples:
#FirstPerson# #LastPosition# #RandomUnit#

RESULTS:

The results are exported to the file specified in the queryStack.json file. By now, only HTML files are supported. The report contains the script used to generate the organization graph database, and the collection of executions for each query, containing the measurements gathered during executions (only time elapsed for now), the query executed and the result of the execution.  



This project is part of CRISTAL (Collection of Resource-centrIc Supporting Tools And Languages). More information about CRISTAL can be found at [http://www.isa.us.es/cristal/](http://www.isa.us.es/cristal/).
