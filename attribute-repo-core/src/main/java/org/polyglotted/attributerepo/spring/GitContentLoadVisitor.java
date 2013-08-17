package org.polyglotted.attributerepo.spring;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.GIT_PROVIDER;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.IGNORE_GIT_PROPERTIES;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.USE_GLOBAL_PROPERTIES;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.isTrue;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createArtifact;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createGlobalArtifact;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createRepo;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import org.polyglotted.attributerepo.core.GitClient;
import org.polyglotted.attributerepo.model.Artifact;

class GitContentLoadVisitor {

    private Properties repoProperties;
    private GitProvider provider;

    public void setRepoProperties(Properties repoProperties) {
        this.repoProperties = repoProperties;
        this.provider = GitProvider.valueOf(repoProperties.getProperty(GIT_PROVIDER, "GITHUB"));
    }

    public boolean visit(Properties props) throws IOException {
        if (isTrue(repoProperties, IGNORE_GIT_PROPERTIES, "false"))
            return false;

        GitClient client = provider.createClient(repoProperties);
        try {
            if (isTrue(repoProperties, USE_GLOBAL_PROPERTIES, "false"))
                safeLoadInto(props, loadArtifact(client, true));

            safeLoadInto(props, loadArtifact(client, false));
            return true;
        }
        finally {
            client.destroy();
        }
    }

    private void safeLoadInto(Properties props, String value) throws IOException {
        props.load(new StringReader(checkNotNull(value)));
    }

    private String loadArtifact(GitClient client, boolean global) {
        Artifact artifact = global ? createGlobalArtifact(repoProperties) : createArtifact(repoProperties);
        return provider.loadPropertyFile(client, createRepo(repoProperties), artifact);
    }
}
