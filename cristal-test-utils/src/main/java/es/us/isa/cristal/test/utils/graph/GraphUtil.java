package es.us.isa.cristal.test.utils.graph;

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
            "CREATE theos_coordinator = {position : 'THEOS Project Coordinator' }\n" +
            "CREATE theos_account = {position : 'THEOS Account Delegate'}\n" +
            "CREATE theos_technician = { position : 'THEOS Technician'}\n" +
            "CREATE theos_assistant = {position : 'THEOS Administrative Assistant'}\n" +
            "CREATE theos_responsible = {position : 'THEOS Responsible for Work Package'}\n" +
            "CREATE theos_student = {position : 'THEOS PhD Student'}\n" +
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
            "CREATE anthony-[:POSITION]->theos_coordinator \n" +
            "CREATE anthony-[:POSITION]->theos_responsible \n" +
            "CREATE betty-[:POSITION]->theos_account \n" +
            "CREATE daniel-[:POSITION]->theos_technician \n" +
            "CREATE anna-[:POSITION]->theos_assistant \n"+
            "CREATE christine-[:POSITION]->theos_student \n"+
            "CREATE adele-[:POSITION]->theos_student \n" +
            "CREATE charles-[:POSITION]->theos_responsible \n" +
            "\n" +
            "CREATE theos_coordinator-[:UNIT]->theos \n"+
            "CREATE theos_account-[:UNIT]->theos \n"+
            "CREATE theos_technician-[:UNIT]->theos \n"+
            "CREATE theos_assistant-[:UNIT]->theos \n"+
            "CREATE theos_responsible-[:UNIT]->theos \n"+
            "CREATE theos_student-[:UNIT]->theos \n"+
            "\n" +
            "CREATE theos_coordinator-[:ROLE]->coordinator \n"+
            "CREATE theos_coordinator-[:ROLE]->administrator \n"+
            "CREATE theos_coordinator-[:ROLE]->resourcemanager \n"+
            "CREATE theos_responsible-[:ROLE]->responsible \n"+
            "CREATE theos_responsible-[:ROLE]->researcher \n"+
            "CREATE theos_student-[:ROLE]->researcher \n"+
            "CREATE theos_student-[:ROLE]->student \n"+
            "CREATE theos_account-[:ROLE]->administrator \n"+
            "CREATE theos_technician-[:ROLE]->technician \n"+
            "CREATE theos_assistant-[:ROLE]->clerk \n"+
            "\n" +
            "CREATE theos_coordinator-[:DELEGATES]->theos_account \n"+
            "CREATE theos_coordinator-[:DELEGATES]->theos_technician \n"+
            "CREATE theos_coordinator-[:DELEGATES]->theos_assistant \n" +
            "CREATE theos_coordinator-[:DELEGATES]->theos_responsible \n" +
            "CREATE theos_responsible-[:DELEGATES]->theos_student \n " +
            "CREATE theos_coordinator<-[:REPORTS]-theos_account \n"+
            "CREATE theos_coordinator<-[:REPORTS]-theos_technician \n"+
            "CREATE theos_coordinator<-[:REPORTS]-theos_assistant \n" +
            "CREATE theos_coordinator<-[:REPORTS]-theos_responsible \n" +
            "CREATE theos_responsible<-[:REPORTS]-theos_student";
	
	
	public static final String DEVELOPMENT_UNIT_GRAPH = "CREATE peter = { name : 'Peter' } \n" + 
			"CREATE john = { name : 'John' } \n" + 
			"CREATE mary = { name : 'Mary' } \n" +
			"CREATE cathy = { name : 'Cathy' } \n" +
			"CREATE albert = { name : 'Albert' } \n" +
			"CREATE programmer = {role : 'Programmer'} \n" +
			"CREATE analyst = {role : 'Analyst'} \n" +
			"CREATE product_owner = {role : 'Product Owner'} \n" +
			"CREATE scrum_master = {role : 'Scrum Master'} \n" +
			"CREATE designer = {role : 'Designer'} \n" +
			"CREATE development = {unit : 'Development'} \n" +
			"CREATE junior_software_developer = {position : 'Junior Software Developer' } \n" +
			"CREATE senior_software_developer = {position : 'Senior Software Developer' } \n" +
			"CREATE team_leader = {position : 'Team Leader' } \n" +
			"CREATE project_director = {position : 'Project Director' } \n" +
			"CREATE junior_software_developer-[:UNIT]->development \n" +
			"CREATE peter-[:POSITION]->junior_software_developer \n" +
			"CREATE junior_software_developer-[:ROLE]->programmer \n" +
			"CREATE senior_software_developer-[:UNIT]->development \n" +
			"CREATE mary-[:POSITION]->senior_software_developer \n" +
			"CREATE albert-[:POSITION]->senior_software_developer \n" +
			"CREATE senior_software_developer-[:ROLE]->programmer \n" +
			"CREATE senior_software_developer-[:ROLE]->analyst \n" +
			"CREATE team_leader-[:UNIT]->development \n" +
			"CREATE cathy-[:POSITION]->team_leader \n" +
			"CREATE team_leader-[:ROLE]->scrum_master \n" +
			"CREATE team_leader-[:ROLE]->analyst \n" +
			"CREATE team_leader-[:ROLE]->programmer \n" +
			"CREATE project_director-[:UNIT]->development \n" +
			"CREATE john-[:POSITION]->project_director \n" +
			"CREATE project_director-[:ROLE]->product_owner";
	
}
