package org.polyglotted.attributerepo.spring;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Properties;

import org.polyglotted.attributerepo.features.FeatureProvider;
import org.polyglotted.attributerepo.features.FeatureRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

/**
 * AttribRepoPlaceholderConfigurer is the main spring configurer for loading Git based attribute repository. Using this
 * directly is discouraged and please import it from "classpath:/META-INF/spring/attributerepo-context.xml"
 * 
 * @author Shankar Vasudevan
 */
public class AttribRepoPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements InitializingBean {

    private final OverrideFileLoadVisitor overrideFileLoadVisitor = new OverrideFileLoadVisitor();
    private final GitContentLoadVisitor gitContentLoadVisitor = new GitContentLoadVisitor();

    private Properties attribRepoProperties;
    private FeatureRegistry registry;

    @Override
    public void setLocation(Resource location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLocations(Resource[] locations) {
        throw new UnsupportedOperationException();
    }

    /**
     * Set the Properties file location
     * 
     * @param attribRepoPropsFile
     */
    public void setPropertiesFileLocation(Resource attribRepoPropsFile) {
        attribRepoProperties = PropertyLoaderUtils.safeLoad(attribRepoPropsFile);
        PropertyLoaderUtils.overrideWithSystemProperties(attribRepoProperties, null);
        gitContentLoadVisitor.setRepoProperties(attribRepoProperties);
    }

    /**
     * Set the Override file location
     * 
     * @param overrideFileLocation
     */
    public void setOverrideFileLocation(Resource overrideFileLocation) {
        overrideFileLoadVisitor.setOverrideFileLocation(overrideFileLocation);
    }

    /**
     * Set the Feature registry
     * 
     * @param registry
     */
    public void setRegistry(FeatureRegistry registry) {
        this.registry = checkNotNull(registry);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        checkNotNull(registry);
        checkNotNull(attribRepoProperties);
    }

    @Override
    protected void loadProperties(Properties props) throws IOException {
        if (overrideFileLoadVisitor.visit(props)) {
            return;
        }
        gitContentLoadVisitor.visit(props);
    }

    @Override
    protected String resolvePlaceholder(String placeholder, Properties props) {
        for (FeatureProvider feature : registry.features()) {
            if (feature.canResolve(placeholder)) {
                return feature.resolveAttribute(placeholder, props);
            }
        }
        return super.resolvePlaceholder(placeholder, props);
    }
}
