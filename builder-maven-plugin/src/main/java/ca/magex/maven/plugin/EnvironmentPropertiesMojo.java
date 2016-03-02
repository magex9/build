package ca.magex.maven.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

@Mojo(name = "env-props")
public class EnvironmentPropertiesMojo extends AbstractMojo {

	@Component
	protected MavenProject project;
	
	@Component
	protected MavenProjectHelper projectHelper;

	public void execute() throws MojoExecutionException {
		try {
			new File("target").mkdir();
			File file = new File("target/build-info.properties");
			Properties properties = new Properties();
			for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
				properties.setProperty(entry.getKey(), entry.getValue());
			}
			properties.store(new FileOutputStream(file), "Generated on " + new Date());
			projectHelper.attachArtifact(project, "props", "env", file);
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating file", e);
		}
	}
	
}