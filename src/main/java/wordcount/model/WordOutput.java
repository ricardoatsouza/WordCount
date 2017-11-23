package wordcount.model;

public class WordOutput {
    public String word;
    public String filename;

    public WordOutput(String word) {
        this.word = word;
    }

    public WordOutput(final String word, final String filename) {
        this(word);
        this.filename = filename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordOutput rowOutput = (WordOutput) o;

        if (word != null ? !word.equals(rowOutput.word) : rowOutput.word != null) return false;
        return filename != null ? filename.equals(rowOutput.filename) : rowOutput.filename == null;
    }

    @Override
    public int hashCode() {
        int result = word != null ? word.hashCode() : 0;
        result = 31 * result + (filename != null ? filename.hashCode() : 0);
        return result;
    }
}