package com.amiron.booking.bot.facade;

import com.amiron.booking.bot.model.BotUpdateType;
import com.amiron.booking.bot.service.BotUpdateTypeResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.Collections.singletonList;

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
    public List<? extends PartialBotApiMethod<?>> onUpdateEvent(@NotNull final Update update) {
        return processUpdatesPayload(update);
    }

    private List<? extends PartialBotApiMethod<?>> processUpdatesPayload(final Update update) {
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

    private List<? extends PartialBotApiMethod<?>> processCommand(final Message commandMessage) {
        return messageCommandFacade.onReceive(commandMessage);
    }

    private List<? extends PartialBotApiMethod<?>> processText(final Message message) {
        return messageTextFacade.onReceive(message);
    }

    private List<? extends PartialBotApiMethod<?>> processCallbackQuery(final CallbackQuery callbackQuery) {
        return callbackQueryFacade.onReceive(callbackQuery);
    }

    private List<? extends PartialBotApiMethod<?>> processContact(final Message contactMessage) {
        final Contact contact = contactMessage.getContact();
        return contactFacade.onReceive(contact);
    }

    private List<? extends PartialBotApiMethod<?>> processRestart(final Message message) {
        return null;
    }

    private List<? extends PartialBotApiMethod<?>> processStop(final Message message) {
        return null;
    }

    private List<? extends PartialBotApiMethod<?>> processUnknownType(final Message message) {
        final Long chatId = message.getChatId();
        final SendMessage sendMessage = new SendMessage(String.valueOf(chatId), "Unknown command.");
        return singletonList(sendMessage);
    }
}
