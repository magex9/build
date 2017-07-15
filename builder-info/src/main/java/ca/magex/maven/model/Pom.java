package ca.magex.maven.model;

import java.util.List;

public class Pom {

	private Gav id;
	
	private Gav parent;
	
	private String name;
	
	private String description;
	
	private String scm;
	
	private List<Dependency> dependencies;
	
	private List<Plugin> plugins;
	
	private List<Gav> artifacts;

	public Gav getId() {
		return id;
	}

	public Gav getParent() {
		return parent;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getScm() {
		return scm;
	}

	public List<Dependency> getDependencies() {
		return dependencies;
	}

	public List<Plugin> getPlugins() {
		return plugins;
	}
	
	public List<Gav> getArtifacts() {
		return artifacts;
	}
	
}
