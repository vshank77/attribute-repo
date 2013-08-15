package org.polyglotted.attributerepo.stash;

import static org.junit.Assert.assertEquals;
import static org.polyglotted.attributerepo.TestUtils.asStream;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createArtifact;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createRepo;

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
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;
import org.polyglotted.attributerepo.stash.StashClientImpl;
import org.polyglotted.attributerepo.stash.StashFileRequest;

public class StashClientTest {

    @Mocked
    AbstractHttpClient mockClient;
    @Mocked
    HttpResponse mockResponse;
    @NonStrict
    ClientConnectionManager mockConnManager;

    @Test
    public void testIntegration() throws IOException {
        new Expectations() {
            {
                mockClient.execute((HttpHost) withNotNull(), (HttpRequest) withNotNull());
                result = mockResponse;

                mockResponse.getStatusLine();
                result = new BasicStatusLine(new ProtocolVersion("http", 1, 1), 200, "ok");

                mockResponse.getEntity();
                result = new InputStreamEntity(asStream("files/stash-output.txt"), 0);

                mockClient.getConnectionManager();
                result = mockConnManager;

                mockConnManager.shutdown();
            }
        };

        Properties props = new Properties();
        props.load(asStream("files/stash-integration.properties"));
        String result = execute(props);
        String expected = "#project properties\nperson.name=adrian tester\nperson.age=28\n";
        assertEquals(expected, result);
    }

    private static String execute(Properties props) {
        StashClientImpl client = null;
        try {
            client = StashClientImpl.create(props);
            StashFileRequest request = new StashFileRequest(createRepo(props), createArtifact(props));
            String result = request.execute(client);

            return result;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            if (client != null)
                client.destroy();
        }
        return null;
    }
}
