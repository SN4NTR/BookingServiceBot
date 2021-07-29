package com.amiron.booking.bot.facade;

import com.amiron.booking.bot.model.BotUpdateType;
import com.amiron.booking.bot.service.BotUpdateTypeResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class BotFacadeImpl implements BotFacade {

    private final MessageCommandFacade messageCommandFacade;
    private final CallbackQueryFacade callbackQueryFacade;
    private final ContactFacade contactFacade;
    private final BotUpdateTypeResolver botUpdateTypeResolver;
    private final MessageTextFacade messageTextFacade;

    @Override
    public BotApiMethod<?> onUpdateEvent(@NotNull final Update update) {
        return processUpdatesPayload(update);
    }

    private BotApiMethod<?> processUpdatesPayload(final Update update) {
        final BotUpdateType botUpdateType = botUpdateTypeResolver.resolve(update);
        return switch (botUpdateType) {
            case COMMAND -> processCommand(update.getMessage());
            case TEXT -> processText(update.getMessage());
            case CALLBACK_QUERY -> processCallbackQuery(update.getCallbackQuery());
            case CONTACT -> processContact(update.getMessage());
            case RESTART -> processRestart(update.getMessage());
            case STOP -> processStop(update.getMessage());
            default -> processUnknownType(update.getMessage());
        };
    }

    private BotApiMethod<?> processCommand(final Message commandMessage) {
        return messageCommandFacade.onReceive(commandMessage);
    }

    private BotApiMethod<?> processText(final Message message) {
        return messageTextFacade.onReceive(message);
    }

    private BotApiMethod<?> processCallbackQuery(final CallbackQuery callbackQuery) {
        return callbackQueryFacade.onReceive(callbackQuery);
    }

    private BotApiMethod<?> processContact(final Message contactMessage) {
        final Contact contact = contactMessage.getContact();
        return contactFacade.onReceive(contact);
    }

    private BotApiMethod<?> processRestart(final Message message) {
        return null;
    }

    private BotApiMethod<?> processStop(final Message message) {
        return null;
    }

    private BotApiMethod<?> processUnknownType(final Message message) {
        final Long chatId = message.getChatId();
        return new SendMessage(String.valueOf(chatId), "Unknown command.");
    }
}
