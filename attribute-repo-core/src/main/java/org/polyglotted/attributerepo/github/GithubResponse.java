package org.polyglotted.attributerepo.github;

import static org.polyglotted.attributerepo.github.GithubConstants.HEADER_XRATE_LIMIT;
import static org.polyglotted.attributerepo.github.GithubConstants.HEADER_XRATE_REMAINING;
import lombok.Getter;

import org.apache.http.HttpResponse;
import org.polyglotted.attributerepo.git.common.DefaultResponse;

/**
 * A response class that handles github specific headers
 * 
 * @author Shankar Vasudevan
 */
@Getter
public class GithubResponse extends DefaultResponse {

    private int remainingRequests = -1;
    private int requestLimit = -1;

    public GithubResponse(HttpResponse response) {
        super(response);
    }

    @Override
    protected void handleHeaders() {
        requestLimit = parseIntHeader(HEADER_XRATE_LIMIT);
        remainingRequests = parseIntHeader(HEADER_XRATE_REMAINING);
    }
}
