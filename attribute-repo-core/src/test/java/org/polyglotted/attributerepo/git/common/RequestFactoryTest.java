package org.polyglotted.attributerepo.git.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.ARTIFACT_ID;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.ENVIRONMENT;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.GLOBAL_ARTIFACT_ID;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.GLOBAL_GROUP_ID;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.GROUP_ID;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.REPO_NAME;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.REPO_USER;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.VERSION;
import static org.polyglotted.attributerepo.core.AttribRepoPropertiesTest.createProps;

import org.junit.Test;
import org.polyglotted.attributerepo.model.Artifact;
import org.polyglotted.attributerepo.model.RepoId;

public class RequestFactoryTest extends RequestFactory {

    @Test
    public void testCreateRepo() {
        RepoId repo = createRepo(createProps(REPO_USER, "testUser", REPO_NAME, "testRepo"));
        assertEquals("testUser", repo.getUser());
        assertEquals("testRepo", repo.getRepo());
    }

    @Test
    public void testCreateArtifact() {
        Artifact artifact = createArtifact(createProps(GROUP_ID, "testGroup", ARTIFACT_ID, "testArtifact", VERSION,
                "testVersion-SNAPSHOT", ENVIRONMENT, "testEnv"));
        assertEquals("testGroup", artifact.getGroupId());
        assertEquals("testArtifact", artifact.getArtifactId());
        assertEquals("testVersion-SNAPSHOT", artifact.getVersion());
        assertEquals("testEnv", artifact.getEnvironment());
        assertTrue(artifact.isSnapshot());
    }

    @Test
    public void testCreateGlobalArtifact() {
        Artifact artifact = createGlobalArtifact(createProps(GLOBAL_GROUP_ID, "testGroup", GLOBAL_ARTIFACT_ID,
                "testArtifact", ENVIRONMENT, "testEnv"));
        assertEquals("testGroup", artifact.getGroupId());
        assertEquals("testArtifact", artifact.getArtifactId());
        assertNull(artifact.getVersion());
        assertEquals("testEnv", artifact.getEnvironment());
        assertTrue(artifact.isSnapshot());
    }
}
