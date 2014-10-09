package es.us.isa.cristal.neo4j.test.utils.graph;

public class GraphUtil {

	public static final String THEOS_GRAPH = "CREATE anthony = { name : 'Anthony', degree: 'PhD', cost: 10}\n" +
            "CREATE betty = { name : 'Betty', cost: 3 }\n" +
            "CREATE daniel = { name : 'Daniel', cost: 5 }\n" +
            "CREATE anna = { name : 'Anna', cost: 4 }\n" +
            "CREATE charles = { name : 'Charles', degree: 'PhD', cost: 8 }\n" +
            "CREATE christine = { name : 'Christine', degree: 'MsC', cost: 7 }\n" +
            "CREATE adele = { name : 'Adele', cost: 7 }\n" +
            "\n" +
            "CREATE theos = { unit : 'Theos' }\n" +
            "\n" +
            "CREATE theoscoordinator = {position : 'THEOS Project Coordinator' }\n" +
            "CREATE theosaccount = {position : 'THEOS Account Delegate'}\n" +
            "CREATE theostechnician = { position : 'THEOS Technician'}\n" +
            "CREATE theosassistant = {position : 'THEOS Administrative Assistant'}\n" +
            "CREATE theosresponsible = {position : 'THEOS Responsible for Work Package'}\n" +
            "CREATE theosstudent = {position : 'THEOS PhD Student'}\n" +
            "\n" +
            "CREATE coordinator = {role : 'Project Coordinator'}\n" +
            "CREATE administrator = {role: 'Account Administrator'}\n" +
            "CREATE resourcemanager = {role: 'Resource Manager'}\n" +
            "CREATE responsible = {role: 'Responsible for Work Package'}\n"+
            "CREATE researcher = {role: 'Researcher'}\n"+
            "CREATE student = {role: 'PhD Student'}\n" +
            "CREATE clerk = {role: 'Clerk'}\n" +
            "CREATE technician = {role: 'Technician'}\n" +
            "\n" +
            "CREATE anthony-[:POSITION]->theoscoordinator \n" +
            "CREATE anthony-[:POSITION]->theosresponsible \n" +
            "CREATE betty-[:POSITION]->theosaccount \n" +
            "CREATE daniel-[:POSITION]->theostechnician \n" +
            "CREATE anna-[:POSITION]->theosassistant \n"+
            "CREATE christine-[:POSITION]->theosstudent \n"+
            "CREATE adele-[:POSITION]->theosstudent \n" +
            "CREATE charles-[:POSITION]->theosresponsible \n" +
            "\n" +
            "CREATE theoscoordinator-[:UNIT]->theos \n"+
            "CREATE theosaccount-[:UNIT]->theos \n"+
            "CREATE theostechnician-[:UNIT]->theos \n"+
            "CREATE theosassistant-[:UNIT]->theos \n"+
            "CREATE theosresponsible-[:UNIT]->theos \n"+
            "CREATE theosstudent-[:UNIT]->theos \n"+
            "\n" +
            "CREATE theoscoordinator-[:ROLE]->coordinator \n"+
            "CREATE theoscoordinator-[:ROLE]->administrator \n"+
            "CREATE theoscoordinator-[:ROLE]->resourcemanager \n"+
            "CREATE theosresponsible-[:ROLE]->responsible \n"+
            "CREATE theosresponsible-[:ROLE]->researcher \n"+
            "CREATE theosstudent-[:ROLE]->researcher \n"+
            "CREATE theosstudent-[:ROLE]->student \n"+
            "CREATE theosaccount-[:ROLE]->administrator \n"+
            "CREATE theostechnician-[:ROLE]->technician \n"+
            "CREATE theosassistant-[:ROLE]->clerk \n"+
            "\n" +
            "CREATE theoscoordinator-[:DELEGATES]->theosaccount \n"+
            "CREATE theoscoordinator-[:DELEGATES]->theostechnician \n"+
            "CREATE theoscoordinator-[:DELEGATES]->theosassistant \n" +
            "CREATE theoscoordinator-[:DELEGATES]->theosresponsible \n" +
            "CREATE theosresponsible-[:DELEGATES]->theosstudent \n " +
            "CREATE theoscoordinator<-[:REPORTS]-theosaccount \n"+
            "CREATE theoscoordinator<-[:REPORTS]-theostechnician \n"+
            "CREATE theoscoordinator<-[:REPORTS]-theosassistant \n" +
            "CREATE theoscoordinator<-[:REPORTS]-theosresponsible \n" +
            "CREATE theosresponsible<-[:REPORTS]-theosstudent";
	
	
	public static final String DEVELOPMENT_UNIT_GRAPH = "CREATE peter = { name : 'Peter' } \n" +
			"CREATE john = { name : 'John' } \n" + 
			"CREATE mary = { name : 'Mary' } \n" + 
			"CREATE cathy = { name : 'Cathy' } \n" + 
			"CREATE albert = { name : 'Albert' } \n" + 
			"CREATE programmer = {role : 'Programmer'} \n" + 
			"CREATE analyst = {role : 'Analyst'} \n" + 
			"CREATE productowner = {role : 'Product Owner'} \n" +
			"CREATE scrummaster = {role : 'Scrum Master'} \n" +
			"CREATE designer = {role : 'Designer'} \n" + 
			"CREATE development = {unit : 'Development'} \n" +
			"CREATE juniorsoftwaredeveloper = {position : 'Junior Software Developer' } \n" +
			"CREATE seniorsoftwaredeveloper = {position : 'Senior Software Developer' } \n" +
			"CREATE teamleader = {position : 'Team Leader' } \n" +
			"CREATE projectdirector = {position : 'Project Director' } \n" +
			"CREATE juniorsoftwaredeveloper-[:UNIT]->development \n" +
			"CREATE peter-[:POSITION]->juniorsoftwaredeveloper \n" +
			"CREATE juniorsoftwaredeveloper-[:ROLE]->programmer \n" +
			"CREATE seniorsoftwaredeveloper-[:UNIT]->development \n" +
			"CREATE mary-[:POSITION]->seniorsoftwaredeveloper \n" +
			"CREATE albert-[:POSITION]->seniorsoftwaredeveloper \n" +
			"CREATE seniorsoftwaredeveloper-[:ROLE]->programmer \n" +
			"CREATE seniorsoftwaredeveloper-[:ROLE]->analyst \n" +
			"CREATE teamleader-[:UNIT]->development \n" +
			"CREATE cathy-[:POSITION]->teamleader \n" +
			"CREATE teamleader-[:ROLE]->scrummaster \n" +
			"CREATE teamleader-[:ROLE]->analyst \n" +
			"CREATE teamleader-[:ROLE]->programmer \n" +
			"CREATE projectdirector-[:UNIT]->development \n" +
			"CREATE john-[:POSITION]->projectdirector \n" +
			"CREATE projectdirector-[:ROLE]->productowner";
	
}
