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
public class MessageCommandGenericValidator {

    private final CommandExistenceValidator commandExistenceValidator;
    private final RequiredFieldValidator requiredFieldValidator;

    public void validate(@NotNull final Message commandMessage) {
        final Long chatId = commandMessage.getChatId();
        final String command = commandMessage.getText();

        requiredFieldValidator.validate(chatId);
        requiredFieldValidator.validate(command);
        commandExistenceValidator.validate(command);
    }
}
