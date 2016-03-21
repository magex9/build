package ca.magex.maven.repository;

public class ApacheCentralMavenRepository extends HttpMavenRepository {

	public ApacheCentralMavenRepository() {
		super("central", "https://repo1.maven.org/maven2");
	}

}
