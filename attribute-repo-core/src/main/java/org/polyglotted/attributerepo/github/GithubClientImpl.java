package org.polyglotted.attributerepo.github;

import static org.polyglotted.attributerepo.git.common.GitConstants.HTTPS_SCHEME;
import static org.polyglotted.attributerepo.git.common.GitConstants.URL_PREFIX;
import static org.polyglotted.attributerepo.github.GithubConstants.API_GITHUB_COM;
import static org.polyglotted.attributerepo.github.GithubConstants.SEGMENT_V3;

import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.polyglotted.attributerepo.core.Response;
import org.polyglotted.attributerepo.git.common.AbstractGitClient;

/**
 * A GitHub.com implementation of the Git client
 * 
 * @author Shankar Vasudevan
 */
public class GithubClientImpl extends AbstractGitClient {

    /**
     * Create a new GithubClientImpl
     * 
     * @param props
     *            the properties to get the credentials and connection details
     * @return a GitClient implementation
     */
    public static GithubClientImpl create(Properties props) {
        GithubClientImpl client = new GithubClientImpl(createTargetHost(props, API_GITHUB_COM, 443, HTTPS_SCHEME));
        client.acquireProxySettings(props);
        client.enableSelfCertification(props);
        client.acquireCredentials(props);

        return client;
    }

    protected GithubClientImpl(HttpHost targetHost) {
        super(targetHost);
        clientProps.put(URL_PREFIX, API_GITHUB_COM.equals(targetHost.getHostName()) ? "" : SEGMENT_V3);
    }

    @Override
    protected Response decorateResponse(HttpResponse response) {
        return new GithubResponse(response);
    }
}
