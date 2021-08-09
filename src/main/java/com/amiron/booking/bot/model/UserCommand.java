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
    BOOKINGS("/account/bookings"),
    SET_EMAIL("/account/setEmail"),
    CHANGE_EMAIL("/account/changeEmail"),
    SET_PHONE_NUMBER("/account/setPhoneNumber"),
    CHANGE_PHONE_NUMBER("/account/changePhoneNumber"),
    CHOOSE_SERVICES("/services"),
    CHOOSE_NAILS("/services/nails"),
    CHOOSE_MASTER("/masters/%s"),
    CHOOSE_MONTH("/month/email=%s&d=%s&m=%s&y=%s"),
    CHOOSE_DAY("/day/email=%s&d=%s&m=%s&y=%s"),
    CHOOSE_TIME("/time/email=%s&d=%s&m=%s&y=%s&h=&mm=%s");

    private final String patternTemplate;

    UserCommand(final String patternTemplate) {
        this.patternTemplate = patternTemplate;
    }
}
