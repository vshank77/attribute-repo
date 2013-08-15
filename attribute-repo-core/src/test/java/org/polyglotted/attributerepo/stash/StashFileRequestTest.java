package org.polyglotted.attributerepo.stash;

import static org.junit.Assert.assertEquals;
import static org.polyglotted.attributerepo.TestUtils.load;
import static org.polyglotted.attributerepo.git.common.GitConstants.CREDENTIALS;
import static org.polyglotted.attributerepo.git.common.GitConstants.URL_PREFIX;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createArtifact;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createRepo;

import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.polyglotted.attributerepo.stash.StashFileRequest;

import com.google.common.collect.ImmutableMap;

public class StashFileRequestTest {
    @Test(expected = RuntimeException.class)
    public void testFailed() {
        createRequest().execute(null);
    }

    @Test
    public void testOverride() {
        StashFileRequest request = createRequest();
        request.setOverrideTagRef("/refs/tags/simpleref");
        Map<String, String> clProps = ImmutableMap.of(URL_PREFIX, "", CREDENTIALS, "test cred");
        assertEquals(
                "/rest/api/1.0/projects/polyglotted/repos/attrib-repo/browse/org/polyglotted/attrib-repo/unit-test.properties?at=%2Frefs%2Ftags%2Fsimpleref",
                request.createUriRequest(clProps).getURI().toASCIIString());
    }

    private StashFileRequest createRequest() {
        Properties props = load("files/unit-test.properties");
        return new StashFileRequest(createRepo(props), createArtifact(props));
    }
}
