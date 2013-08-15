package org.polyglotted.attributerepo.stash;

import java.util.List;

public class StashFile implements java.io.Serializable {

    private static final long serialVersionUID = 6239121368026033814L;

    private List<Line> lines;
    private int start;
    private int size;
    private boolean isLastPage;

    public List<Line> getLines() {
        return lines;
    }

    public int getStart() {
        return start;
    }

    public int getSize() {
        return size;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public static class Line implements java.io.Serializable {
        private static final long serialVersionUID = -7464322084417691195L;

        private String text;

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
