package ca.magex.maven.model;

public class Dependency {

	private Gav gav;
	
	private String scope;
	
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
	
}
