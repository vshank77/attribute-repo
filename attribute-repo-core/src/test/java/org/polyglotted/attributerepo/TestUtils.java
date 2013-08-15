package org.polyglotted.attributerepo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestUtils {

    public static Properties load(String path) {
        try {
            Properties props = new Properties();
            props.load(asStream(path));
            return props;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public static InputStream asStream(String path) {
        return TestUtils.class.getClassLoader().getResourceAsStream(path);
    }
}
