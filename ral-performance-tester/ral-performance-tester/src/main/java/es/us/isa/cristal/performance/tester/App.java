package es.us.isa.cristal.performance.tester;

import es.us.isa.cristal.organization.generator.ConfigurationFactory;
import es.us.isa.cristal.organization.generator.OrganizationGenerator;
import es.us.isa.cristal.organization.model.gson.Model;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        
    	ConfigurationFactory factory = new ConfigurationFactory();
		OrganizationGenerator generator = new OrganizationGenerator(factory.getDefaultConfiguration(5));
		
		Model model = generator.generate();
    	
    }
}
