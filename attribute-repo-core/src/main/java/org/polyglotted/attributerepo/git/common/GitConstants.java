package org.polyglotted.attributerepo.git.common;

import org.polyglotted.attributerepo.core.NotInstantiable;

public class GitConstants {

    public static final String HTTP_SCHEME = "http";
    public static final String HTTPS_SCHEME = "https";

    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_AUTHORIZATION = "Authorization";

    public static final String USER_AGENT = "AttribRepo/1.0.0";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String DEFAULT_CONTENT_TYPE = CONTENT_TYPE_JSON + "; charset=UTF-8";
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
    public static final String AUTH_TOKEN = "token ";
    public static final String BASIC_AUTH = "Basic ";

    public static final String CREDENTIALS = "credentials";
    public static final String URL_PREFIX = "urlPrefix";
    
    @NotInstantiable
    private GitConstants() {
    }
}
