package com.example.pdf.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity(name = "user_")
@Data
@NoArgsConstructor
public class User {

    @Id
    private Long chatId;

    private Language language;

    private String phoneNumber;

    private Timestamp registeredAt;

    private String firstName;

    private String lastName;

    private String userName;

    private Double latitude;

    private Double longitude;

    private String bio;

    private String description;

    private String pinnedMessage;

    @OneToMany(fetch = FetchType.LAZY)
    private List<File> files;
}
