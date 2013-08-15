package org.polyglotted.attributerepo.model;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Artifact represents the subsystem or service that is deployed in an environment
 * 
 * @author vasudesh
 */
public class Artifact implements java.io.Serializable {

    private static final long serialVersionUID = -3996133728755226428L;
    private static final String SEGMENT_REF_TAGS = "refs/tags/";

    private String groupId;
    private String artifactId;
    private String version;
    private String environment;

    /**
     * Create a new Artifact
     */
    public Artifact() {}

    /**
     * Create a new Artifact
     * 
     * @param groupId
     *            the String representing groupId
     * @param artifactId
     *            the String representing artifactId
     * @param version
     *            the String representing version, can be null or a SNAPSHOT
     * @param environment
     *            the environment to pick the properties for
     */
    public Artifact(String groupId, String artifactId, String version, String environment) {
        setGroupId(groupId);
        setArtifactId(artifactId);
        setVersion(version);
        setEnvironment(environment);
    }

    /**
     * Create the default file path for the given artifact property file
     * 
     * @return String representing the file location for the given artifact
     */
    public String buildFilePath() {
        StringBuilder builder = new StringBuilder();
        builder.append(checkNotNull(getGroupId()).replace('.', '/'));
        builder.append("/");
        builder.append(checkNotNull(getArtifactId()));
        builder.append("/");
        builder.append(checkNotNull(getEnvironment()));
        builder.append(".properties");

        return builder.toString();
    }

    /**
     * Create the default tag reference for the given artifact (assumes /refs/tags)
     * 
     * @return the String representing tag reference
     */
    public String buildTagRef() {
        StringBuilder builder = new StringBuilder();
        builder.append(SEGMENT_REF_TAGS);
        builder.append(checkNotNull(getGroupId()));
        builder.append("/");
        builder.append(checkNotNull(getArtifactId()));
        builder.append("/");
        builder.append(checkNotNull(getVersion()));

        return builder.toString();
    }

    /**
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId
     *            the String representing groupId
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the artifactId
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * @param artifactId
     *            the String representing artifactId
     */
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *            the String representing version, can be null or a SNAPSHOT
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the environment
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * @param environment
     *            the environment to pick the properties for
     */
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    /**
     * @return true if the version is null or ends with -SNAPSHOT
     */
    public boolean isSnapshot() {
        return version == null || version.endsWith("-SNAPSHOT");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
        result = prime * result + ((environment == null) ? 0 : environment.hashCode());
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
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

        Artifact other = (Artifact) obj;
        if ((groupId == null) ? (other.groupId != null) : !groupId.equals(other.groupId))
            return false;
        if ((artifactId == null) ? (other.artifactId != null) : !artifactId.equals(other.artifactId))
            return false;
        if ((version == null) ? (other.version != null) : !version.equals(other.version))
            return false;
        if ((environment == null) ? (other.environment != null) : !environment.equals(other.environment))
            return false;

        return true;
    }
}
