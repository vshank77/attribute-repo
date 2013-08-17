package org.polyglotted.attributerepo.spring;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PropertyLoaderUtilsTest extends PropertyLoaderUtils {

    @Test
    public void testSafeLoadResource() {
        assertNotNull(safeLoad(null));
    }
}
