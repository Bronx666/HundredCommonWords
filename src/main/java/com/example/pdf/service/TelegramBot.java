package com.example.pdf.service;

import com.example.pdf.config.BotConfig;
import com.example.pdf.model.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private BotConfig config;
    @Autowired
    private UserRepository userRepository;

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/translate_pdf", "send your file to search for unique words"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }


    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        // Set variables
        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (messageText) {

                case "/start" -> {
                    registerUser(update.getMessage());
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                }
                case "/translate_pdf" -> sendMessage("Send your pdf", chatId);
                default -> sendMessage("Something went wrong, try again!", chatId);

            }

        } else if (update.hasMessage() && update.getMessage().hasDocument()) {

            com.example.pdf.model.File file = new com.example.pdf.model.File();
            TextExtractor textExtractor = new TextExtractor();

            //Getting file from user and creating a class instance
            file.setName(downloadAndSaveFileToRootDirectory(update));
            file.setWords(textExtractor.extractTextToList(file.getName()));

            //Translating of unique words
            DeeplTranslate deeplTranslate = new DeeplTranslate();
            var test = deeplTranslate.getTranslateWordList(file.getWords());
            //Building request
            var message = test.stream()
                    .map(word -> word.getOriginalWord() + " - " + word.getTranslatedWord())
                    .collect(Collectors.joining("\n"));

            sendMessage(message, update.getMessage().getChatId());
        }

    }

    private void registerUser(Message msg) {

        if(userRepository.findById(msg.getChatId()).isEmpty()){

            var chatId = msg.getChatId();
            var chat = msg.getChat();

            com.example.pdf.model.User user = new com.example.pdf.model.User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));


            userRepository.save(user);
            log.info("user saved: " + user);
        }
    }
    private void startCommandReceived(long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!";
        log.info("Replied to user " + name);

        sendMessage(answer, chatId);
    }
    private String downloadAndSaveFileToRootDirectory(Update update) {
        String fileId = update.getMessage().getDocument().getFileId();
        String fileName = update.getMessage().getDocument().getFileName();
        String mimeType = update.getMessage().getDocument().getMimeType();

        Document document = new Document();
        document.setMimeType(mimeType);
        document.setFileName(fileName);
        document.setFileId(fileId);

        GetFile getFile = new GetFile();
        getFile.setFileId(document.getFileId());
        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
            downloadFile(file, new java.io.File("/resources/" + fileName));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    private void sendMessage(String textToSend, long chatId) {
        SendMessage message = new SendMessage(); // Create a message object
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        send(message);
    }

    private void send(SendMessage msg) {
        try {
            execute(msg); // Sending our message object to user
        } catch (TelegramApiException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }


}