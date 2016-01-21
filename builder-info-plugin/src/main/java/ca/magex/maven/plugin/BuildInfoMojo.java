package ca.magex.maven.plugin;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.FileUtils;

/**
 * Attach information to every jar file.
 *
 */
@Mojo(name = "attach")
public class BuildInfoMojo extends AbstractMojo {

	@Component
	protected MavenProject project;
	
	@Component
	protected MavenProjectHelper projectHelper;

	public void execute() throws MojoExecutionException {
		try {
			new File("target").mkdir();
			FileUtils.fileWrite("target/build-info.properties", "env=value");
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating file", e);
		}
		
		projectHelper.attachArtifact(project, "props", "build-info", new File("target/build-info.properties"));

	}
	
}