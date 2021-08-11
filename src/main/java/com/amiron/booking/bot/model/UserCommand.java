package com.amiron.booking.bot.model;

import lombok.Getter;

/**
 * @author Aliaksandr Miron
 */
@Getter
public enum UserCommand {

    START("/start"),
    MENU("/menu"),
    ACCOUNT("/account"),
    GET_BOOKINGS("/bookings"),
    CANCEL_BOOKING("/booking/cancel/%s"),
    SET_EMAIL("/setEmail"),
    CHANGE_EMAIL("/changeEmail"),
    SET_PHONE_NUMBER("/setPhoneNumber"),
    CHANGE_PHONE_NUMBER("/changePhoneNumber"),
    CHOOSE_SERVICES("/services"),
    CHOOSE_NAILS("/services/nails"),
    CHOOSE_MASTER("/masters/%s"),
    CHOOSE_MONTH("/month/e=%s&d=%s&m=%s&y=%s"),
    CHOOSE_DAY("/day/e=%s&d=%s&m=%s&y=%s"),
    CHOOSE_TIME("/time/e=%s&d=%s&m=%s&y=%s&h=%s&mm=%s"),
    BOOK_TIME("/book/e=%s&d=%s&m=%s&y=%s&h=%s&mm=%s");

    private final String patternTemplate;

    UserCommand(final String patternTemplate) {
        this.patternTemplate = patternTemplate;
    }
}
