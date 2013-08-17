package org.polyglotted.attributerepo.github;

import static org.junit.Assert.assertEquals;
import static org.polyglotted.attributerepo.TestUtils.load;
import static org.polyglotted.attributerepo.git.common.GitConstants.CREDENTIALS;
import static org.polyglotted.attributerepo.git.common.GitConstants.URL_PREFIX;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createArtifact;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createRepo;

import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class GithubFileRequestTest {

    @Test(expected = RuntimeException.class)
    public void testFailed() {
        createRequest().execute(null);
    }

    @Test
    public void testUri() {
        GithubFileRequest request = createRequest();
        request.setUri(new StringBuilder("/repos/polyglotted/attrib-repo?ref=refs"));
        assertEquals("/repos/polyglotted/attrib-repo?ref=refs", request.toString());
    }

    @Test
    public void testOverride() {
        GithubFileRequest request = createRequest();
        request.setOverrideTagRef("/refs/tags/simpleref");
        Map<String, String> clProps = ImmutableMap.of(URL_PREFIX, "", CREDENTIALS, "test cred");
        assertEquals(
                "/repos/polyglotted/attrib-repo/contents/org/polyglotted/attrib-repo/unit-test.properties?ref=%2Frefs%2Ftags%2Fsimpleref",
                request.createUriRequest(clProps).getURI().toASCIIString());
    }

    private GithubFileRequest createRequest() {
        Properties props = load("files/unit-test.properties");
        return new GithubFileRequest(createRepo(props), createArtifact(props));
    }
}
