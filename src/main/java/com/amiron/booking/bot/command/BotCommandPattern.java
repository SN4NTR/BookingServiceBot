package com.amiron.booking.bot.command;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.amiron.booking.core.constant.RegexConstants.DAY_OF_MONTH_PATTERN;
import static com.amiron.booking.core.constant.RegexConstants.EMAIL_PATTERN;
import static com.amiron.booking.core.constant.RegexConstants.HOURS_PATTERN;
import static com.amiron.booking.core.constant.RegexConstants.MINUTES_PATTERN;
import static com.amiron.booking.core.constant.RegexConstants.MONTH_PATTERN;
import static com.amiron.booking.core.constant.RegexConstants.UUID_PATTERN;
import static com.amiron.booking.core.constant.RegexConstants.YEAR_PATTERN;
import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

/**
 * @author Aliaksandr Miron
 */
@Getter
public enum BotCommandPattern {

    START("/start"),
    MENU("/menu"),
    ACCOUNT("/account"),
    GET_BOOKINGS("/bookings"),
    CANCEL_BOOKING("/booking/cancel/%s", UUID_PATTERN),
    SET_EMAIL("/setEmail"),
    CHANGE_EMAIL("/changeEmail"),
    SET_PHONE_NUMBER("/setPhoneNumber"),
    CHANGE_PHONE_NUMBER("/changePhoneNumber"),
    CHOOSE_SERVICES("/services"),
    CHOOSE_NAILS("/services/nails"),
    CHOOSE_MASTER("/masters/%s", UUID_PATTERN),
    CHOOSE_MONTH("/month/e=%s&d=%s&m=%s&y=%s", EMAIL_PATTERN, DAY_OF_MONTH_PATTERN, MONTH_PATTERN, YEAR_PATTERN),
    CHOOSE_DAY("/day/e=%s&d=%s&m=%s&y=%s", EMAIL_PATTERN, DAY_OF_MONTH_PATTERN, MONTH_PATTERN, YEAR_PATTERN),
    CHOOSE_TIME("/time/e=%s&d=%s&m=%s&y=%s&h=%s&mm=%s", EMAIL_PATTERN, DAY_OF_MONTH_PATTERN, MONTH_PATTERN, YEAR_PATTERN, HOURS_PATTERN, MINUTES_PATTERN),
    BOOK_TIME("/book/e=%s&d=%s&m=%s&y=%s&h=%s&mm=%s", EMAIL_PATTERN, DAY_OF_MONTH_PATTERN, MONTH_PATTERN, YEAR_PATTERN, HOURS_PATTERN, MINUTES_PATTERN);

    private final String patternTemplate;
    private final Pattern pattern;

    BotCommandPattern(final String patternTemplate, final Object... parameters) {
        this.patternTemplate = patternTemplate;
        this.pattern = compile(format(patternTemplate, parameters));
    }

    public static Optional<BotCommandPattern> findBotPatternByTextCommand(final String textCommand) {
        return Arrays.stream(values())
                .filter(value -> matchesPattern(value.getPattern(), textCommand))
                .findFirst();
    }

    private static boolean matchesPattern(final Pattern pattern, final String textCommand) {
        return pattern.matcher(textCommand).matches();
    }
}
