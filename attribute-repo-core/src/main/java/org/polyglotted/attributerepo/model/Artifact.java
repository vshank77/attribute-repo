package org.polyglotted.attributerepo.model;

import static com.google.common.base.Preconditions.checkNotNull;
import lombok.Data;

/**
 * Artifact represents the subsystem or service that is deployed in an environment
 * 
 * @author vasudesh
 */
@Data
public class Artifact implements java.io.Serializable {

    private static final long serialVersionUID = -3996133728755226428L;
    private static final String SEGMENT_REF_TAGS = "refs/tags/";

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String environment;

    /**
     * Create the default file path for the given artifact property file
     * 
     * @return String representing the file location for the given artifact
     */
    public String buildFilePath(String fileName) {
        StringBuilder builder = new StringBuilder();
        builder.append(checkNotNull(getGroupId()).replace('.', '/'));
        builder.append("/");
        builder.append(checkNotNull(getArtifactId()));
        builder.append("/");
        builder.append(checkNotNull(getEnvironment()));
        builder.append(fileName);

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
     * @return true if the version is null or ends with -SNAPSHOT
     */
    public boolean isSnapshot() {
        return version == null || version.endsWith("-SNAPSHOT");
    }
}
