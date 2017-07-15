package ca.magex.maven.model;

import java.util.List;

public class Dependency {

	private Gav gav;
	
	private String scope;
	
	private List<String> exclusions;

	public Gav getGav() {
		return gav;
	}

	public void setGav(Gav gav) {
		this.gav = gav;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public List<String> getExclusions() {
		return exclusions;
	}

	public void setExclusions(List<String> exclusions) {
		this.exclusions = exclusions;
	}
	
}
