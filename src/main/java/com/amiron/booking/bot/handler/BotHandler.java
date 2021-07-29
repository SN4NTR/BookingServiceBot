package com.amiron.booking.bot.handler;

import com.amiron.booking.bot.config.BotConfigProperties;
import com.amiron.booking.bot.facade.BotFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Component
@Slf4j
public class BotHandler extends TelegramLongPollingBot {

    private final BotConfigProperties botConfigurationProperties;
    private final BotFacade botFacade;

    @Override
    public String getBotUsername() {
        return botConfigurationProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botConfigurationProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        final BotApiMethod<?> message = botFacade.onUpdateEvent(update);
        sendMessage(message);
    }

    private void sendMessage(final BotApiMethod<?> message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
