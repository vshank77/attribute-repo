package org.polyglotted.attributerepo.features;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Test;
import org.springframework.core.Ordered;

public class AbstractFeatureProviderTest extends AbstractFeatureProvider {

    public static final FeatureRegistry REGISTRY = new FeatureRegistry();

    public AbstractFeatureProviderTest() {
        super(REGISTRY);
    }

    @Test
    public void test() {
        assertEquals(Ordered.HIGHEST_PRECEDENCE, getOrder());
        postProcessBeanFactory(null);
    }

    @Override
    public boolean canResolve(String placeholder) {
        return "test".equals(placeholder);
    }

    @Override
    public String resolveAttribute(String placeholder, Properties props) {
        return "value";
    }
}
