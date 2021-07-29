package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.BotCommand;
import lombok.Data;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
@Data
public abstract class Command<T extends BotApiObject> {

    public abstract BotApiMethod execute(@NotNull final T payload);

    public abstract BotCommand getCommand();
}
