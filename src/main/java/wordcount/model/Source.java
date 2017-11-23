package wordcount.model;

public class Source {
    public String filename;
    public String processedByThread;

    public Source(final String filename) {
        this.filename = filename;
    }

    public Source(final String filename, final String processedByThread) {
        this.filename = filename;
        this.processedByThread = processedByThread;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Source source = (Source) o;

        if (filename != null ? !filename.equals(source.filename) : source.filename != null) return false;
        return processedByThread != null ? processedByThread.equals(source.processedByThread) : source.processedByThread == null;
    }

    @Override
    public int hashCode() {
        int result = filename != null ? filename.hashCode() : 0;
        result = 31 * result + (processedByThread != null ? processedByThread.hashCode() : 0);
        return result;
    }
}
