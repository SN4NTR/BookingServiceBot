package com.amiron.booking.bot.util;

import lombok.NoArgsConstructor;

import java.util.UUID;
import java.util.regex.Matcher;
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
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class CommandUtils {

    private static final Pattern UUID_WITH_SLASH_PATTERN = compile(format("(/%s)", UUID_PATTERN));
    private static final Pattern DAY_OF_MONTH_VALUE_PATTERN = compile(format("d=%s", DAY_OF_MONTH_PATTERN));
    private static final Pattern MONTH_VALUE_PATTERN = compile(format("m=%s", MONTH_PATTERN));
    private static final Pattern YEAR_VALUE_PATTERN = compile(format("y=%s", YEAR_PATTERN));
    private static final Pattern EMAIL_VALUE_PATTERN = compile(format("e=%s", EMAIL_PATTERN));
    private static final Pattern HOURS_VALUE_PATTERN = compile(format("h=%s", HOURS_PATTERN));
    private static final Pattern MINUTES_VALUE_PATTERN = compile(format("mm=%s", MINUTES_PATTERN));
    private static final int UUID_GROUP_NUMBER = 2;
    private static final int DATE_VALUE_GROUP_NUMBER = 1;
    private static final int TIME_VALUE_GROUP_NUMBER = 1;
    private static final int EMAIL_VALUE_GROUP_NUMBER = 1;

    public static UUID getUuidFromCommand(final String commandText) {
        final String uuid = getUuidFromCommandAsString(commandText);
        return UUID.fromString(uuid);
    }

    public static String getUuidFromCommandAsString(final String commandText) {
        final Matcher matcher = UUID_WITH_SLASH_PATTERN.matcher(commandText);
        return matcher.find()
                ? matcher.group(UUID_GROUP_NUMBER)
                : EMPTY;
    }

    public static String getEmailFromCommand(final String commandText) {
        final Matcher matcher = EMAIL_VALUE_PATTERN.matcher(commandText);
        return matcher.find()
                ? matcher.group(EMAIL_VALUE_GROUP_NUMBER)
                : EMPTY;
    }

    public static int getDayOfMonthValueFromCommand(final String commandText) {
        final Matcher matcher = DAY_OF_MONTH_VALUE_PATTERN.matcher(commandText);
        return matcher.find()
                ? Integer.parseInt(matcher.group(DATE_VALUE_GROUP_NUMBER))
                : 0;
    }

    public static int getMonthValueFromCommand(final String commandText) {
        final Matcher matcher = MONTH_VALUE_PATTERN.matcher(commandText);
        return matcher.find()
                ? Integer.parseInt(matcher.group(DATE_VALUE_GROUP_NUMBER))
                : 0;
    }

    public static int getYearValueFromCommand(final String commandText) {
        final Matcher matcher = YEAR_VALUE_PATTERN.matcher(commandText);
        return matcher.find()
                ? Integer.parseInt(matcher.group(DATE_VALUE_GROUP_NUMBER))
                : 0;
    }

    public static int getHoursValueFromCommand(final String commandText) {
        final Matcher matcher = HOURS_VALUE_PATTERN.matcher(commandText);
        return matcher.find()
                ? Integer.parseInt(matcher.group(TIME_VALUE_GROUP_NUMBER))
                : 0;
    }

    public static int getMinutesValueFromCommand(final String commandText) {
        final Matcher matcher = MINUTES_VALUE_PATTERN.matcher(commandText);
        return matcher.find()
                ? Integer.parseInt(matcher.group(TIME_VALUE_GROUP_NUMBER))
                : 0;
    }
}
