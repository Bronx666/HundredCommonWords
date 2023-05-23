package com.example.pdf.service;

import com.deepl.api.*;
import com.example.pdf.model.Word;

import java.io.FileInputStream;
import java.util.*;

public class DeeplTranslate {
    Translator translator;

    public List<Word> getTranslateWordList(List<Word> words) throws Exception {
       for (Word word:words){
           word.setTranslatedWord(translateText(word.getOriginalWord()));
       }
        return words;

    }
    public String translateText(String text) throws Exception {
        Properties property = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/application.properties");
        property.load(fis);
        String authKey = property.getProperty("deepl.token");
        translator = new Translator(authKey);
        TextResult result =
                translator.translateText(text, null, "ru");
        return result.getText();
    }
}
