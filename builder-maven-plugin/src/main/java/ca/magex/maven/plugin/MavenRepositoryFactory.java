package ca.magex.maven.plugin;

import ca.magex.maven.repository.FilesystemMavenRepository;
import ca.magex.maven.repository.HttpMavenRepository;
import ca.magex.maven.repository.MavenRepository;

public class MavenRepositoryFactory {

	public static MavenRepository createMavenRepository(String conn) {
		if (conn == null) {
			throw new RuntimeException("Connection string cannot be null");
		} else if (conn.startsWith("http://")) {
			return new HttpMavenRepository("http", conn);
		} else if (conn.startsWith("file://")) {
			return new FilesystemMavenRepository(conn.substring("file://".length()));
		}
		throw new RuntimeException("Unable to create maven repoistory with connection string: " + conn);
	}

}
