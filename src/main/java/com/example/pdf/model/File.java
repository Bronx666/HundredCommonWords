package com.example.pdf.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "file")
public class File {

    @Id()
    private Long id;
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Word> words;

}
