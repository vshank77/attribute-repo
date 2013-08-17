package org.polyglotted.attributerepo.git.common;

import static com.google.common.base.Charsets.UTF_8;
import static java.net.HttpURLConnection.HTTP_ACCEPTED;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

import lombok.SneakyThrows;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.entity.BufferedHttpEntity;
import org.polyglotted.attributerepo.core.Response;

import com.google.common.base.Strings;

/**
 * A default implementation of the Response object
 * 
 * @author Shankar Vasudevan
 */
public class DefaultResponse implements Response {

    public static final int HTTP_UNPROCESSABLE_ENTITY = 422;

    private final HttpResponse response;
    private StatusLine statusLine = null;

    public DefaultResponse(HttpResponse response) {
        this.response = response;
        handleHeaders();
        handleResponse();
    }

    @Override
    public int getStatusCode() {
        return statusLine == null ? HTTP_INTERNAL_ERROR : statusLine.getStatusCode();
    }

    @Override
    public String getResponseStatus() {
        if (statusLine == null)
            return null;
        int code = statusLine.getStatusCode();
        String status = statusLine.getReasonPhrase();

        if (!Strings.isNullOrEmpty(status))
            return status + " (" + code + ')';
        else
            return "Unknown error occurred (" + code + ")";
    }

    @Override
    public String getMeta(String headerName) {
        return parseHeader(headerName);
    }

    @Override
    @SneakyThrows
    public <V> V getResult(Type type) {
        InputStream inputStream = null;
        try {
            inputStream = new BufferedHttpEntity(response.getEntity()).getContent();
            Reader responseReader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
            return GitUtils.readJson(responseReader, type);
        }
        finally {
            inputStream.close();
        }
    }

    protected void handleHeaders() {
        // hook for subclasses to handle
    }

    protected String parseHeader(String headerName) {
        Header header = response.getFirstHeader(headerName);
        return header == null ? null : header.getValue();
    }

    protected int parseIntHeader(String headerName) {
        String headerValue = parseHeader(headerName);
        return headerValue == null ? -1 : Integer.parseInt(headerValue);
    }

    protected void handleResponse() {
        statusLine = response.getStatusLine();
        final int code = getStatusCode();

        if (isOk(code)) {
            return;
        }
        else if (!isEmpty(code))
            throw new RuntimeException(getResponseStatus());
    }

    protected boolean isOk(final int code) {
        return code >= HTTP_OK && code < HTTP_ACCEPTED;
    }

    protected boolean isEmpty(final int code) {
        return HTTP_NO_CONTENT == code;
    }
}
