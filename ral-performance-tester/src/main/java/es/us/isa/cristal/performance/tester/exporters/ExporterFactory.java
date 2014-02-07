package es.us.isa.cristal.performance.tester.exporters;

/**
 * 
 * @author Manuel Leon
 *
 */
public class ExporterFactory {

	Exporter htmlExporter;
	
	public ExporterFactory(){
		htmlExporter = new HTMLExporter();
	}
	
	public Exporter getExporter(String filename){

		if(filename.endsWith(htmlExporter.getExtensionIdentifier())){
			return htmlExporter;
		}else{
			throw new RuntimeException("Export format not supported yet");
		}
		
	}
	
}
