package org.polyglotted.attributerepo.github;

import static org.junit.Assert.assertEquals;
import static org.polyglotted.attributerepo.TestUtils.asStream;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createArtifact;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createRepo;
import static org.polyglotted.attributerepo.spring.GitProvider.GITHUB;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.polyglotted.attributerepo.core.GitClient;

public class GithubIntegrationTest {

    public static void main(String ar[]) {
        System.out.println(execute(System.getProperties()));
    }

    @Test
    public void testIntegration() throws IOException {
        Properties props = new Properties();
        props.load(asStream("files/github-integration.properties"));

        String result = execute(props);
        String expected = "#project properties\nperson.name=david tester\nperson.age=31\n";
        assertEquals(expected, result);
    }

    private static String execute(Properties props) {
        GitClient client = GITHUB.createClient(props);
        try {
            return GITHUB.loadPropertyFile(client, createRepo(props), createArtifact(props));
        }
        finally {
            client.destroy();
        }
    }
}
