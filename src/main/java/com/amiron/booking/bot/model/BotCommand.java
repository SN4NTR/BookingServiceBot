package com.amiron.booking.bot.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Aliaksandr Miron
 */
@Getter
public enum BotCommand {

    START("/start"),
    SET_EMAIL("/setEmail"),
    SET_PHONE_NUMBER("/setPhoneNumber"),
    BOOK_NAILS("/bookNails"),
    UNKNOWN("unknown");

    private final String value;

    BotCommand(final String value) {
        this.value = value;
    }

    public static Optional<BotCommand> findByValue(final String value) {
        return Arrays.stream(BotCommand.values())
                .filter(command -> command.getValue().equals(value))
                .findFirst();
    }

    public static boolean containsValue(final String value) {
        return Arrays.stream(BotCommand.values())
                .anyMatch(command -> command.getValue().equals(value));
    }

    public static boolean doesNotContainValue(final String value) {
        return !containsValue(value);
    }
}
