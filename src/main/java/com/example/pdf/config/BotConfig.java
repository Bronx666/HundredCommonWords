package com.example.pdf.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;


@Data
@Configuration
@PropertySource("classpath:application.properties")
public class BotConfig {

    @Value("${telegram.botName}")
    String name;

    @Value("${telegram.botToken}")
    String token;
}

