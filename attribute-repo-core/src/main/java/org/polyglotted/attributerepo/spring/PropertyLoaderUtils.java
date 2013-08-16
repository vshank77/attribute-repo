package org.polyglotted.attributerepo.spring;

import static com.google.common.base.Charsets.UTF_8;
import static org.springframework.core.io.support.PropertiesLoaderUtils.fillProperties;

import java.util.Properties;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

@Slf4j
abstract class PropertyLoaderUtils {

    public static Properties safeLoad(Resource location) {
        Properties result = new Properties();
        safeLoad(result, location);
        return result;
    }

    @SneakyThrows
    public static boolean safeLoad(Properties props, Resource location) {
        if (location == null || !location.isReadable()) 
            return false;

        log.info("Loading properties file from " + location);
        fillProperties(props, new EncodedResource(location, UTF_8));
        return true;
    }

    public static void overrideWithSystemProperties(Properties props) {
        props.putAll(System.getProperties());
    }
}
