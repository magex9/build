package ca.magex.maven.repository;

import org.junit.Test;

import ca.magex.maven.model.Gav;
import ca.magex.maven.model.MavenRepository;

public class FilesystemMavenRepositoryTest {

	public static final String LOCAL_REPO = "/Users/magex/workspace/maven/local-repo";
	
	@Test
	public void testGetGroupIds() throws Exception {
		MavenRepository repo = new FilesystemMavenRepository(LOCAL_REPO);
		for (String artifacts : repo.findVersions("org.apache.maven.plugins", "maven-jar-plugin")) {
			System.out.println(artifacts);
		}
		for (Gav artifacts : repo.findArtifacts("org.apache.maven.plugins", "maven-jar-plugin", "2.6")) {
			System.out.println(artifacts);
		}
	}
	
}
