package org.polyglotted.attributerepo.features;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * A feature registry is an extensible way of adding features to the attrib-repo spring configurer.
 * 
 * @author Shankar Vasudevan
 */
public class FeatureRegistry {

    private static final Logger logger = LoggerFactory.getLogger(FeatureRegistry.class);
    private List<FeatureProvider> featureList = Lists.newArrayList();

    /**
     * Register a new feature provider
     * 
     * @param provider
     *            the FeatureProvider to add
     */
    public void register(FeatureProvider provider) {
        logger.info("registering feature provider: " + provider);
        featureList.add(provider);
    }

    /**
     * @return the list of all feature providers registered
     */
    public List<FeatureProvider> features() {
        return featureList;
    }
}
