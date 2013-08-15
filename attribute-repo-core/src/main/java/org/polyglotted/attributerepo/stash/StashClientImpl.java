package org.polyglotted.attributerepo.stash;

import static org.polyglotted.attributerepo.git.common.GitConstants.HTTP_SCHEME;
import static org.polyglotted.attributerepo.git.common.GitConstants.URL_PREFIX;

import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.polyglotted.attributerepo.core.Response;
import org.polyglotted.attributerepo.git.common.AbstractGitClient;
import org.polyglotted.attributerepo.git.common.DefaultResponse;

/**
 * A Atlassian Stash implementation of Git client
 * 
 * @author Shankar Vasudevan
 */
public class StashClientImpl extends AbstractGitClient {

    /**
     * Create a new StashClientImpl
     * 
     * @param props
     *            the properties to get the credentials and connection details
     * @return a GitClient implementation
     */
    public static StashClientImpl create(Properties props) {
        StashClientImpl client = new StashClientImpl(createTargetHost(props, "localhost", 7990, HTTP_SCHEME));
        client.acquireProxySettings(props);
        client.enableSelfCertification(props);
        client.acquireCredentials(props);

        return client;
    }

    protected StashClientImpl(HttpHost targetHost) {
        super(targetHost);
        clientProps.put(URL_PREFIX, "");
    }

    @Override
    protected Response decorateResponse(HttpResponse response) {
        return new DefaultResponse(response);
    }
}
