package ca.magex.maven.repository;

import ca.magex.maven.exceptions.MavenException;

public class MavenRepositoryFactory {

	public static MavenRepository createMavenRepository(String conn) {
		if (conn == null) {
			throw new MavenException("Connection string cannot be null");
		} else if (conn.startsWith("http://")) {
			return new HttpMavenRepository("http", conn);
		} else if (conn.startsWith("file://")) {
			return new FilesystemMavenRepository(conn.substring("file://".length()));
		}
		throw new MavenException("Unable to create maven repoistory with connection string: " + conn);
	}
	
}