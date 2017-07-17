package ca.magex.maven.repository;

import java.io.File;

import org.apache.commons.io.FileUtils;

import ca.magex.maven.model.Gav;

public class MavenRepositorySyncronizer {

	public static void main(String[] args) throws Exception {
		MavenRepository src = new HttpMavenRepository("central", "http://central.maven.org/maven2/");
		FileUtils.forceMkdir(new File("/Users/magex/workspace/maven/sync-repo/"));
		FilesystemMavenRepository dest = new FilesystemMavenRepository("/Users/magex/workspace/maven/sync-repo/");
		//download(src, dest, "ant", "1.5.1");
	}
	
	public static void download(MavenRepository src, FilesystemMavenRepository dest) {
		MavenRepository repo = new FilesystemMavenRepository("/Users/magex/workspace/maven/local-repo/");
		repo.findAllGroupIds().stream().forEach(groupId -> {
			repo.findArtifactIds(groupId).stream().forEach(artifactId -> {
				repo.findVersions(groupId, artifactId).stream().forEach(version -> {
					repo.findArtifacts(groupId, artifactId, version).forEach(gav -> {
						dest.upload(gav, src.read(gav));
					});
				});
			});
		});
	}
	
}
