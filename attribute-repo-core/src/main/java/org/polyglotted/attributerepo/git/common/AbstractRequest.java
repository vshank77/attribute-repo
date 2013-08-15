package org.polyglotted.attributerepo.git.common;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.polyglotted.attributerepo.git.common.GitConstants.CHARSET_UTF8;
import static org.polyglotted.attributerepo.git.common.GitConstants.CREDENTIALS;
import static org.polyglotted.attributerepo.git.common.GitConstants.DEFAULT_CONTENT_TYPE;
import static org.polyglotted.attributerepo.git.common.GitConstants.HEADER_AUTHORIZATION;
import static org.polyglotted.attributerepo.git.common.GitConstants.HEADER_CONTENT_LENGTH;
import static org.polyglotted.attributerepo.git.common.GitConstants.HEADER_CONTENT_TYPE;
import static org.polyglotted.attributerepo.git.common.GitConstants.HEADER_USER_AGENT;
import static org.polyglotted.attributerepo.git.common.GitConstants.URL_PREFIX;
import static org.polyglotted.attributerepo.git.common.GitConstants.USER_AGENT;
import static org.polyglotted.attributerepo.git.common.GitUtils.GSON;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.polyglotted.attributerepo.core.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.gson.JsonParseException;

/**
 * A fully executable git request object. Subclasses can construct the necessary uri, headers and params and execute
 * using the git client. Only the get result methods are left open for the subclasses.
 * 
 * @author Shankar Vasudevan
 * 
 * @param <R>
 */
public abstract class AbstractRequest<R> implements Request<R> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRequest.class);

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
        if (logger.isInfoEnabled()) {
            logger.info("creating uri:" + fullUri);
        }

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
        if (value != null)
            headers.put(key, value);
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
    public int hashCode() {
        final String fullUri = generateUri();
        return fullUri != null ? fullUri.hashCode() : super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof AbstractRequest))
            return false;
        final String fullUri = generateUri();

        @SuppressWarnings("unchecked")
        final String objUri = ((AbstractRequest<R>) obj).generateUri();
        return fullUri != null && fullUri.equals(objUri);
    }

    @Override
    public String toString() {
        final String fullUri = generateUri();
        return fullUri != null ? fullUri : super.toString();
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

    protected ByteArrayEntity createEntity(Object body) {
        try {
            return new ByteArrayEntity(toJson(body).getBytes(CHARSET_UTF8));
        }
        catch (UnsupportedEncodingException uue) {
            throw new RuntimeException("error creating entity", uue);
        }
    }

    protected String toJson(Object object) {
        try {
            return GSON.toJson(object);
        }
        catch (JsonParseException jpe) {
            throw new RuntimeException("error formatting json", jpe);
        }
    }
}
