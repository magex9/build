package ca.magex.ttl.products;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import ca.magex.maven.repository.HttpMavenRepository;
import static org.junit.Assert.*;

public class HttpMavenRepositoryTest {
	
	private HttpMavenRepository repo = new HttpMavenRepository("central", 
			"https://repo1.maven.org/maven2");
	
	@Test
	public void testDirectories() throws IOException {
		String url = "https://repo1.maven.org/maven2/ant/ant/";
		List<String> directories = repo.directories(url)
			.stream()
			.filter(item -> item.equals("1.5.1"))
			.collect(Collectors.toList());
		assertTrue(directories.size() == 1);
	}
	
	@Test
	public void testGetALlArtifacts() {
		//https://repo1.maven.org/maven2/org/apache/maven/maven-artifact/3.6.3/maven-artifact-3.6.3.jar
		repo.findArtifacts("org.apache.maven", "maven-artifact", "3.6.3").stream().forEach(gav -> {
			System.out.println(gav);
		});
	}
	
}