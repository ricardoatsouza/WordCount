package wordcount;

import wordcount.model.Word;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SharedCache {

    private final Map<Word, Integer> map = new HashMap<>();

    public void addToMap(final Word word) {
        map.put(word, 1);
    }

    public Map<String, Long> reduceByWord() {
        return map.entrySet().stream().map(Map.Entry::getKey).collect(
            Collectors.groupingBy(word -> word.wordString, Collectors.counting())
        );
    }

}
