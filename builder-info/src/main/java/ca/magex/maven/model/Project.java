package ca.magex.maven.model;

import java.util.List;
import java.util.Map;

public class Project {

	private Pom pom;
	
	private Map<String, List<Dependency>> dependencyTree;
	
	private Map<String, List<Dependency>> reverseTree;

	public Project(Pom pom) {
		this.pom = pom;
	}

	public Pom getPom() {
		return pom;
	}

	public Map<String, List<Dependency>> getDependencyTree() {
		return dependencyTree;
	}

	public Map<String, List<Dependency>> getReverseTree() {
		return reverseTree;
	}

}
