package ca.magex.maven.repository;

public class MavenCentralRepository extends HttpMavenRepository {

	public MavenCentralRepository() {
		super("central", "https://repo1.maven.org/maven2");
	}

}
