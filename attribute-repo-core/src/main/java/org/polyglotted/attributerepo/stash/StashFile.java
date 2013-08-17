package org.polyglotted.attributerepo.stash;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class StashFile implements java.io.Serializable {

    private static final long serialVersionUID = 6239121368026033814L;

    private List<Line> lines;
    private int start;
    private int size;
    private boolean isLastPage;

    @Setter
    public static class Line implements java.io.Serializable {
        private static final long serialVersionUID = -7464322084417691195L;

        private String text;

        @Override
        public String toString() {
            return text;
        }
    }
}
