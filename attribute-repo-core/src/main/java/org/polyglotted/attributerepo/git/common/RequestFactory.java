package org.polyglotted.attributerepo.git.common;

import static org.polyglotted.attributerepo.core.AttribRepoProperties.*;

import java.util.Properties;

import org.polyglotted.attributerepo.model.Artifact;
import org.polyglotted.attributerepo.model.RepoId;

/**
 * A factory object for creating default model objects
 * 
 * @author Shankar Vasudevan
 */
public class RequestFactory {

    /**
     * Create a RepoId from "repo.user" and "repo.name" mandatory property names
     * 
     * @param props
     *            the properties to query
     * @return the RepoId
     */
    public static RepoId createRepo(Properties props) {
        return new RepoId(notNullProperty(props, REPO_USER), notNullProperty(props, REPO_NAME));
    }

    /**
     * Create an Artifact from "groupId", "artifactId", "version" and "environment" property names. The groupId,
     * artifactId and version are mandatory property names and cannot be null.
     * 
     * @param props
     *            the properties to query
     * @return the Artifact
     */
    public static Artifact createArtifact(Properties props) {
        return new Artifact(notNullProperty(props, GROUP_ID), notNullProperty(props, ARTIFACT_ID),
                props.getProperty(VERSION), notNullProperty(props, ENVIRONMENT));
    }

    /**
     * Create an Artifact from "global.groupId", "global.artifactId", "global.version" and "global.environment" property
     * names. The global.groupId and global.artifactId are mandatory property names. For environment, one of the
     * global.environment or environment property names is mandatory.
     * 
     * @param props
     *            the properties to query
     * @return the Artifact
     */
    public static Artifact createGlobalArtifact(Properties props) {
        return new Artifact(notNullProperty(props, GLOBAL_GROUP_ID), notNullProperty(props, GLOBAL_ARTIFACT_ID),
                props.getProperty(GLOBAL_VERSION), notNullProperty(props, GLOBAL_ENVIRONMENT, ENVIRONMENT));
    }
}
