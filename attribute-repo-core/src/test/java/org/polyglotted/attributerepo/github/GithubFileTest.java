package org.polyglotted.attributerepo.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.polyglotted.attributerepo.git.common.GitUtils.toBase64;
import static org.polyglotted.attributerepo.github.GithubConstants.ENCODED_BASE64;

import org.junit.Test;

public class GithubFileTest {

    @Test
    public void testGetDecodedContentNull() {
        assertNull(new GithubFile().getDecodedContent());
    }

    @Test
    public void testGetDecodedContentEmpty() {
        GithubFile githubFile = new GithubFile();
        githubFile.setContent("");
        assertEquals("", githubFile.getDecodedContent());
    }

    @Test
    public void testGetDecodedContentNotEncoded() {
        GithubFile githubFile = new GithubFile();
        githubFile.setContent("test");
        assertEquals("test", githubFile.getDecodedContent());
    }

    @Test
    public void testGetDecodedContent() {
        GithubFile githubFile = new GithubFile();
        githubFile.setContent(toBase64("test"));
        githubFile.setEncoding(ENCODED_BASE64);
        assertEquals("test", githubFile.getDecodedContent());
    }
}
