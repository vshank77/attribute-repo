package org.polyglotted.attributerepo.core;

/**
 * Represents a remote Git request, that can be used to perform any git action on a repository. It is an extension of a
 * generic Http request and provides interface methods representing a Http request.
 * 
 * @author Shankar Vasudevan
 */
public interface Request {

    /**
     * Add a meta-header to the request
     * 
     * @param header
     *            string representing meta name
     * @param value
     *            string represenring meta value
     */
    void addHeader(String header, String value);

    /**
     * Add a parameter to the request
     * 
     * @param name
     *            string representing parameter name
     * @param value
     *            string representing parameter value
     */
    void addParam(String name, String value);

    /**
     * set the uri for the execution of this request
     * 
     * @param uri
     *            string representing the uri
     */
    void setUri(StringBuilder uri);
}
