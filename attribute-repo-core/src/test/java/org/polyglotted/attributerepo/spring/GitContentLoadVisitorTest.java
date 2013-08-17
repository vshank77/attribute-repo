package org.polyglotted.attributerepo.spring;

import static org.junit.Assert.assertEquals;
import static org.polyglotted.attributerepo.TestUtils.asStream;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.USE_GLOBAL_PROPERTIES;

import java.io.IOException;
import java.util.Properties;

import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrict;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;

public class GitContentLoadVisitorTest {

    @Mocked
    AbstractHttpClient mockClient;
    @Mocked
    HttpResponse mockResponse;
    @NonStrict
    ClientConnectionManager mockConnManager;

    @Test
    public void testVisit() throws IOException {
        new Expectations() {
            {
                mockClient.execute((HttpHost) withNotNull(), (HttpRequest) withNotNull());
                result = mockResponse;
                mockResponse.getFirstHeader(anyString);
                result = null;
                mockResponse.getFirstHeader(anyString);
                result = null;
                
                mockResponse.getStatusLine();
                result = new BasicStatusLine(new ProtocolVersion("http", 1, 1), 200, "ok");
                mockResponse.getEntity();
                result = new StringEntity("{\"content\"=\"unit=test\n\"}");
                mockClient.getConnectionManager();
                result = mockConnManager;
                mockConnManager.shutdown();
            }
        };

        Properties repoProps = new Properties();
        repoProps.load(asStream("files/unit-test.properties"));
        repoProps.put(USE_GLOBAL_PROPERTIES, "false");

        Properties result = new Properties();
        GitContentLoadVisitor visitor = new GitContentLoadVisitor();
        visitor.setRepoProperties(repoProps);
        visitor.visit(result);
        assertEquals("test", result.getProperty("unit"));
    }
}
