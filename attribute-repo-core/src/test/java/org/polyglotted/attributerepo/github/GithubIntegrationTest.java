package org.polyglotted.attributerepo.github;

import static org.junit.Assert.assertEquals;
import static org.polyglotted.attributerepo.TestUtils.asStream;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createArtifact;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createRepo;

import java.io.IOException;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;
import org.polyglotted.attributerepo.github.GithubClientImpl;
import org.polyglotted.attributerepo.github.GithubFileRequest;

public class GithubIntegrationTest {

    public static void main(String ar[]) {
        System.out.println(execute(System.getProperties()));
    }

    @Test
    @Ignore(value = "Loaded as part of BasicSpringAppTest")
    public void testIntegration() throws IOException {
        Properties props = new Properties();
        props.load(asStream("files/github-integration.properties"));
        String result = execute(props);
        String expected = "#project properties\nperson.name=david tester\nperson.age=31\n";
        assertEquals(expected, result);
    }

    private static String execute(Properties props) {
        GithubClientImpl client = null;
        try {
            client = GithubClientImpl.create(props);
            GithubFileRequest request = new GithubFileRequest(createRepo(props), createArtifact(props));
            String result = request.execute(client);

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (client != null)
                client.destroy();
        }
        return null;
    }
}
