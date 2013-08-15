package org.polyglotted.attributerepo.model;

public class RepoId implements java.io.Serializable {

    private static final long serialVersionUID = 5099339419509754479L;

    private String user;
    private String repo;

    public RepoId() {}

    public RepoId(String user, String repo) {
        setUser(user);
        setRepo(repo);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((repo == null) ? 0 : repo.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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

        RepoId other = (RepoId) obj;
        if ((repo == null) ? (other.repo != null) : !repo.equals(other.repo))
            return false;
        if ((user == null) ? (other.user != null) : !user.equals(other.user))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "user:" + user + ";repo:" + repo;
    }
}
