package ca.magex.maven.repository;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import ca.magex.maven.model.Gav;
import ca.magex.maven.repository.FilesystemMavenRepository;

public class MavenCentralRepositoryTest {

	public static void main(String[] args) throws Exception {
		//ApacheMavenRepository repo = new ApacheMavenRepository();
		MavenRepository repo = new FilesystemMavenRepository(new File("/Users/magex/workspace/maven/local-repo"));
		//Gav gav = repo.findArtifact("org.apache.maven.plugin", "maven-resource-plugin", "2.79");
		//System.out.println(gav);
		Gav gav = new Gav("org.apache.maven.plugins", "maven-resources-plugin", "2.7", "jar", "tests");
		//System.out.println(repo.contains(gav));
		
		for (Gav version : repo.findArtifacts(gav.getGroupId(), gav.getArtifactId(), gav.getVersion())) {
			System.out.println(version);
		}
		
		//Gav pom = new Gav("org.apache.maven.plugins", "maven-resources-plugin", "2.7", "pom");
		Gav pom = new Gav("org.apache.maven", "maven", "3.3.9", "pom");
		String content = repo.content(pom);
		System.out.println(content);
		
		MavenXpp3Reader reader = new MavenXpp3Reader();
		Model model = reader.read(new InputStreamReader(repo.read(pom)));
		
		System.out.println(model.getGroupId());
		System.out.println(model.getArtifactId());
		System.out.println(model.getVersion());
		
		Properties properties = model.getProperties();
		for (Object key : properties.keySet()) {
			System.out.println();
			System.out.println(key + " = " + properties.getProperty(key.toString()));
		}
		
		for (Dependency dependency : model.getDependencies()) {
			System.out.println();
			System.out.println("  " + dependency.getGroupId());
			System.out.println("  " + dependency.getArtifactId());
			System.out.println("  " + dependency.getVersion());
		}
		
	}
	
}
