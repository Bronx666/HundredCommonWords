package com.example.pdf.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity(name = "word")
public class Word {

    @Id
    private Long id;
    private String originalWord;
    private String translatedWord;
    private int count;

    public Word(String originalWord){
        this.originalWord = originalWord;
        this.count = 0;
    }

    public void incrementCount(){
        this.count++;
    }


}
