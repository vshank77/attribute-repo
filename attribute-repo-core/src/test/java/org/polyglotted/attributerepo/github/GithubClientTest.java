package org.polyglotted.attributerepo.github;

import static org.apache.http.conn.params.ConnRoutePNames.DEFAULT_PROXY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.polyglotted.attributerepo.TestUtils.load;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.AUTH_PASSPHRASE;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.AUTH_PASSWORD;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.AUTH_USERNAME;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.GIT_HOST_NAME;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.GIT_HOST_PORT;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.GIT_HOST_SCHEME;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.PROXY_HOST;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.PROXY_PASSWORD;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.PROXY_PORT;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.PROXY_USERNAME;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.TRUST_SELFSIGNED;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.USE_BASIC_AUTH;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.USE_PROXY;
import static org.polyglotted.attributerepo.git.common.GitConstants.CREDENTIALS;
import static org.polyglotted.attributerepo.git.common.GitConstants.URL_PREFIX;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createArtifact;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createRepo;
import static org.polyglotted.attributerepo.github.GithubConstants.SEGMENT_V3;

import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

public class GithubClientTest {

    @Test
    public void testTargetHost() {
        String hostName = "mygithub.test.com";
        String port = "8443";
        String scheme = "http";

        Properties props = load("files/unit-test.properties");
        props.put(GIT_HOST_NAME, hostName);
        props.put(GIT_HOST_PORT, port);
        props.put(GIT_HOST_SCHEME, scheme);
        GithubClientImpl impl = GithubClientImpl.create(props);
        HttpHost targetHost = impl.targetHost();

        assertEquals(hostName, targetHost.getHostName());
        assertEquals(8443, targetHost.getPort());
        assertEquals(scheme, targetHost.getSchemeName());
    }

    @Test
    public void testApiPrefix() {
        String hostName = "mygithub.test.com";
        Properties props = load("files/unit-test.properties");
        props.put(GIT_HOST_NAME, hostName);
        GithubClientImpl impl = GithubClientImpl.create(props);
        assertEquals(SEGMENT_V3, impl.clientProps().get(URL_PREFIX));
    }

    @Test
    public void testCredentials() {
        Properties props = load("files/unit-test.properties");
        props.put(USE_BASIC_AUTH, "true");
        props.put(AUTH_USERNAME, "testuser");
        props.put(AUTH_PASSWORD, "880817b1c2811f317d30cebbf1425a4f$d274bef5797541f82f54b9061bfcae74");
        props.put(AUTH_PASSPHRASE, "R@ndomP@ssword");
        GithubClientImpl impl = GithubClientImpl.create(props);
        assertEquals("Basic dGVzdHVzZXI6dGVzdFB3ZA==", impl.clientProps().get(CREDENTIALS));
    }

    @Test
    public void testProxy() {
        String proxyHost = "proxy.test.com";
        String proxyUser = "testuser";
        String proxyPwd = "testpwd";

        Properties props = load("files/unit-test.properties");
        props.put(USE_PROXY, "true");
        props.put(PROXY_HOST, proxyHost);
        props.put(PROXY_PORT, "8080");
        props.put(PROXY_USERNAME, proxyUser);
        props.put(PROXY_PASSWORD, proxyPwd);
        GithubClientImpl impl = GithubClientImpl.create(props);
        DefaultHttpClient httpClient = impl.httpClient();
        
        HttpHost proxy = (HttpHost) httpClient.getParams().getParameter(DEFAULT_PROXY);
        assertEquals(proxyHost, proxy.getHostName());
        assertEquals(8080, proxy.getPort());
        
        Credentials expCredentials = httpClient.getCredentialsProvider().getCredentials(new AuthScope(proxyHost, 8080));
        assertEquals(proxyUser, expCredentials.getUserPrincipal().getName());
        assertEquals(proxyPwd, expCredentials.getPassword());
    }

    @Test
    public void testEnableCerts() {
        Properties props = load("files/unit-test.properties");
        props.put(TRUST_SELFSIGNED, "true");
        GithubClientImpl impl = GithubClientImpl.create(props);
        SchemeRegistry registry = impl.httpClient().getConnectionManager().getSchemeRegistry();
        assertNotNull(registry.getScheme("https").getSchemeSocketFactory());
    }

    @Test(expected = RuntimeException.class)
    public void failedSimply() {
        String hostName = "mygithub.com";
        Properties props = load("files/unit-test.properties");
        props.put(GIT_HOST_NAME, hostName);
        props.put(TRUST_SELFSIGNED, "true");
        GithubClientImpl impl = GithubClientImpl.create(props);
        impl.execute(new GithubFileRequest(createRepo(props), createArtifact(props)));
    }
}
