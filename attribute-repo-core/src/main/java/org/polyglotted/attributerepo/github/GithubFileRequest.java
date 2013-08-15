package org.polyglotted.attributerepo.github;

import static org.polyglotted.attributerepo.git.common.GitConstants.HEADER_ACCEPT;
import static org.polyglotted.attributerepo.github.GithubConstants.ACCEPT_TYPE;
import static org.polyglotted.attributerepo.github.GithubConstants.ENCODED_BASE64;
import static org.polyglotted.attributerepo.github.GithubConstants.PARAM_REF;
import static org.polyglotted.attributerepo.github.GithubConstants.SEGMENT_CONTENTS;
import static org.polyglotted.attributerepo.github.GithubConstants.SEGMENT_REPOS;
import static org.springframework.util.StringUtils.isEmpty;

import org.polyglotted.attributerepo.core.GitClient;
import org.polyglotted.attributerepo.core.Response;
import org.polyglotted.attributerepo.git.common.FileRequest;
import org.polyglotted.attributerepo.git.common.GitUtils;
import org.polyglotted.attributerepo.model.Artifact;
import org.polyglotted.attributerepo.model.RepoId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A File content request that returns the contents of a file
 * 
 * @author Shankar Vasudevan
 */
public class GithubFileRequest extends FileRequest<String> {

    private static final Logger logger = LoggerFactory.getLogger(GithubFileRequest.class);

    /**
     * Create a new GithubFileRequest
     * 
     * @param repo
     *            the repository definition
     * @param artifact
     *            the deployed artifact that you would like to get the properties
     */
    public GithubFileRequest(RepoId repo, Artifact artifact) {
        super(repo, artifact);
        addHeader(HEADER_ACCEPT, ACCEPT_TYPE);
    }

    @Override
    public String execute(GitClient client) {
        try {
            Response response = client.execute(this);
            GithubFile githubFile = response.getResult(GithubFile.class);
            return getDecodedContent(githubFile);
        }
        catch (Exception ex) {
            logger.error("error in content request execute", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected String getRefParam() {
        return PARAM_REF;
    }

    @Override
    protected StringBuilder buildUri() {
        StringBuilder uri = new StringBuilder();
        uri.append(SEGMENT_REPOS);
        uri.append("/");
        uri.append(repo.getUser());
        uri.append("/");
        uri.append(repo.getRepo());
        uri.append(SEGMENT_CONTENTS);
        uri.append(artifact.buildFilePath());
        return uri;
    }

    protected String getDecodedContent(GithubFile githubFile) {
        String contentStr = githubFile.getContent();
        if (!isEmpty(contentStr) && ENCODED_BASE64.equals(githubFile.getEncoding())) {
            return GitUtils.fromBase64(contentStr);
        }
        return contentStr;
    }
}
