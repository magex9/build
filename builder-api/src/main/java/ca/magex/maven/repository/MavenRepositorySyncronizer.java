package ca.magex.maven.repository;

import java.io.File;

import org.apache.commons.io.FileUtils;

import ca.magex.maven.model.Gav;

public class MavenRepositorySyncronizer {

	public static void main(String[] args) throws Exception {
		MavenRepository src = new HttpMavenRepository("central", "http://central.maven.org/maven2/");
		MavenRepository reference = new FilesystemMavenRepository("/Users/magex/workspace/maven/local-repo/");
		FileUtils.forceMkdir(new File("/Users/magex/workspace/maven/sync-repo/"));
		File dest = new File("/Users/magex/workspace/maven/sync-repo/");
		syncronize(reference, src, dest);
	}
	
	public static void syncronize(MavenRepository reference, MavenRepository src, File dest) {
		FilesystemMavenRepository out = new FilesystemMavenRepository(dest);
		reference.findAllGroups().stream().forEach(group -> {
			reference.findArtifacts(group).stream().forEach(artifact -> {
				reference.findVersions(group, artifact).stream().forEach(version -> {
					src.findArtifacts(group, artifact, version).forEach(gav -> {
						if (!out.contains(gav)) {
							System.out.println("Uploading: " + gav);
							out.upload(gav, src.read(gav));
						} else {
							System.out.println("Skipping: " + gav);
						}
					});
				});
			});
		});
	}
	
}
