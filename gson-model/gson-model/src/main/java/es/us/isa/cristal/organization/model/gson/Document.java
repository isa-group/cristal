package es.us.isa.cristal.organization.model.gson;

import java.util.List;

public class Document implements CypherGenerator{
	private List<String> shared;
	private String modelId;
	private int revision;
	private String description;
	private String name;
	private String type;
	//extensions??
	
	private Model model;

	
	public String getCypherCreateQuery() {
		return model.getCypherCreateQuery();
	}
	
	
	/**
	 * @return the shared
	 */
	public final List<String> getShared() {
		return shared;
	}

	/**
	 * @return the modelId
	 */
	public final String getModelId() {
		return modelId;
	}

	/**
	 * @return the revision
	 */
	public final int getRevision() {
		return revision;
	}

	/**
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public final String getType() {
		return type;
	}

	/**
	 * @return the model
	 */
	public final Model getModel() {
		return model;
	}

	/**
	 * @param shared the shared to set
	 */
	public final void setShared(List<String> shared) {
		this.shared = shared;
	}

	/**
	 * @param modelId the modelId to set
	 */
	public final void setModelId(String modelId) {
		this.modelId = modelId;
	}

	/**
	 * @param revision the revision to set
	 */
	public final void setRevision(int revision) {
		this.revision = revision;
	}

	/**
	 * @param description the description to set
	 */
	public final void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @param type the type to set
	 */
	public final void setType(String type) {
		this.type = type;
	}

	/**
	 * @param model the model to set
	 */
	public final void setModel(Model model) {
		this.model = model;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((modelId == null) ? 0 : modelId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + revision;
		result = prime * result + ((shared == null) ? 0 : shared.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (modelId == null) {
			if (other.modelId != null)
				return false;
		} else if (!modelId.equals(other.modelId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (revision != other.revision)
			return false;
		if (shared == null) {
			if (other.shared != null)
				return false;
		} else if (!shared.equals(other.shared))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	
	
	
	
	

}