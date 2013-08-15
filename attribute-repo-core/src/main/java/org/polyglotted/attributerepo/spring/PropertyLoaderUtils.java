package org.polyglotted.attributerepo.spring;

import static org.springframework.core.io.support.PropertiesLoaderUtils.fillProperties;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;

public class PropertyLoaderUtils {
    private static Logger logger = LoggerFactory.getLogger(PropertyLoaderUtils.class);

    public static Properties safeLoad(Resource location) {
        Properties result = new Properties();
        safeLoad(result, location);
        return result;
    }

    public static boolean safeLoad(Properties props, Resource location) {
        if (location == null || !location.isReadable()) {
            return false;
        }

        if (logger.isInfoEnabled()) {
            logger.info("Loading properties file from " + location);
        }

        try {
            fillProperties(props, new EncodedResource(location, Charsets.UTF_8));
            return true;
        } catch (IOException e) {
            throw new RuntimeException("unable to load properties", e);
        }
    }

    public static void overrideWithSystemProperties(Properties props, Predicate<String> predicate) {
        if (predicate == null) {
            props.putAll(System.getProperties());
            return;
        }

        for (Entry<Object, Object> entry : System.getProperties().entrySet()) {
            if (predicate.apply(String.valueOf(entry.getKey()))) {
                props.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
