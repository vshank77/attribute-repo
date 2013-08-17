package org.polyglotted.attributerepo.stash;

import static org.polyglotted.attributerepo.stash.StashConstants.PARAM_AT;
import static org.polyglotted.attributerepo.stash.StashConstants.SEGMENT_API;
import static org.polyglotted.attributerepo.stash.StashConstants.SEGMENT_BROWSE;
import static org.polyglotted.attributerepo.stash.StashConstants.SEGMENT_PROJECTS;
import static org.polyglotted.attributerepo.stash.StashConstants.SEGMENT_REPOS;

import org.polyglotted.attributerepo.core.GitClient;
import org.polyglotted.attributerepo.core.Response;
import org.polyglotted.attributerepo.git.common.FileRequest;
import org.polyglotted.attributerepo.model.Artifact;
import org.polyglotted.attributerepo.model.RepoId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

/**
 * Stash based Browse request that downloads contents of a file
 * 
 * @author Shankar Vasudevan
 */
public class StashFileRequest extends FileRequest {

    private static final Joiner NEWJOINER = Joiner.on("\n");
    private static final Logger logger = LoggerFactory.getLogger(StashFileRequest.class);

    /**
     * Create a new StashFileRequest
     * 
     * @param repo
     *            the repository definition
     * @param artifact
     *            the deployed artifact that you would like to get the properties
     */
    public StashFileRequest(RepoId repo, Artifact artifact) {
        super(repo, artifact);
    }

    public String execute(GitClient client) {
        try {
            Response response = client.execute(this);
            StashFile fileContent = response.getResult(StashFile.class);
            return collateLines(fileContent);
        }
        catch (Exception ex) {
            logger.error("error in content request execute", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected String getRefParam() {
        return PARAM_AT;
    }

    @Override
    protected StringBuilder buildUri() {
        StringBuilder uri = new StringBuilder();
        uri.append(SEGMENT_API);
        uri.append(SEGMENT_PROJECTS);
        uri.append(repo.getUser());
        uri.append(SEGMENT_REPOS);
        uri.append(repo.getRepo());
        uri.append(SEGMENT_BROWSE);
        uri.append(artifact.buildFilePath());
        return uri;
    }

    protected String collateLines(StashFile fileContent) {
        return NEWJOINER.join(fileContent.getLines());
    }
}
