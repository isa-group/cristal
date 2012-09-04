package es.us.isa.cristal.web;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.FileUtils;

import raci2bpmn.ModelHandler;
import raci2bpmn.ProcessHandler;
import bpmn.TTask;

@Path("/api/repository")
public class RepositoryResource {
	
	private String directory;
	private String resourcesDirectory;
	private String editorUrl;

	

	public String getEditorUrl() {
		return editorUrl;
	}

	public void setEditorUrl(String editorUrl) {
		this.editorUrl = editorUrl;
	}

	public String getResourcesDirectory() {
		return resourcesDirectory;
	}

	public void setResourcesDirectory(String resourcesDirectory) {
		this.resourcesDirectory = resourcesDirectory;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	@Path("/processes")
	@GET
	@Produces("application/json")
	public List<ProcessInfo> getProcesses(@Context UriInfo uriInfo) {
		List<ProcessInfo> result = new ArrayList<ProcessInfo>();
		File dir = new File(directory);
		File[] files = dir.listFiles();
		
		if (files != null) {
			for(File f : files) {
				String filename = f.getName();
				if (filename.endsWith("bpmn20.xml")) {
					String processName = filename.replace(".bpmn20.xml", "");
					
					UriBuilder ub = uriInfo.getBaseUriBuilder().path(this.getClass()).path(this.getClass(), "getProcess");
					URI uri = ub.build(processName);
					
					String editor = editorUrl  + "/p/editor?id=root-directory%3B"+processName+".signavio.xml";
					result.add(new ProcessInfo(processName, uri.toString(), editor));
					
				}
				
			}
		}
		
		return result;			
	}
	
	@Path("/processes/{id}")
	@Produces("application/xml")
	@GET
	public String getProcess(@PathParam("id") String id) {
		File processFile = getProcessFile(id);
		String process="";
		try {
			process = FileUtils.readFileToString(processFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return process;	
		
	}
	
	private File getProcessFile(String id) {
		String filename = directory + File.separator + id + ".bpmn20.xml";
		File processFile = new File(filename);
		
		return processFile;		
	}
	
	private File getResourcesFile(String id) {
		String filename = resourcesDirectory + File.separator + id + ".raci.json";
		File resourcesFile = new File(filename);
		
		return resourcesFile;		
	}
	
	@Path("/processes/{id}/activities")
	@Produces("application/json")
	@GET
	public List<String> getActivityNames(@PathParam("id") String id) {
		List<String> activities = new ArrayList<String>(); 
		File processFile = getProcessFile(id);
		ModelHandler model = new ModelHandler();
		model.loadModelFile(processFile);
		
		ProcessHandler handler = new ProcessHandler(model.getDefinitions());
		
		List<TTask> tasks = handler.getTasks();
		
		for(TTask task: tasks) {
			String name = task.getName();
			if (name == null || "".equals(name))
				name = task.getId();
			
			activities.add(name);
		}
		
		return activities;
	}
	
	@Path("/processes/{id}/raci")
	@Produces("application/json")
	@GET
	public String getResources(@PathParam("id") String id) {
		File resourcesFile = getResourcesFile(id);
		String raci="";
		try {
			raci = FileUtils.readFileToString(resourcesFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if ("".equals(raci))
			throw new org.jboss.resteasy.spi.NotFoundException("RACI not found");
		
		return raci;	

	}
	
	@Path("/processes/{id}/raci")
	@POST
	public void saveRaci(@PathParam("id") String id, @FormParam("raci") String raci) {
		File resourcesFile = getResourcesFile(id);
		try {
			FileUtils.writeStringToFile(resourcesFile, raci);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
