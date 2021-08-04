package com.amiron.booking.bot.model;

import java.util.Arrays;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Aliaksandr Miron
 */
public enum BotCommand {

    BOOKINGS("/bookings"),
    ACCOUNT("/account"),
    MENU("/menu"),
    START("/start"),
    SET_EMAIL("/setEmail"),
    SET_PHONE_NUMBER("/setPhoneNumber"),
    BOOK_NAILS("/bookNails"),
    BOOK_MASTER("/bookMaster/%s"),
    UNKNOWN("unknown");

    private final String value;

    BotCommand(final String value) {
        this.value = value;
    }

    public static Optional<BotCommand> findByValue(final String value) {
        return Arrays.stream(BotCommand.values())
                .filter(command -> command.getFormattedValue().equals(value))
                .findFirst();
    }

    public static boolean containsValue(final String value) {
        return Arrays.stream(BotCommand.values())
                .anyMatch(command -> command.getFormattedValue().equals(value));
    }

    public static boolean doesNotContainValue(final String value) {
        return !containsValue(value);
    }

    public String getOriginValue() {
        return value;
    }

    public String getFormattedValue() {
        if (value.contains("/%s")) {
            return value.replace("/%s", EMPTY);
        }
        return value;
    }
}
