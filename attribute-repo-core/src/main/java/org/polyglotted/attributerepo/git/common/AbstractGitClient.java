package org.polyglotted.attributerepo.git.common;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.http.conn.params.ConnRoutePNames.DEFAULT_PROXY;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.AUTH_PASSPHRASE;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.AUTH_PASSWORD;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.AUTH_USERNAME;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.GIT_HOST_NAME;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.GIT_HOST_PORT;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.GIT_HOST_SCHEME;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.OAUTH2_TOKEN;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.PROXY_HOST;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.PROXY_PASSWORD;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.PROXY_PORT;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.PROXY_USERNAME;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.TRUST_SELFSIGNED;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.USE_BASIC_AUTH;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.USE_PROXY;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.getIntProperty;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.isFalse;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.notNullProperty;
import static org.polyglotted.attributerepo.git.common.GitConstants.AUTH_TOKEN;
import static org.polyglotted.attributerepo.git.common.GitConstants.BASIC_AUTH;
import static org.polyglotted.attributerepo.git.common.GitUtils.toBase64;

import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.polyglotted.attributerepo.core.GitClient;
import org.polyglotted.attributerepo.core.Request;
import org.polyglotted.attributerepo.core.Response;
import org.polyglotted.crypto.symmetric.AesDecrypter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;

/**
 * An abstract implementation for GitClient that sets up some of the basic requirements for connecting to a GIT REST
 * service
 * 
 * @author Shankar Vasudevan
 */
public abstract class AbstractGitClient implements GitClient {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGitClient.class);

    protected final DefaultHttpClient httpClient = new DefaultHttpClient(new PoolingClientConnectionManager());
    protected final Map<String, String> clientProps = Maps.newHashMap();
    protected final HttpHost targetHost;

    protected AbstractGitClient(HttpHost targetHost) {
        this.targetHost = targetHost;
    }

    @Override
    public final <R> Response execute(Request<R> request) {
        try {
            checkArgument(request instanceof AbstractRequest, "unknown request class {}", request.getClass());

            HttpUriRequest httpRequest = ((AbstractRequest<R>) request).createUriRequest(clientProps);
            return decorateResponse(httpClient.execute(targetHost, httpRequest));

        }
        catch (RuntimeException re) {
            logger.error("error in github execution", re);
            throw re;
        }
        catch (Exception ex) {
            logger.error("error in github execution", ex);
            throw new RuntimeException("github execution failed", ex);
        }
    }

    protected abstract Response decorateResponse(HttpResponse response);

    @Override
    public void destroy() {
        httpClient.getConnectionManager().shutdown();
    }

    protected void acquireProxySettings(Properties props) {
        boolean dontUseProxy = isFalse(props, USE_PROXY, "false");
        if (dontUseProxy)
            return;

        String proxyHost = notNullProperty(props, PROXY_HOST);
        int proxyPort = getIntProperty(props, PROXY_PORT, 8080);

        httpClient.getCredentialsProvider().setCredentials(new AuthScope(proxyHost, proxyPort),
                new UsernamePasswordCredentials(props.getProperty(PROXY_USERNAME), props.getProperty(PROXY_PASSWORD)));
        httpClient.getParams().setParameter(DEFAULT_PROXY, new HttpHost(proxyHost, proxyPort));
    }

    protected void enableSelfCertification(Properties props) {
        boolean dontTrustSelfSigned = isFalse(props, TRUST_SELFSIGNED, "false");
        if (dontTrustSelfSigned)
            return;

        try {
            Scheme trustSelfSignedCerts = new Scheme("https", 443, new SSLSocketFactory(new TrustSelfSignedStrategy(),
                    new AllowAllHostnameVerifier()));
            httpClient.getConnectionManager().getSchemeRegistry().register(trustSelfSignedCerts);

        }
        catch (Exception ignore) {}
    }

    protected void acquireCredentials(Properties props) {
        boolean dontUseBasicAuth = isFalse(props, USE_BASIC_AUTH, "false");
        if (dontUseBasicAuth) {
            clientProps.put(GitConstants.CREDENTIALS, AUTH_TOKEN + notNullProperty(props, OAUTH2_TOKEN));
            return;
        }

        String authUsername = notNullProperty(props, AUTH_USERNAME);
        String authPassword = AesDecrypter.decrypt(notNullProperty(props, AUTH_PASSPHRASE),
                notNullProperty(props, AUTH_PASSWORD));

        clientProps.put(GitConstants.CREDENTIALS, BASIC_AUTH + toBase64(authUsername + ":" + authPassword));
    }

    protected static HttpHost createTargetHost(Properties props, String defHost, int defPort, String defScheme) {
        String hostName = props.getProperty(GIT_HOST_NAME, defHost);
        int port = getIntProperty(props, GIT_HOST_PORT, defPort);
        String scheme = props.getProperty(GIT_HOST_SCHEME, defScheme);

        return new HttpHost(hostName, port, scheme);
    }

    @VisibleForTesting
    public DefaultHttpClient httpClient() {
        return httpClient;
    }

    @VisibleForTesting
    public HttpHost targetHost() {
        return targetHost;
    }

    @VisibleForTesting
    public Map<String, String> clientProps() {
        return clientProps;
    }
}
