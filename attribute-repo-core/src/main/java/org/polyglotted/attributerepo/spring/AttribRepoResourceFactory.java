package org.polyglotted.attributerepo.spring;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.polyglotted.attributerepo.core.AttribRepoProperties.GIT_PROVIDER;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createArtifact;
import static org.polyglotted.attributerepo.git.common.RequestFactory.createRepo;
import static org.polyglotted.attributerepo.spring.GitContentLoadVisitor.safeLoadInto;
import static org.polyglotted.attributerepo.spring.PropertyLoaderUtils.overrideWithSystemProperties;
import static org.polyglotted.attributerepo.spring.PropertyLoaderUtils.safeLoad;

import java.io.IOException;
import java.util.Properties;

import org.polyglotted.attributerepo.core.GitClient;
import org.polyglotted.attributerepo.model.Artifact;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;

public class AttribRepoResourceFactory implements InitializingBean, BeanFactoryPostProcessor {

    private Properties repoProperties;
    private GitProvider provider;

    /**
     * Set the Properties file location
     * 
     * @param attribRepoPropsFile
     */
    public void setPropertiesFileLocation(Resource attribRepoPropsFile) {
        repoProperties = safeLoad(attribRepoPropsFile);
        overrideWithSystemProperties(repoProperties);
        this.provider = GitProvider.valueOf(repoProperties.getProperty(GIT_PROVIDER, "GITHUB"));
    }


    public Properties loadProperties(String fileName) throws IOException {
        GitClient client = provider.createClient(repoProperties);
        try {
            return safeLoadInto(new Properties(), loadArtifact(client, fileName));
        }
        finally {
            client.destroy();
        }
    }

    public String loadResource(String fileName) throws IOException {
        GitClient client = provider.createClient(repoProperties);
        try {
            return loadArtifact(client, fileName);
        }
        finally {
            client.destroy();
        }
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        checkNotNull(repoProperties);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // marker method for this class to be eagerly loaded
    }

    private String loadArtifact(GitClient client, String fileName) {
        Artifact artifact = createArtifact(repoProperties).setFileName(fileName);
        return provider.loadFile(client, createRepo(repoProperties), artifact);
    }
}
