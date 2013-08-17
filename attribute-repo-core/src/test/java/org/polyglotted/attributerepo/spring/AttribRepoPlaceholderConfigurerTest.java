package org.polyglotted.attributerepo.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.junit.Test;
import org.polyglotted.attributerepo.features.AbstractFeatureProviderTest;

public class AttribRepoPlaceholderConfigurerTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testSetLocation() {
        new AttribRepoPlaceholderConfigurer().setLocation(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetLocations() {
        new AttribRepoPlaceholderConfigurer().setLocations(null);
    }

    @Test
    public void testResolvePlaceholder() {
        new AbstractFeatureProviderTest();
        AttribRepoPlaceholderConfigurer configurer = new AttribRepoPlaceholderConfigurer();
        configurer.setRegistry(AbstractFeatureProviderTest.REGISTRY);
        assertEquals("value", configurer.resolvePlaceholder("test", null));
        assertNull(configurer.resolvePlaceholder("test2", new Properties()));
    }
}
