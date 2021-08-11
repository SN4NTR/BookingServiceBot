package com.amiron.booking.bot.handler;

import com.amiron.booking.bot.config.BotConfigProperties;
import com.amiron.booking.bot.facade.BotFacade;
import com.amiron.booking.bot.model.MessageType;
import com.amiron.booking.bot.resolver.BotMessageTypeResolver;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Component
@Slf4j
public class BotHandler extends TelegramLongPollingBot {

    private static final long DELAY_IN_MILLIS = 300;

    private final BotConfigProperties botConfigurationProperties;
    private final BotFacade botFacade;
    private final BotMessageTypeResolver botMessageTypeResolver;

    @Override
    public String getBotUsername() {
        return botConfigurationProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botConfigurationProperties.getToken();
    }

    @Override
    public void onUpdateReceived(final Update update) {
        final List<? extends PartialBotApiMethod<?>> messages = botFacade.onUpdateEvent(update);

        sendMessages(messages);
    }

    @SneakyThrows
    public void sendMessages(final List<? extends PartialBotApiMethod<?>> messages) {
        messages.forEach(this::sendMessage);
    }

    // TODO correct photo sending (something with content)
    @SneakyThrows
    public void sendMessage(final PartialBotApiMethod<?> message) {
        Thread.sleep(DELAY_IN_MILLIS);

        final MessageType messageType = botMessageTypeResolver.resolve(message);
        switch (messageType) {
            case TEXT -> execute((BotApiMethod<?>) message);
            case PHOTO -> execute((SendPhoto) message);
            default -> throw new RuntimeException();
        }
    }
}
