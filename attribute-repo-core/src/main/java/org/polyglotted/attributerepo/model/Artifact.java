package org.polyglotted.attributerepo.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.polyglotted.attributerepo.git.common.GitConstants.DEFAULT_FILE;
import static org.polyglotted.attributerepo.git.common.GitConstants.FILE_PREFIX;
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
    private String fileName = DEFAULT_FILE;

    /**
     * Create the default file path for the given artifact property file
     * 
     * @return String representing the file location for the given artifact
     */
    public String buildFilePath() {
        StringBuilder builder = new StringBuilder();
        builder.append(checkNotNull(groupId).replace('.', '/'));
        builder.append("/");
        builder.append(checkNotNull(artifactId));
        builder.append("/");
        builder.append(checkNotNull(environment));
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
        builder.append(checkNotNull(groupId));
        builder.append("/");
        builder.append(checkNotNull(artifactId));
        builder.append("/");
        builder.append(checkNotNull(version));

        return builder.toString();
    }

    /**
     * @return true if the version is null or ends with -SNAPSHOT
     */
    public boolean isSnapshot() {
        return version == null || version.endsWith("-SNAPSHOT");
    }

    public Artifact setFileName(String fileName) {
        this.fileName = FILE_PREFIX + fileName;
        return this;
    }
}
