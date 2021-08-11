package com.amiron.booking.bot.model;

import lombok.NoArgsConstructor;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.amiron.booking.bot.model.UserCommand.ACCOUNT;
import static com.amiron.booking.bot.model.UserCommand.BOOK_TIME;
import static com.amiron.booking.bot.model.UserCommand.CANCEL_BOOKING;
import static com.amiron.booking.bot.model.UserCommand.CHANGE_EMAIL;
import static com.amiron.booking.bot.model.UserCommand.CHANGE_PHONE_NUMBER;
import static com.amiron.booking.bot.model.UserCommand.CHOOSE_DAY;
import static com.amiron.booking.bot.model.UserCommand.CHOOSE_MASTER;
import static com.amiron.booking.bot.model.UserCommand.CHOOSE_MONTH;
import static com.amiron.booking.bot.model.UserCommand.CHOOSE_NAILS;
import static com.amiron.booking.bot.model.UserCommand.CHOOSE_SERVICES;
import static com.amiron.booking.bot.model.UserCommand.CHOOSE_TIME;
import static com.amiron.booking.bot.model.UserCommand.GET_BOOKINGS;
import static com.amiron.booking.bot.model.UserCommand.MENU;
import static com.amiron.booking.bot.model.UserCommand.SET_EMAIL;
import static com.amiron.booking.bot.model.UserCommand.SET_PHONE_NUMBER;
import static com.amiron.booking.bot.model.UserCommand.START;
import static com.amiron.booking.core.constant.RegexConstants.DAY_OF_MONTH_PATTERN;
import static com.amiron.booking.core.constant.RegexConstants.EMAIL_PATTERN;
import static com.amiron.booking.core.constant.RegexConstants.HOURS_PATTERN;
import static com.amiron.booking.core.constant.RegexConstants.MINUTES_PATTERN;
import static com.amiron.booking.core.constant.RegexConstants.MONTH_PATTERN;
import static com.amiron.booking.core.constant.RegexConstants.UUID_PATTERN;
import static com.amiron.booking.core.constant.RegexConstants.YEAR_PATTERN;
import static java.lang.String.format;
import static java.util.regex.Pattern.compile;
import static lombok.AccessLevel.PRIVATE;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class UserCommandPatternHolder {

    public static final Map<UserCommand, Pattern> USER_COMMANDS_PATTERNS = new EnumMap<>(UserCommand.class);

    static {
        USER_COMMANDS_PATTERNS.put(START, compile(START.getPatternTemplate()));
        USER_COMMANDS_PATTERNS.put(MENU, compile(MENU.getPatternTemplate()));
        USER_COMMANDS_PATTERNS.put(ACCOUNT, compile(ACCOUNT.getPatternTemplate()));
        USER_COMMANDS_PATTERNS.put(GET_BOOKINGS, compile(GET_BOOKINGS.getPatternTemplate()));
        USER_COMMANDS_PATTERNS.put(CANCEL_BOOKING, compile(format(CANCEL_BOOKING.getPatternTemplate(), UUID_PATTERN)));
        USER_COMMANDS_PATTERNS.put(SET_EMAIL, compile(SET_EMAIL.getPatternTemplate()));
        USER_COMMANDS_PATTERNS.put(CHANGE_EMAIL, compile(CHANGE_EMAIL.getPatternTemplate()));
        USER_COMMANDS_PATTERNS.put(SET_PHONE_NUMBER, compile(SET_PHONE_NUMBER.getPatternTemplate()));
        USER_COMMANDS_PATTERNS.put(CHANGE_PHONE_NUMBER, compile(CHANGE_PHONE_NUMBER.getPatternTemplate()));
        USER_COMMANDS_PATTERNS.put(CHOOSE_SERVICES, compile(CHOOSE_SERVICES.getPatternTemplate()));
        USER_COMMANDS_PATTERNS.put(CHOOSE_NAILS, compile(CHOOSE_NAILS.getPatternTemplate()));
        USER_COMMANDS_PATTERNS.put(CHOOSE_MASTER, compile(format(CHOOSE_MASTER.getPatternTemplate(), UUID_PATTERN)));
        USER_COMMANDS_PATTERNS.put(CHOOSE_MONTH, compile(format(CHOOSE_MONTH.getPatternTemplate(), EMAIL_PATTERN, DAY_OF_MONTH_PATTERN, MONTH_PATTERN, YEAR_PATTERN)));
        USER_COMMANDS_PATTERNS.put(CHOOSE_DAY, compile(format(CHOOSE_DAY.getPatternTemplate(), EMAIL_PATTERN, DAY_OF_MONTH_PATTERN, MONTH_PATTERN, YEAR_PATTERN)));
        USER_COMMANDS_PATTERNS.put(CHOOSE_TIME, compile(format(CHOOSE_TIME.getPatternTemplate(), EMAIL_PATTERN, DAY_OF_MONTH_PATTERN, MONTH_PATTERN, YEAR_PATTERN, HOURS_PATTERN, MINUTES_PATTERN)));
        USER_COMMANDS_PATTERNS.put(BOOK_TIME, compile(format(BOOK_TIME.getPatternTemplate(), EMAIL_PATTERN, DAY_OF_MONTH_PATTERN, MONTH_PATTERN, YEAR_PATTERN, HOURS_PATTERN, MINUTES_PATTERN)));
    }

    public static Optional<UserCommand> findCommandByTextCommand(final String textCommand) {
        return USER_COMMANDS_PATTERNS.entrySet().stream()
                .filter(entrySet -> matchesPattern(entrySet.getValue(), textCommand))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    private static boolean matchesPattern(final Pattern pattern, final String textCommand) {
        return pattern.matcher(textCommand).matches();
    }
}
