package com.amiron.booking.bot.model;

import java.util.Arrays;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Aliaksandr Miron
 */
public enum UserCommand {

    START("/start"),
    MENU("/menu"),
    ACCOUNT("/account"),
    BOOKINGS("/account/bookings"),
    SET_EMAIL("/account/setEmail"),
    CHANGE_EMAIL("/account/changeEmail"),
    SET_PHONE_NUMBER("/account/setPhoneNumber"),
    CHANGE_PHONE_NUMBER("/account/changePhoneNumber"),
    CHOOSE_SERVICES("/services"),
    CHOOSE_NAILS("/services?name=chooseNails"),
    CHOOSE_MASTER("/masters/%s"),
    CHOOSE_MONTH("/masters/%s/calendars?day=%s&month=%s&year=%s"),
    CHOOSE_DAY("/masters/%s/calendars/%s/day?day=%s&month=%s&year=%s"),
    UNKNOWN("unknown");

    private final String value;

    UserCommand(final String value) {
        this.value = value;
    }

    public static Optional<UserCommand> findByValue(final String value) {
        return Arrays.stream(UserCommand.values())
                .filter(command -> command.getFormattedValue().equals(value))
                .findFirst();
    }

    public static boolean containsValue(final String value) {
        return Arrays.stream(UserCommand.values())
                .anyMatch(command -> command.getFormattedValue().equals(value));
    }

    public static boolean doesNotContainValue(final String value) {
        return !containsValue(value);
    }

    public String getOriginValue() {
        return value;
    }

    public String getFormattedValue() {
        return value.contains("/%s")
                ? value.replace("/%s", EMPTY)
                : value;
    }
}
