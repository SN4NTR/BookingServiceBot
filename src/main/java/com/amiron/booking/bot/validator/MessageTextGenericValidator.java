package com.amiron.booking.bot.validator;

import com.amiron.booking.core.validator.RequiredFieldValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class MessageTextGenericValidator {

    private final RequiredFieldValidator requiredFieldValidator;

    public void validate(@NotNull final Message message) {
        final Long chatId = message.getChatId();
        final String text = message.getText();

        requiredFieldValidator.validate(chatId);
        requiredFieldValidator.validate(text);
    }
}
