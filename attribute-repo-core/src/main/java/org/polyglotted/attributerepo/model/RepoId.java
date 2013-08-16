package org.polyglotted.attributerepo.model;

import lombok.Data;

@Data
public class RepoId implements java.io.Serializable {

    private static final long serialVersionUID = 5099339419509754479L;

    private final String user;
    private final String repo;
}
