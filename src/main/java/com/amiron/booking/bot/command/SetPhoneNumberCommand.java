package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.BotCommand;
import com.amiron.booking.bot.util.KeyboardBuilder;
import com.amiron.booking.bot.util.MessageBuilder;
import com.amiron.booking.user.facade.UserFacade;
import com.amiron.booking.user.model.User;
import com.amiron.booking.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import javax.validation.constraints.NotNull;

import static com.amiron.booking.bot.model.BotCommand.SET_PHONE_NUMBER;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class SetPhoneNumberCommand extends Command<Contact> {

    private final UserService userService;
    private final UserFacade userFacade;

    @Override
    public BotApiMethod execute(@NotNull final Contact contact) {
        final Long userId = contact.getUserId();
        final String phoneNumber = contact.getPhoneNumber();

        final User user = setPhoneNumberForExistingUser(userId, phoneNumber);
        userFacade.onUpdate(user);

        final Long chatId = user.getChatId();
        return buildResponseMessage(chatId);
    }

    @Override
    public BotCommand getCommand() {
        return SET_PHONE_NUMBER;
    }

    private User setPhoneNumberForExistingUser(final Long userId, final String phoneNumber) {
        final User existingUser = userService.getById(userId);
        existingUser.setPhoneNumber(phoneNumber);
        return existingUser;
    }

    private SendMessage buildResponseMessage(final Long chatId) {
        final ReplyKeyboardRemove keyboardRemove = KeyboardBuilder.buildKeyboardRemove();
        return MessageBuilder.buildSendMessage(String.valueOf(chatId), "Please enter your email.", keyboardRemove);
    }
}
