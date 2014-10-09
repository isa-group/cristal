package es.us.isa.cristal.organization.generator;

import es.us.isa.cristal.organization.model.gson.OrganizationalModel;

/**
 * 
 * @author Manuel Leon
 *
 */
public class Main {

	public static void main(String[] args) {
		
		ConfigurationFactory factory = new ConfigurationFactory();
		OrganizationGenerator generator = new OrganizationGenerator(factory.getDefaultConfiguration(5));
		
		OrganizationalModel model = generator.generate();
		
		System.out.println(model.getCypherCreateQuery());
		

	}

}
