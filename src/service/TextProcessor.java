package service;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TextProcessor {
    public String cleanText(String input) {
        return input.replaceAll("[^a-zA-Z0-9\s]", "").toLowerCase();
    }

    public Map<String, Long> wordFrequency(String input) {
        return Arrays.stream(input.split("\\s+"))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public String summarizeText(String input) {
        String[] sentences = input.split("(?<=[.!?])\\s*");
        return sentences.length > 0 ? sentences[0] : input;
    }
}