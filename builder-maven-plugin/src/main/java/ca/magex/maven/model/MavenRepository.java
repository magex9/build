package ca.magex.maven.model;

import java.io.File;
import java.io.InputStream;
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
	 * Get a list of all the artifacts for a given group, artifact and version.
	 * This list will include the pom, main artifact and any additional
	 * artifacts with classifiers.
	 * 
	 * @param groupId
	 * @param artifactId
	 * @param version
	 * @return
	 */
	List<Gav> findArtifacts(String groupId, String artifactId, String version);

	/**
	 * Check to see if this repository contains the given gav
	 * 
	 * @param gav
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
	 * Get an input stream of a specified gav
	 * 
	 * @param gav
	 * @return
	 */
	InputStream read(Gav gav);

	/**
	 * Get the text content from a specified gav
	 * @param gav
	 * @return
	 */
	String content(Gav gav);

	/**
	 * Get the root url for the implementation
	 * 
	 * @return
	 */
	String getBaseUrl();

	/**
	 * Get the name of this repository
	 * 
	 * @return
	 */
	String getRepoId();

}
