package ca.magex.maven.model;

import org.apache.commons.lang3.StringUtils;

/**
 * A pointer reference to a maven artifact. The group, artifact and version are
 * mandatory elements. The packaging defaults to jar and the classifier is
 * blank.
 * 
 * @author magex
 *
 */
public class Gav {

	private String groupId;

	private String artifactId;

	private String version;

	private String packaging;

	private String classifier;

	/**
	 * Create an exact copy of a given gav.
	 * 
	 * @param gav
	 */
	public Gav(Gav gav) {
		this(gav.groupId, gav.artifactId, gav.version, gav.packaging, gav.classifier);
	}

	/**
	 * Create a gav with only the required fields.
	 * 
	 * @param groupId
	 * @param artifactId
	 * @param version
	 */
	public Gav(String groupId, String artifactId, String version) {
		this(groupId, artifactId, version, null, null);
	}

	/**
	 * Create a gav with the required fields and different packaging
	 * 
	 * @param groupId
	 * @param artifactId
	 * @param version
	 * @param packaging
	 */
	public Gav(String groupId, String artifactId, String version, String packaging) {
		this(groupId, artifactId, version, packaging, null);
	}

	/**
	 * Create a gav with all the required parameters.
	 * 
	 * @param groupId
	 * @param artifactId
	 * @param version
	 * @param packaging
	 * @param classifier
	 */
	public Gav(String groupId, String artifactId, String version, String packaging, String classifier) {
		super();
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.packaging = StringUtils.isBlank(packaging) ? "jar" : packaging;
		this.classifier = StringUtils.isBlank(classifier) ? null : classifier;
	}

	/**
	 * Get the group identifier
	 * 
	 * @return
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * Get the artifact identifier
	 * 
	 * @return
	 */
	public String getArtifactId() {
		return artifactId;
	}

	/**
	 * Get the version
	 * 
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Get the packaging type
	 * 
	 * @return
	 */
	public String getPackaging() {
		return packaging;
	}

	/**
	 * Get the classifier if required
	 * 
	 * @return
	 */
	public String getClassifier() {
		return classifier;
	}

	/**
	 * Get a new gav with the updated artifact id, but keeping the same group id
	 * and version.
	 * 
	 * @param artifactId
	 * @return
	 */
	public Gav withArtifactId(String artifactId) {
		return withArtifactId(artifactId, null, null);
	}

	/**
	 * Get a new gav with the updated artifact and packaging types
	 * 
	 * @param artifactId
	 * @param packaging
	 * @return
	 */
	public Gav withArtifactId(String artifactId, String packaging) {
		return withArtifactId(artifactId, packaging, null);
	}

	/**
	 * Get a new gav with the updated artifact, packaging and classifier
	 * 
	 * @param artifactId
	 * @param packaging
	 * @param classifier
	 * @return
	 */
	public Gav withArtifactId(String artifactId, String packaging, String classifier) {
		return new Gav(groupId, artifactId, version, packaging, classifier);
	}

	/**
	 * Get a new gav with the updated version
	 * 
	 * @param version
	 * @return
	 */
	public Gav withVersion(String version) {
		return new Gav(groupId, artifactId, version, packaging, classifier);
	}

	/**
	 * Get a new gav with an updated packaging type
	 * 
	 * @param packaging
	 * @return
	 */
	public Gav withPackaging(String packaging) {
		return new Gav(groupId, artifactId, version, packaging, classifier);
	}

	/**
	 * Get a new gav with an updated classifier
	 * 
	 * @param classifier
	 * @return
	 */
	public Gav withClassifier(String classifier) {
		return new Gav(groupId, artifactId, version, packaging, classifier);
	}

	/**
	 * Get the filename representation of this gav as it would exist in the
	 * repository
	 * 
	 * @return
	 */
	public String getFilename() {
		StringBuilder sb = new StringBuilder();
		sb.append(artifactId);
		sb.append("-");
		sb.append(version);
		if (classifier != null) {
			sb.append("-");
			sb.append(classifier);
		}
		sb.append(".");
		sb.append(packaging);
		return sb.toString();
	}

	/**
	 * The string representation of this gav. This string can be passed back
	 * into the constructor to make an exact copy of this gav.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(groupId);
		sb.append(":");
		sb.append(artifactId);
		sb.append(":");
		sb.append(version);
		sb.append(":");
		sb.append(packaging);
		if (classifier != null) {
			sb.append(":");
			sb.append(classifier);
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
		result = prime * result + ((classifier == null) ? 0 : classifier.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((packaging == null) ? 0 : packaging.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gav other = (Gav) obj;
		if (artifactId == null) {
			if (other.artifactId != null)
				return false;
		} else if (!artifactId.equals(other.artifactId))
			return false;
		if (classifier == null) {
			if (other.classifier != null)
				return false;
		} else if (!classifier.equals(other.classifier))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (packaging == null) {
			if (other.packaging != null)
				return false;
		} else if (!packaging.equals(other.packaging))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

}
