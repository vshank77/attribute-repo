package org.polyglotted.attributerepo.git.common;

import org.apache.http.client.methods.*;

public enum RequestType {
    GET {
        @Override
        public HttpUriRequest createUriRequest(String uri) {
            return new HttpGet(uri);
        }
    },
    PUT {
        @Override
        public HttpUriRequest createUriRequest(String uri) {
            return new HttpPut(uri);
        }
    },
    POST {
        @Override
        public HttpUriRequest createUriRequest(String uri) {
            return new HttpPost(uri);
        }
    },
    DELETE {
        @Override
        public HttpUriRequest createUriRequest(String uri) {
            return new HttpDelete(uri);
        }
    };

    public abstract HttpUriRequest createUriRequest(String uri);
}