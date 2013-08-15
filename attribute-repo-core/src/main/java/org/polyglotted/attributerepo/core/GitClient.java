package org.polyglotted.attributerepo.core;

/**
 * Represents a client connection to a remote git server. It would return a generic response object properly formatted
 * to the calling function
 * 
 * @author Shankar Vasudevan
 */
public interface GitClient {

    /**
     * execute the given request
     * 
     * @param request
     *            object representing a git action
     * @return object representing a git response
     */
    <R> Response execute(Request<R> request);

    /**
     * free any resources consumed by this connection
     */
    void destroy();
}
