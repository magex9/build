package ca.magex.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import ca.magex.maven.repository.FilesystemMavenRepository;
import ca.magex.maven.repository.MavenRepository;

@Mojo(name = "complete-maven-repository", requiresProject = false)
public class CompleteMavenRepositoryMojo extends AbstractMavenMojo {

	@Parameter(property = "src", required = true)
	private String src;

	@Parameter(property = "dest", required = true)
	private String dest;

	public void execute() throws MojoExecutionException {
		MavenRepository sourceRepo = MavenRepositoryFactory.createMavenRepository(src);
		FilesystemMavenRepository destRepo = (FilesystemMavenRepository)MavenRepositoryFactory.createMavenRepository(dest);

		getLog().info("Uploading " + src + " into repository " + dest);
		destRepo.findAllGroupIds().stream().forEach(groupId -> {
			if (!groupId.startsWith("ca.magex")) {
				destRepo.findArtifactIds(groupId).stream().forEach(artifactId -> {
					destRepo.findVersions(groupId, artifactId).stream().forEach(version -> {
						sourceRepo.findArtifacts(groupId, artifactId, version).forEach(gav -> {
							if (!destRepo.contains(gav)) {
								System.out.println("Uploading: " + gav);
								destRepo.upload(gav, sourceRepo.read(gav));
							} else {
								System.out.println("Skipping: " + gav);
							}
						});
					});
				});
			}
		});
	}

}
