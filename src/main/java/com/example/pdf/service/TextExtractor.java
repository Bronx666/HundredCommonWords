package com.example.pdf.service;

import com.example.pdf.model.Word;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TextExtractor {

    private final String[] UNTRANSLATABLE_WORDS = new String[]{"the", "and", "you", "but", "can", "for", "are", "zero", "one",
            "two", "three", "four", "five", "six", "seven", "eight", "nine", "were", "this", "that", "not", "did", "too",
            "got", "don", "get", "yes", "had", "have", "your", "said", "was", "then", "they", "his", "him", "all", "what",
            "now"};



    public List<Word> extractTextToList(String filename) throws IOException {

        var text = extractText(filename);
        return countOccurrences(createObjectsFromWordSet(
                getUniqueWordsFromString(text), text), text);
    }

    public int getTotalLength(List<Word> words) {
        return words.stream()
                .map(Word::getOriginalWord)
                .mapToInt(String::length)
                .sum();
    }

    private String extractText(String filename) throws IOException {

        PdfReader reader = new PdfReader(filename);
        StringBuilder extractedText = new StringBuilder();
        for (int i = 1; i <= reader.getNumberOfPages(); ++i) {
            TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
            String text = PdfTextExtractor.getTextFromPage(reader, i, strategy);
            extractedText.append(text);
        }

        reader.close();
        return extractedText.toString();
    }

    private Set<String> getUniqueWordsFromString(String text) {
        return Arrays.stream(text.split("[^a-zA-Z]+"))
                .map(String::toLowerCase)
                .filter(word -> word.length() > 2)
                .filter(str -> !Arrays.asList(UNTRANSLATABLE_WORDS).contains(str))
                .collect(Collectors.toCollection(HashSet::new));

    }

    private List<Word> createObjectsFromWordSet(Set<String> uniqueWords, String text) {

        return uniqueWords.stream()
                .filter(text::contains)
                .collect(Collectors.groupingBy(Word::new))
                .keySet().stream()
                .sorted(Comparator.comparingInt(Word::getCount).reversed())
                .collect(Collectors.toList());
    }

    private List<Word> countOccurrences(List<Word> words, String text) {
        String[] array = text.split("[^a-zA-Z]+");
        for (Word word : words) {
            for (String w : array) {
                if (word.getOriginalWord().equalsIgnoreCase(w)) {
                    word.incrementCount();
                }
            }
        }

        return words.stream()
                .sorted(Comparator.comparing(Word::getCount))
                .collect(Collectors.toList());
    }


}