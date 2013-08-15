package org.polyglotted.attributerepo.github;

class GithubFile implements java.io.Serializable {

    private static final long serialVersionUID = -3883458299963101911L;

    private String name;
    private String path;
    private String sha;
    private int size;
    private String type;
    private String content;
    private String encoding;

    public GithubFile() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + ((sha == null) ? 0 : sha.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        GithubFile other = (GithubFile) obj;
        if ((path == null) ? (other.path != null) : !path.equals(other.path))
            return false;
        if ((sha == null) ? (other.sha != null) : !sha.equals(other.sha))
            return false;
        return true;
    }
}
