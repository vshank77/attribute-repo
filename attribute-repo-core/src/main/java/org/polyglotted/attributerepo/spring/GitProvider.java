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
        public String loadFile(GitClient client, RepoId repo, Artifact artifact) {
            return new GithubFileRequest(repo, artifact).execute(client);
        }
    },

    STASH {
        @Override
        public GitClient createClient(Properties repoProps) {
            return StashClientImpl.create(repoProps);
        }

        @Override
        public String loadFile(GitClient client, RepoId repo, Artifact artifact) {
            return new StashFileRequest(repo, artifact).execute(client);
        }
    };

    public abstract GitClient createClient(Properties repoProperties);

    public abstract String loadFile(GitClient client, RepoId repo, Artifact artifact);
}
