package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.UserCommand;
import lombok.Data;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Aliaksandr Miron
 */
@Data
public abstract class BotCommand<T extends BotApiObject> {

    public abstract List<? extends PartialBotApiMethod<?>> execute(@NotNull final T payload);

    public abstract UserCommand getCommand();
}
