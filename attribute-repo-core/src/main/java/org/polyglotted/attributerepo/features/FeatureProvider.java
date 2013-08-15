package org.polyglotted.attributerepo.features;

import java.util.Properties;

/**
 * Feature providers are advanced attribute resolvers that enhance or modify the default behavior of loading attributes
 * from a file. Some example implementations could be CryptoFeatureProvider which could decrypt encrypted values in the
 * properties based on a placeholder prefix.
 * 
 * @author Shankar Vasudevan
 */
public interface FeatureProvider {

    /**
     * Checks to see if you can resolve this attribute
     * 
     * @param placeholder
     *            the String representing the placeholder
     * @return true if this feature provider will resolve this attribute
     */
    boolean canResolve(String placeholder);

    /**
     * Resolve the given placeholder
     * 
     * @param placeholder
     *            the String representing the placeholder
     * @param props
     *            the Properties to query
     * @return the resolved attribute, null if not resolved
     */
    String resolveAttribute(String placeholder, Properties props);
}
