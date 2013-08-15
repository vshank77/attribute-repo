package org.polyglotted.attributerepo.github;

import org.polyglotted.attributerepo.core.NotInstantiable;

class GithubConstants {

    public static final String API_GITHUB_COM = "api.github.com";

    public static final String SEGMENT_V3 = "/api/v3";
    public static final String SEGMENT_REPOS = "/repos";
    public static final String SEGMENT_CONTENTS = "/contents/";

    public static final String HEADER_XRATE_LIMIT = "X-RateLimit-Limit";
    public static final String HEADER_XRATE_REMAINING = "X-RateLimit-Remaining";

    public static final String PARAM_REF = "ref";

    public static final String ACCEPT_TYPE = "application/vnd.github.beta+json";

    public static final String ENCODED_BASE64 = "base64";

    @NotInstantiable
    private GithubConstants() {}
}
