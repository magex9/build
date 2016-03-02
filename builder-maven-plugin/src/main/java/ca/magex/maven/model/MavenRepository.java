package ca.magex.maven.model;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

/**
 * The maven repository interface gives the ability to query and gain access to
 * all the information needed by a project.
 * 
 * Multiple implementations of this exist for different types of maven
 * repositories such as local filesystem, apache httpd directories, nexus,
 * artifactory, etc.
 * 
 * @author magex
 *
 */
public interface MavenRepository {

	/**
	 * Find all of the group id's in the system
	 * 
	 * @return
	 */
	List<String> findAllGroupIds();

	/**
	 * Get a list of all the group directory names under the given base
	 * directory.
	 * 
	 * @param baseDir
	 * @return
	 */
	List<String> findDirectories(String baseDir);

	/**
	 * Find all of the artifact id's for a given group
	 * 
	 * @param groupId
	 * @return
	 */
	List<String> findArtifactIds(String groupId);

	/**
	 * Find all of the versions for a given group and artifact
	 * 
	 * @param groupId
	 * @param artifactId
	 * @return
	 */
	List<String> findVersions(String groupId, String artifactId);

	/**
	 * Get the pom gav for a given group, artifact and version.
	 * 
	 * @param groupId
	 * @param artifactId
	 * @param version
	 * @return
	 */
	Gav findPom(String groupId, String artifactId, String version);

	/**
	 * Get the artifact gav for a given group, artifact and version
	 * 
	 * @param groupId
	 * @param artifactId
	 * @param version
	 * @return
	 */
	Gav findArtifact(String groupId, String artifactId, String version);

	/**
	 * Get a list of all the extra classifier artifacts for a given group,
	 * artifact and version
	 * 
	 * @param groupId
	 * @param artifactId
	 * @param version
	 * @return
	 */
	List<Gav> findClassifiers(String groupId, String artifactId, String version);

	/**
	 * Check to see if this repository contains the given gav
	 * 
	 * @param pom
	 * @return
	 */
	boolean contains(Gav gav);

	/**
	 * Downloads an artifact to the specified file.
	 * 
	 * @param gav
	 * @param file
	 */
	void download(Gav gav, File file);

	/**
	 * Get an output stream of a specified gav
	 * 
	 * @param gav
	 * @return
	 */
	OutputStream read(Gav gav);

	/**
	 * Get the root url for the implementation
	 * 
	 * @return
	 */
	String getUrl();

	/**
	 * Get the name of this repository
	 * 
	 * @return
	 */
	String getRepoId();

}
