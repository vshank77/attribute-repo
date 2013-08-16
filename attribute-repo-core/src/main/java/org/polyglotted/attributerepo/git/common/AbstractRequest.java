package org.polyglotted.attributerepo.git.common;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.polyglotted.attributerepo.git.common.GitConstants.CREDENTIALS;
import static org.polyglotted.attributerepo.git.common.GitConstants.DEFAULT_CONTENT_TYPE;
import static org.polyglotted.attributerepo.git.common.GitConstants.HEADER_AUTHORIZATION;
import static org.polyglotted.attributerepo.git.common.GitConstants.HEADER_CONTENT_LENGTH;
import static org.polyglotted.attributerepo.git.common.GitConstants.HEADER_CONTENT_TYPE;
import static org.polyglotted.attributerepo.git.common.GitConstants.HEADER_USER_AGENT;
import static org.polyglotted.attributerepo.git.common.GitConstants.URL_PREFIX;
import static org.polyglotted.attributerepo.git.common.GitConstants.USER_AGENT;
import static org.polyglotted.attributerepo.git.common.GitUtils.GSON;
import static org.polyglotted.crypto.utils.Charsets.UTF8;

import java.util.Map;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
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

    private final RequestType type;
    private Map<String, String> params = Maps.newLinkedHashMap();
    private Map<String, String> headers = Maps.newLinkedHashMap();
    private StringBuilder uri = null;
    private Object body = null;

    protected AbstractRequest(RequestType type) {
        this.type = checkNotNull(type);
    }

    public HttpUriRequest createUriRequest(Map<String, String> clientProps) {
        String fullUri = checkNotNull(clientProps.get(URL_PREFIX)) + generateUri();
        log.info("creating uri:" + fullUri);

        HttpUriRequest request = getType().createUriRequest(fullUri);
        addDefaultRequestHeaders(request, clientProps.get(CREDENTIALS));
        setBodyIfApplicable(request);
        return request;
    }

    public RequestType getType() {
        return type;
    }

    @Override
    public void addParam(String key, String value) {
        params.put(key, value);
    }

    public AbstractRequest<R> setParams(Map<String, String> params) {
        this.params = checkNotNull(params);
        return this;
    }

    @Override
    public void addHeader(String key, String value) {
        headers.put(checkNotNull(key), checkNotNull(value));
    }

    public AbstractRequest<R> setHeaders(Map<String, String> headers) {
        this.headers = checkNotNull(headers);
        return this;
    }

    @Override
    public void setUri(StringBuilder uri) {
        this.uri = checkNotNull(uri);
    }

    @Override
    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return generateUri();
    }

    protected String generateUri() {
        if (uri == null)
            return null;

        final String baseUri = uri.toString();
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

    protected void setBodyIfApplicable(HttpUriRequest request) {
        if (!(request instanceof HttpEntityEnclosingRequestBase)) {
            return;
        }
        if (body == null) {
            request.setHeader(HEADER_CONTENT_LENGTH, "0");
            return;
        }

        ((HttpEntityEnclosingRequestBase) request).setEntity(createEntity(body));
    }

    @SneakyThrows
    protected ByteArrayEntity createEntity(Object body) {
        return new ByteArrayEntity(UTF8.getBytes(GSON.toJson(body)));
    }
}
