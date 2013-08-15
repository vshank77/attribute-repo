package org.polyglotted.attributerepo.spring;

import java.util.Properties;

import org.polyglotted.attributerepo.core.GitClient;
import org.polyglotted.attributerepo.github.GithubClientImpl;
import org.polyglotted.attributerepo.github.GithubFileRequest;
import org.polyglotted.attributerepo.model.Artifact;
import org.polyglotted.attributerepo.model.RepoId;
import org.polyglotted.attributerepo.stash.StashClientImpl;
import org.polyglotted.attributerepo.stash.StashFileRequest;

public enum GitProvider {

    GITHUB {
        @Override
        public GitClient createClient(Properties repoProps) {
            return GithubClientImpl.create(repoProps);
        }

        @Override
        public String loadPropertyFile(GitClient client, RepoId repo, Artifact artifact) {
            GithubFileRequest request = new GithubFileRequest(repo, artifact);
            return request.execute(client);
        }
    },

    STASH {
        @Override
        public GitClient createClient(Properties repoProps) {
            return StashClientImpl.create(repoProps);
        }

        @Override
        public String loadPropertyFile(GitClient client, RepoId repo, Artifact artifact) {
            StashFileRequest request = new StashFileRequest(repo, artifact);
            return request.execute(client);
        }
    };

    public abstract GitClient createClient(Properties repoProperties);

    public abstract String loadPropertyFile(GitClient client, RepoId repo, Artifact artifact);
}
