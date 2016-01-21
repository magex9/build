package ca.magex.maven.plugin;

import java.io.File;
import java.io.IOException;

import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.FileUtils;

/**
 * Says "Hi" to the user.
 *
 */
@Mojo(name = "sayhi")
public class BuildInfoMojo extends AbstractMojo {

	@Component
	protected MavenProject project;
	
	@Component
	protected MavenProjectHelper projectHelper;

	@Component
	private ArtifactResolver artifactResolver;

	public void execute() throws MojoExecutionException {
		getLog().info("Hello, world.");

		try {
			new File("target").mkdir();
			FileUtils.fileWrite("target/build-info.properties", "test=42");
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating file", e);
		}
		
		projectHelper.attachArtifact(project, "props", "build-info", new File("target/build-info.properties"));

	}
}