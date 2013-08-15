package org.polyglotted.attributerepo.core;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Properties;

/**
 * Generic set of all properties that can be configured on this library
 * 
 * @author Shankar Vasudevan
 */
public class AttribRepoProperties {

    public static final String PROPERTY_PREFIX = "attributerepo.";

    public static final String PROPERTY_FILE_LOCATION = "attributerepo.properties.file.location";
    public static final String OVERRIDE_FILE_LOCATION = "attributerepo.override.file.location";

    public static final String GIT_PROVIDER = "attributerepo.git.provider";
    public static final String GIT_HOST_NAME = "attributerepo.git.host.name";
    public static final String GIT_HOST_PORT = "attributerepo.git.host.port";
    public static final String GIT_HOST_SCHEME = "attributerepo.git.host.scheme";

    public static final String USE_PROXY = "attributerepo.use.proxy";
    public static final String PROXY_HOST = "attributerepo.proxy.host";
    public static final String PROXY_PORT = "attributerepo.proxy.port";
    public static final String PROXY_USERNAME = "attributerepo.proxy.username";
    public static final String PROXY_PASSWORD = "attributerepo.proxy.password";
    public static final String TRUST_SELFSIGNED = "attributerepo.trust.selfsigned.certs";

    public static final String USE_BASIC_AUTH = "attributerepo.use.basic.auth";
    public static final String AUTH_USERNAME = "attributerepo.auth.username";
    public static final String AUTH_PASSWORD = "attributerepo.auth.password";
    public static final String OAUTH2_TOKEN = "attributerepo.oauth2.token";

    public static final String REPO_USER = "repo.user";
    public static final String REPO_NAME = "repo.name";

    public static final String IGNORE_GIT_PROPERTIES = "attributerepo.ignore.git.properties";
    public static final String GROUP_ID = "groupId";
    public static final String ARTIFACT_ID = "artifactId";
    public static final String VERSION = "version";
    public static final String ENVIRONMENT = "environment";

    public static final String USE_GLOBAL_PROPERTIES = "attributerepo.use.global.properties";
    public static final String GLOBAL_GROUP_ID = "global.groupId";
    public static final String GLOBAL_ARTIFACT_ID = "global.artifactId";
    public static final String GLOBAL_VERSION = "global.version";
    public static final String GLOBAL_ENVIRONMENT = "global.environment";

    @NotInstantiable
    private AttribRepoProperties() {
    }

    /**
     * Return a boolean representation of a property
     * 
     * @param props
     *            the Properties to query
     * @param propName
     *            the name of the property
     * @param defValue
     *            boolean representing default state
     * @return true if the property represents True value
     */
    public static boolean isTrue(Properties props, String propName, String defValue) {
        String bool = props.getProperty(propName, defValue);
        return "true".equalsIgnoreCase(bool);
    }

    /**
     * Return a boolean representation of a property
     * 
     * @param props
     *            the Properties to query
     * @param propName
     *            the name of the property
     * @param defValue
     *            boolean representing default state
     * @return true if the property represents false value
     */
    public static boolean isFalse(Properties props, String propName, String defValue) {
        return !isTrue(props, propName, defValue);
    }

    /**
     * Return a integer representation of a property
     * 
     * @param props
     *            the Properties to query
     * @param propName
     *            the name of the property
     * @param defValue
     *            default integer to return if the property is not present
     * @return int representing the property
     */
    public static int getIntProperty(Properties props, String propName, int defValue) {
        return props.containsKey(propName) ? Integer.parseInt(props.getProperty(propName)) : defValue;
    }

    /**
     * Return a non null property value
     * 
     * @param props
     *            the properties to query
     * @param propName
     *            the name of the property
     * @param propNameDefault
     *            an alternate property that would be queried if the main name fails
     * @return the String representing the property
     * @throws IllegalArgumentException
     *             if the property or the alternate property are not found
     */
    public static String notNullProperty(Properties props, String propName, String propNameDefault) {
        String result = props.getProperty(propName);
        return result != null ? result : checkNotNull(props.getProperty(propNameDefault));
    }

    /**
     * Return a non null property value
     * 
     * @param props
     *            the properties to query
     * @param propName
     *            the name of the property
     * @return the String representing the property
     * @throws IllegalArgumentException
     *             if the property is not found
     */
    public static String notNullProperty(Properties props, String propName) {
        return checkNotNull(props.getProperty(propName));
    }
}
