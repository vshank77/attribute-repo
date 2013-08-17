package org.polyglotted.attributerepo.git.common;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.polyglotted.attributerepo.git.common.GitConstants.CREDENTIALS;
import static org.polyglotted.attributerepo.git.common.GitConstants.DEFAULT_CONTENT_TYPE;
import static org.polyglotted.attributerepo.git.common.GitConstants.HEADER_AUTHORIZATION;
import static org.polyglotted.attributerepo.git.common.GitConstants.HEADER_CONTENT_TYPE;
import static org.polyglotted.attributerepo.git.common.GitConstants.HEADER_USER_AGENT;
import static org.polyglotted.attributerepo.git.common.GitConstants.URL_PREFIX;
import static org.polyglotted.attributerepo.git.common.GitConstants.USER_AGENT;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.methods.HttpUriRequest;
import org.polyglotted.attributerepo.core.Request;

import com.google.common.collect.Maps;

/**
 * A fully executable git request object. Subclasses can construct the necessary uri, headers and params and execute
 * using the git client. Only the get result methods are left open for the subclasses.
 * 
 * @author Shankar Vasudevan
 * 
 * @param <R>
 */
@Slf4j
public abstract class AbstractRequest<R> implements Request<R> {

    private Map<String, String> params = Maps.newLinkedHashMap();
    private Map<String, String> headers = Maps.newLinkedHashMap();
    private StringBuilder uri = null;

    public HttpUriRequest createUriRequest(Map<String, String> clientProps) {
        String fullUri = checkNotNull(clientProps.get(URL_PREFIX)) + generateUri();
        log.info("creating uri:" + fullUri);

        HttpUriRequest request = createBaseRequest(fullUri);
        addDefaultRequestHeaders(request, clientProps.get(CREDENTIALS));
        return request;
    }

    protected abstract HttpUriRequest createBaseRequest(String uri);

    @Override
    public void addParam(String key, String value) {
        params.put(key, value);
    }

    @Override
    public void addHeader(String key, String value) {
        headers.put(checkNotNull(key), checkNotNull(value));
    }

    @Override
    public void setUri(StringBuilder uri) {
        this.uri = checkNotNull(uri);
    }

    @Override
    public String toString() {
        return generateUri();
    }

    protected String generateUri() {
        final String baseUri = checkNotNull(uri).toString();
        if (baseUri.indexOf('?') != -1)
            return baseUri;

        final String paramStr = GitUtils.addParams(params);
        return (paramStr.length() > 0) ? baseUri + '?' + paramStr : baseUri;
    }

    protected void addDefaultRequestHeaders(HttpUriRequest request, String credentials) {
        request.addHeader(HEADER_USER_AGENT, USER_AGENT);
        request.addHeader(HEADER_CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
        request.addHeader(HEADER_AUTHORIZATION, credentials);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.addHeader(entry.getKey(), entry.getValue());
        }
    }
}
