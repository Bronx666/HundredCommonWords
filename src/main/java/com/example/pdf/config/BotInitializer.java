package com.example.pdf.config;

import com.example.pdf.service.TelegramBot;
import lombok.AllArgsConstructor;
import org.springframework.context.event.*;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.*;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@AllArgsConstructor
public class BotInitializer {

    TelegramBot bot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            throw new RuntimeException("Error of bot initialize",e);
        }
    }
}
