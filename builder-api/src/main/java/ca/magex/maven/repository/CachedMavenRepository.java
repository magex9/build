package ca.magex.maven.repository;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.magex.maven.model.Gav;

/**
 * A caching wrapper for any repository to cache the information from the finder
 * methods. This can be used to cache the results of many requests as they
 * should be fairly static.
 * 
 * @author magex
 *
 */
@SuppressWarnings("unchecked")
public class CachedMavenRepository implements MavenRepository {

	private MavenRepository repo;

	private Map<String, Object> cache;

	public CachedMavenRepository(MavenRepository repo) {
		this.repo = repo;
		this.cache = new HashMap<String, Object>();
	}

	public void refresh() {
		cache = new HashMap<String, Object>();
	}

	private Object cache(String key, Object value) {
		if (!cache.containsKey(key))
			cache.put(key, value);
		return value;
	}
	
	public List<String> findAllGroups() {
		String key = "findAllGroups()";
		return (List<String>)cache(key, repo.findAllGroups());
	}
	
	public List<String> findRootGroups() {
		String key = "findRootGroups()";
		return (List<String>)cache(key, repo.findRootGroups());
	}

	public List<String> findChildGroups(String group) {
		String key = "findChildGroups(" + group + ")";
		return (List<String>)cache(key, repo.findChildGroups(group));
	}

	public boolean isGroup(String group) {
		String key = "isGroup(" + group + ")";
		return (Boolean)cache(key, repo.isGroup(group));
	}

	public List<String> findArtifacts(String group) {
		String key = "findArtifacts(" + group + ")";
		return (List<String>) cache(key, repo.findArtifacts(group));
	}

	public List<String> findVersions(String group, String artifact) {
		String key = "findVersions(" + group + "," + artifact + ")";
		return (List<String>) cache(key, repo.findVersions(group, artifact));
	}

	public Gav findPom(String group, String artifact, String version) {
		String key = "findPom(" + group + "," + artifact + "," + version + ")";
		return (Gav) cache(key, repo.findPom(group, artifact, version));
	}

	public Gav findArtifact(String group, String artifact, String version) {
		String key = "findArtifact(" + group + "," + artifact + "," + version + ")";
		return (Gav) cache(key, repo.findArtifact(group, artifact, version));
	}

	public List<Gav> findArtifacts(String group, String artifact, String version) {
		String key = "findArtifacts(" + group + "," + artifact + "," + version + ")";
		return (List<Gav>) cache(key, repo.findArtifacts(group, artifact, version));
	}

	public boolean contains(Gav gav) {
		String key = "contains(" + gav + ")";
		return (Boolean) cache(key, repo.contains(gav));
	}

	public void download(Gav gav, File file) {
		repo.download(gav, file);
	}

	public InputStream read(Gav gav) {
		return (InputStream) cache.get("read");
	}

	public String content(Gav gav) {
		return repo.content(gav);
	}

	public String getBaseUrl() {
		return repo.getBaseUrl();
	}

	public String getRepo() {
		return repo.getRepo();
	}

}
