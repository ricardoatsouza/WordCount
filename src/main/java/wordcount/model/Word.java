package simplemapreduce.tmp.wordcount.model;

public class Word {
    public String word;
    public Source source;

    public Word(String word, Source source) {
        this.word = word;
        this.source = source;
    }
}
