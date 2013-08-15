package org.polyglotted.attributerepo.git.common;

import static com.google.gson.stream.JsonToken.BEGIN_ARRAY;
import static java.net.HttpURLConnection.HTTP_ACCEPTED;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_GONE;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.polyglotted.attributerepo.git.common.GitUtils.GSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.entity.BufferedHttpEntity;
import org.polyglotted.attributerepo.core.Response;

import com.google.common.base.Strings;
import com.google.gson.stream.JsonReader;

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
            return "Unknown error occurred (" + code + ')';
    }

    @Override
    public String getMeta(String headerName) {
        return parseHeader(headerName);
    }

    @Override
    public <V> V getResult(Type type) {
        return getResult(type, null);
    }

    @Override
    public <V> V getResult(Type type, Type listType) {
        InputStream inputStream = null;
        try {
            inputStream = new BufferedHttpEntity(response.getEntity()).getContent();
            Reader responseReader = new BufferedReader(new InputStreamReader(inputStream, GitConstants.CHARSET_UTF8));
            JsonReader jsonReader = new JsonReader(responseReader);

            if (listType != null && jsonReader.peek() == BEGIN_ARRAY)
                return GSON.fromJson(jsonReader, listType);

            else if (type == null)
                return null;

            return GSON.fromJson(jsonReader, type);

        }
        catch (Exception ioe) {
            throw new RuntimeException("failed to parse response", ioe);

        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException ignored) {}
            }
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
        try {
            Header header = response.getFirstHeader(headerName);
            return header == null ? -1 : Integer.parseInt(header.getValue());
        }
        catch (Exception ex) {
            return -1;
        }
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

    protected boolean isError(final int code) {
        switch (code) {
            case HTTP_BAD_REQUEST:
            case HTTP_UNAUTHORIZED:
            case HTTP_FORBIDDEN:
            case HTTP_NOT_FOUND:
            case HTTP_CONFLICT:
            case HTTP_GONE:
            case HTTP_UNPROCESSABLE_ENTITY:
            case HTTP_INTERNAL_ERROR:
                return true;
            default:
                return false;
        }
    }

    protected boolean isOk(final int code) {
        switch (code) {
            case HTTP_OK:
            case HTTP_CREATED:
            case HTTP_ACCEPTED:
                return true;
            default:
                return false;
        }
    }

    protected boolean isEmpty(final int code) {
        return HTTP_NO_CONTENT == code;
    }
}
