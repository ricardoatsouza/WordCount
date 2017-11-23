package wordcount;

import wordcount.model.Word;
import wordcount.model.WordOutput;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SharedCache {

    private final Set<Word> wordSet = new HashSet<>();

    public void addToMap(final Word word) {
        wordSet.add(word);
    }

    public Map<WordOutput, Long> reduceByWord() {
        return wordSet.stream().collect(
                Collectors.groupingBy(word -> new WordOutput(word.wordString), Collectors.counting())
        );
    }

    public Map<WordOutput, Long> reduceByWordAndFilename() {
        return wordSet.stream().collect(
                Collectors.groupingBy(word -> new WordOutput(word.wordString, word.source.filename), Collectors.counting())
        );
    }

}
