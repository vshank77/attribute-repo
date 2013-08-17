package org.polyglotted.attributerepo.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Test;

public class AttribRepoPropertiesTest extends AttribRepoProperties {

    @Test
    public void testIsTrue() {
        Properties props = createProps("test1", "true", "test2", "false");
        assertTrue(isTrue(props, "test1", "true"));
        assertFalse(isTrue(props, "test2", "true"));
        assertTrue(isTrue(props, "test3", "true"));
        assertFalse(isTrue(props, "test3", "false"));
    }

    @Test
    public void testIsFalse() {
        Properties props = createProps("test1", "true", "test2", "false");
        assertFalse(isFalse(props, "test1", "true"));
        assertTrue(isFalse(props, "test2", "true"));
        assertFalse(isFalse(props, "test3", "true"));
        assertTrue(isFalse(props, "test3", "false"));
    }

    @Test
    public void testGetIntProperty() {
        Properties props = createProps("test1", "10");
        assertEquals(10, getIntProperty(props, "test1", 25));
        assertEquals(25, getIntProperty(props, "test2", 25));
    }

    @Test
    public void testNotNullProperty() {
        Properties props = createProps("test1", "value");
        assertEquals("value", notNullProperty(props, "test1"));
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullPropertyFail() {
        Properties props = new Properties();
        notNullProperty(props, "test1");
    }

    @Test
    public void testNotNullPropertyDefault() {
        Properties props = createProps("test1", "value1", "defTest", "defValue");
        assertEquals("value1", notNullProperty(props, "test1", "defTest"));
        assertEquals("defValue", notNullProperty(props, "test2", "defTest"));
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullPropertyDefaultFail() {
        Properties props = new Properties();
        notNullProperty(props, "test1", "defTest");
    }

    public static Properties createProps(String... values) {
        Properties props = new Properties();
        for (int i = 0; i < values.length; i = i + 2) {
            props.put(values[i], values[i + 1]);
        }
        return props;
    }
}
