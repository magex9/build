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
	
	public List<String> findAllGroupIds() {
		String key = "findAllGroupIds()";
		return (List<String>)cache(key, repo.findAllGroupIds());
	}
	
	public List<String> findRootGroups() {
		String key = "findRootGroups()";
		return (List<String>)cache(key, repo.findRootGroups());
	}

	public List<String> findChildGroups(String group) {
		String key = "findChildGroups(" + group + ")";
		return (List<String>)cache(key, repo.findChildGroups(group));
	}

	public boolean isGroupId(String groupId) {
		String key = "isGroupId(" + groupId + ")";
		return (Boolean)cache(key, repo.isGroupId(groupId));
	}

	public List<String> findArtifactIds(String groupId) {
		String key = "findArtifactIds(" + groupId + ")";
		return (List<String>) cache(key, repo.findArtifactIds(groupId));
	}

	public List<String> findVersions(String groupId, String artifactId) {
		String key = "findVersions(" + groupId + "," + artifactId + ")";
		return (List<String>) cache(key, repo.findVersions(groupId, artifactId));
	}

	public Gav findPom(String groupId, String artifactId, String version) {
		String key = "findPom(" + groupId + "," + artifactId + "," + version + ")";
		return (Gav) cache(key, repo.findPom(groupId, artifactId, version));
	}

	public Gav findArtifact(String groupId, String artifactId, String version) {
		String key = "findArtifact(" + groupId + "," + artifactId + "," + version + ")";
		return (Gav) cache(key, repo.findArtifact(groupId, artifactId, version));
	}

	public List<Gav> findArtifacts(String groupId, String artifactId, String version) {
		String key = "findArtifacts(" + groupId + "," + artifactId + "," + version + ")";
		return (List<Gav>) cache(key, repo.findArtifacts(groupId, artifactId, version));
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

	public String getRepoId() {
		return repo.getRepoId();
	}

}
