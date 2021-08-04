package com.amiron.booking.bot.validator;

import com.amiron.booking.core.validator.RequiredFieldValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class CallbackQueryGenericValidator {

    private final RequiredFieldValidator requiredFieldValidator;

    public void validate(@NotNull final CallbackQuery callbackQuery) {
        final Message message = callbackQuery.getMessage();
        final Long chatId = message.getChatId();
        final String data = callbackQuery.getData();

        requiredFieldValidator.validate(chatId);
        requiredFieldValidator.validate(data);
    }
}
