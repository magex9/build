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

	private final String group;

	private final String artifact;

	private final String version;

	private final String packaging;

	private final String classifier;

	/**
	 * Create an exact copy of a given gav.
	 * 
	 * @param gav
	 */
	public Gav(Gav gav) {
		this(gav.group, gav.artifact, gav.version, gav.packaging, gav.classifier);
	}

	/**
	 * Create a gav with only the required fields.
	 * 
	 * @param group
	 * @param artifact
	 * @param version
	 */
	public Gav(String group, String artifact, String version) {
		this(group, artifact, version, null, null);
	}

	/**
	 * Create a gav with the required fields and different packaging
	 * 
	 * @param group
	 * @param artifact
	 * @param version
	 * @param packaging
	 */
	public Gav(String group, String artifact, String version, String packaging) {
		this(group, artifact, version, packaging, null);
	}

	/**
	 * Create a gav with all the required parameters.
	 * 
	 * @param group
	 * @param artifact
	 * @param version
	 * @param packaging
	 * @param classifier
	 */
	public Gav(String group, String artifact, String version, String packaging, String classifier) {
		super();
		this.group = group;
		this.artifact = artifact;
		this.version = version;
		this.packaging = StringUtils.isBlank(packaging) ? "jar" : packaging;
		this.classifier = StringUtils.isBlank(classifier) ? null : classifier;
	}

	/**
	 * Get the group identifier
	 * 
	 * @return
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * Get the artifact identifier
	 * 
	 * @return
	 */
	public String getArtifact() {
		return artifact;
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
	 * @param artifact
	 * @return
	 */
	public Gav withArtifact(String artifact) {
		return withArtifact(artifact, null, null);
	}

	/**
	 * Get a new gav with the updated artifact and packaging types
	 * 
	 * @param artifact
	 * @param packaging
	 * @return
	 */
	public Gav withArtifact(String artifact, String packaging) {
		return withArtifact(artifact, packaging, null);
	}

	/**
	 * Get a new gav with the updated artifact, packaging and classifier
	 * 
	 * @param artifact
	 * @param packaging
	 * @param classifier
	 * @return
	 */
	public Gav withArtifact(String artifact, String packaging, String classifier) {
		return new Gav(group, artifact, version, packaging, classifier);
	}

	/**
	 * Get a new gav with the updated version
	 * 
	 * @param version
	 * @return
	 */
	public Gav withVersion(String version) {
		return new Gav(group, artifact, version, packaging, classifier);
	}

	/**
	 * Get a new gav with an updated packaging type
	 * 
	 * @param packaging
	 * @return
	 */
	public Gav withPackaging(String packaging) {
		return new Gav(group, artifact, version, packaging, classifier);
	}

	/**
	 * Get a new gav with an updated classifier
	 * 
	 * @param classifier
	 * @return
	 */
	public Gav withClassifier(String classifier) {
		return new Gav(group, artifact, version, packaging, classifier);
	}

	/**
	 * Get the filename representation of this gav as it would exist in the
	 * repository
	 * 
	 * @return
	 */
	public String getFilename() {
		StringBuilder sb = new StringBuilder();
		sb.append(artifact);
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
		sb.append(group);
		sb.append(":");
		sb.append(artifact);
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
		result = prime * result + ((artifact == null) ? 0 : artifact.hashCode());
		result = prime * result + ((classifier == null) ? 0 : classifier.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
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
		if (artifact == null) {
			if (other.artifact != null)
				return false;
		} else if (!artifact.equals(other.artifact))
			return false;
		if (classifier == null) {
			if (other.classifier != null)
				return false;
		} else if (!classifier.equals(other.classifier))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
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
