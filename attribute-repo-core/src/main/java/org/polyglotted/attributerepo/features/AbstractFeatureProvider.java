package org.polyglotted.attributerepo.features;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * The parent class for all feature providers that auto-registers with the FeatureRegistry
 * 
 * @author Shankar Vasudevan
 */
public abstract class AbstractFeatureProvider implements FeatureProvider, BeanFactoryPostProcessor, PriorityOrdered {

    public AbstractFeatureProvider(FeatureRegistry registry) {
        checkNotNull(registry).register(this);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // marker method for this class to be eagerly loaded
    }
}
