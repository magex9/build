package ca.magex.maven.model;

import java.util.Date;
import java.util.Map;

public class Build {
	
	private Project project;

	private String user;
	
	private Date date;
	
	private Map<String,String> settings;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}
	
}
