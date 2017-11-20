package wordcount.model;

import java.util.UUID;

public class Word implements Comparable<Word> {
    public String uuid;
    public String wordString;
    public Source source;

    public Word(final String wordString, final Source source) {
        this.uuid = UUID.randomUUID().toString();
        this.wordString = wordString;
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word = (Word) o;

        if (uuid != null ? !uuid.equals(word.uuid) : word.uuid != null) return false;
        if (wordString != null ? !wordString.equals(word.wordString) : word.wordString != null) return false;
        return source != null ? source.equals(word.source) : word.source == null;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (wordString != null ? wordString.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(final Word otherWord) {
        return this.wordString.compareTo(otherWord.wordString);
    }
}
