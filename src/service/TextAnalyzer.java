package service;

import exceptions.InvalidInputException;

import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

public class TextAnalyzer {
    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "in", "on", "at", "to", "of", "and", "or", "is", "are"
    );

    // 1. Word Frequency Analysis
    public static Map<String, Long> analyzeWordFrequency(String text) throws InvalidInputException {
        try {

        } catch (Exception e) {
            throw new InvalidInputException("Failure to extract word frequency from this text.\n Try using inputs that has context");

        }
        return Arrays.stream(text.split("\\W+"))
                .parallel()
                .filter(word -> !word.isEmpty())
                .map(String::toLowerCase)
                .filter(word -> !STOP_WORDS.contains(word))
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
    }

    // 2. Pattern Recognition
    public static Map<String, List<String>> recognizePatterns(String text, Map<String, Pattern> patterns) {
        return patterns.entrySet().parallelStream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> extractPatternMatches(text, entry.getValue())
                ));
    }

    private static List<String> extractPatternMatches(String text, Pattern pattern) {
        return pattern.matcher(text).results()
                .map(MatchResult::group)
                .collect(Collectors.toList());
    }

    // 3. Text Summarization
    public static String generateSummary(String text, int sentenceCount) throws InvalidInputException {
        try{
            Map<String, Long> wordFrequencies = analyzeWordFrequency(text);

            List<String> sentences = Arrays.asList(text.split("[.!?]+"));

            for (String sentence : sentences) {
                System.out.println("Sentence: " + sentence+" Score: "+calculateSentenceScore(sentence, wordFrequencies));
            }

            Map<String, Double> sentenceScores = sentences.parallelStream()
                    .collect(Collectors.toMap(
                            Function.identity(),
                            sentence -> calculateSentenceScore(sentence, wordFrequencies)
                    ));

            return sentenceScores.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(sentenceCount)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.joining(". "));
        } catch (Exception e) {
            throw new InvalidInputException("This text is not compatible for summarization.\n Try using inputs that has context");
        }
    }

    private static Double calculateSentenceScore(String sentence, Map<String, Long> wordFrequencies) {
        return Arrays.stream(sentence.split("\\W+"))
                .map(String::toLowerCase)
                .mapToDouble(word -> wordFrequencies.getOrDefault(word, 0L))
                .average()
                .orElse(0.0);
    }

    /*
    // Example Usage
    public static void main(String[] args) {
        String sampleText = "The quick brown fox jumps over the lazy dog. " +
                "A quick observation: foxes are clever animals. " +
                "Dog owners should watch their pets when foxes are nearby.";

        // 1. Word Frequency Analysis
        Map<String, Long> frequencies = analyzeWordFrequency(sampleText);
        System.out.println("Word Frequencies:");
        frequencies.forEach((word, count) -> System.out.println(word + ": " + count));

        // 2. Pattern Recognition
        Map<String, Pattern> patterns = new HashMap<>();
        patterns.put("animal", Pattern.compile("(fox|dog)", Pattern.CASE_INSENSITIVE));
        patterns.put("adjective", Pattern.compile("\\b\\w+ly\\b")); // Adverbs

        Map<String, List<String>> matches = recognizePatterns(sampleText, patterns);
        System.out.println("\nPattern Matches:");
        matches.forEach((pattern, results) -> System.out.println(pattern + ": " + results));

        // 3. Text Summarization
        String summary = generateSummary(sampleText, 2);
        System.out.println("\nSummary:");
        System.out.println(summary);
    }

     */
}