package com.example.pdf;

import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@AutoConfigurationPackage
public class PdfApplication {

    public static void main(String[] args){

        new SpringApplicationBuilder(PdfApplication.class).run(args);
    }
}
