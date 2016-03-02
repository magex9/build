package ca.magex.maven.repository;

import ca.magex.maven.exceptions.BuilderException;
import ca.magex.maven.model.MavenRepository;

public class MavenRepositoryFactory {

	public static MavenRepository createMavenRepository(String conn) {
		if (conn == null) {
			throw new BuilderException("Connection string cannot be null");
		} else if (conn.startsWith("http://")) {
			return new HttpMavenRepository();
		} else if (conn.startsWith("file://")) {
			return new FilesystemMavenRepository(conn.substring("file://".length()));
		}
		throw new BuilderException("Unable to create maven repoistory with connection string: " + conn);
	}
	
}
