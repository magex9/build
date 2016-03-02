package ca.magex.maven.repository;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

import ca.magex.maven.exceptions.BuilderException;
import ca.magex.maven.model.Gav;
import ca.magex.maven.model.MavenRepository;

public class FilesystemMavenRepository implements MavenRepository {

	private File basedir;
	
	public FilesystemMavenRepository(String basedir) {
		this(new File(basedir));
	}
	
	public FilesystemMavenRepository(File basedir) {
		if (!basedir.exists() || !basedir.isDirectory())
			throw new BuilderException("Base directory does not exist: " + basedir);
		this.basedir = basedir;
	}

	public List<String> findAllGroupIds() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> findGroupIds() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> findArtifactIds(String groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> findVersions(String groupId, String artifactId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Gav findPom(String groupId, String artifactId, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	public Gav findArtifact(String groupId, String artifactId, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Gav> findClassifiers(String groupId, String artifactId, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean contains(Gav pom) {
		// TODO Auto-generated method stub
		return false;
	}

	public void download(Gav gav, File file) {
		// TODO Auto-generated method stub
		
	}

	public OutputStream read(Gav gav) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRepoId() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
