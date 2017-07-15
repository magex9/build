package ca.magex.ttl.products;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import ca.magex.maven.repository.HttpMavenRepository;
import static org.junit.Assert.*;

public class HttpMavenRepositoryTest {
	
	private HttpMavenRepository repo = new HttpMavenRepository("central", 
			"http://central.maven.org/maven2");
	
	@Test
	public void testDirectories() {
		String url = "http://central.maven.org/maven2/ant/ant/";
		List<String> directories = repo.directories(url)
			.stream()
			.filter(item -> item.equals("1.5.1"))
			.collect(Collectors.toList());
		assertTrue(directories.size() == 1);
	}
	
}
