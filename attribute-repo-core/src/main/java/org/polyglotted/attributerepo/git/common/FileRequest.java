package org.polyglotted.attributerepo.git.common;

import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.polyglotted.attributerepo.model.Artifact;
import org.polyglotted.attributerepo.model.RepoId;

public abstract class FileRequest<R> extends AbstractRequest<R> {

    protected final RepoId repo;
    protected final Artifact artifact;
    private String overrideTagRef = null;

    /**
     * Create a new FileRequest
     * 
     * @param repo
     *            the repository definition
     * @param artifact
     *            the deployed artifact that you would like to get the properties
     */
    public FileRequest(RepoId repo, Artifact artifact) {
        this.repo = repo;
        this.artifact = artifact;
    }

    /**
     * Set an override for the tag reference to fetch. By default this request builds a reference of the format
     * groupId/artifactId/version from the artifact.
     * 
     * @param overrideTagRef
     */
    public void setOverrideTagRef(String overrideTagRef) {
        this.overrideTagRef = overrideTagRef;
    }

    @Override
    public HttpUriRequest createUriRequest(Map<String, String> clientProps) {
        setUri(buildUri());
        if (!artifact.isSnapshot())
            addParam(getRefParam(), overrideTagRef != null ? overrideTagRef : artifact.buildTagRef());

        return super.createUriRequest(clientProps);
    }

    @Override
    protected HttpUriRequest createBaseRequest(String uri) {
        return new HttpGet(uri);
    }

    protected abstract String getRefParam();

    protected abstract StringBuilder buildUri();
}
