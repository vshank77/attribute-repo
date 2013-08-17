package org.polyglotted.attributerepo.github;

import static org.polyglotted.attributerepo.github.GithubConstants.ENCODED_BASE64;
import static org.springframework.util.StringUtils.isEmpty;
import lombok.Getter;
import lombok.Setter;

import org.polyglotted.attributerepo.git.common.GitUtils;

@Getter
@Setter
class GithubFile implements java.io.Serializable {

    private static final long serialVersionUID = -3883458299963101911L;

    private String name;
    private String path;
    private String sha;
    private int size;
    private String type;
    private String content;
    private String encoding;

    public String getDecodedContent() {
        String contentStr = getContent();
        if (!isEmpty(contentStr) && ENCODED_BASE64.equals(getEncoding())) {
            return GitUtils.fromBase64(contentStr);
        }
        return contentStr;
    }
}
