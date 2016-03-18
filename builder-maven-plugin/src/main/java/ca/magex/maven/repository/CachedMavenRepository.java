package ca.magex.maven.repository;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.magex.maven.model.Gav;
import ca.magex.maven.model.MavenRepository;

/**
 * A caching wrapper for any repository to cache the information 
 * from the find methods
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

	public List<String> findAllGroupIds() {
		if (!cache.containsKey("findAllGroupIds"))
			cache.put("findAllGroupIds", repo.findAllGroupIds());
		return (List<String>)cache.get("findAllGroupIds");
	}

	public List<String> findArtifactIds(String groupId) {
		if (!cache.containsKey("findArtifactIds"))
			cache.put("findArtifactIds", repo.findArtifactIds(groupId));
		return (List<String>)cache.get("findArtifactIds");
	}

	public List<String> findVersions(String groupId, String artifactId) {
		if (!cache.containsKey("findVersions"))
			cache.put("findVersions", repo.findVersions(groupId, artifactId));
		return (List<String>)cache.get("findVersions");
	}

	public Gav findPom(String groupId, String artifactId, String version) {
		if (!cache.containsKey("findPom"))
			cache.put("findPom", repo.findPom(groupId, artifactId, version));
		return (Gav)cache.get("findPom");
	}

	public Gav findArtifact(String groupId, String artifactId, String version) {
		if (!cache.containsKey("findArtifact"))
			cache.put("findArtifact", repo.findArtifact(groupId, artifactId, version));
		return (Gav)cache.get("findArtifact");
	}

	public List<Gav> findArtifacts(String groupId, String artifactId, String version) {
		if (!cache.containsKey("findArtifacts"))
			cache.put("findArtifacts", repo.findArtifacts(groupId, artifactId, version));
		return (List<Gav>)cache.get("findArtifacts");
	}

	public boolean contains(Gav gav) {
		if (!cache.containsKey("contains"))
			cache.put("contains", repo.contains(gav));
		return (Boolean)cache.get("contains");
	}

	public void download(Gav gav, File file) {
		repo.download(gav, file);
	}

	public InputStream read(Gav gav) {
		return (InputStream)cache.get("read");
	}

	public String getBaseUrl() {
		return repo.getBaseUrl();
	}

	public String getRepoId() {
		return repo.getRepoId();
	}
	
}
