package com.amiron.booking.bot.util;

import lombok.NoArgsConstructor;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class CommandUtils {

    private static final String COMMAND_PATTERN = "(/)([a-zA-Z]+)";
    private static final Pattern COMMAND_WITH_UUID_PATTERN = Pattern.compile(COMMAND_PATTERN + "((/)([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}))");
    private static final Pattern COMMAND_WITH_DATE_PATTERN = Pattern.compile(COMMAND_PATTERN + "((/)((\\d|\\d{2})-(\\d|\\d{2})-(\\d{4})))");
    private static final int UUID_WITH_SLASH_GROUP_NUMBER = 3;
    private static final int UUID_GROUP_NUMBER = 5;
    private static final int DATE_WITH_SLASH_GROUP_NUMBER = 3;
    private static final int DATE_GROUP_NUMBER = 5;

    public static String removeUuidPartFromCommand(final String commandText) {
        final String uuidPart = getCommandUuidPart(commandText);
        return commandText.replace(uuidPart, EMPTY);
    }

    public static UUID getUuidFromCommand(final String commandText) {
        final String uuid = getUuidFromCommandAsString(commandText);
        return UUID.fromString(uuid);
    }

    public static String getUuidFromCommandAsString(final String commandText) {
        final Matcher matcher = COMMAND_WITH_UUID_PATTERN.matcher(commandText);
        return matcher.find()
                ? matcher.group(UUID_GROUP_NUMBER)
                : EMPTY;
    }

    public static boolean isCommandWithUuid(final String commandText) {
        return COMMAND_WITH_UUID_PATTERN.matcher(commandText).matches();
    }

    public static boolean isCommandWithDate(final String commandText) {
        return COMMAND_WITH_DATE_PATTERN.matcher(commandText).matches();
    }

    public static String removeDatePartFromCommand(final String commandText) {
        final String datePart = getCommandDatePart(commandText);
        return commandText.replace(datePart, EMPTY);
    }

    private static String getCommandDatePart(final String commandText) {
        final Matcher matcher = COMMAND_WITH_DATE_PATTERN.matcher(commandText);
        return matcher.find()
                ? matcher.group(DATE_WITH_SLASH_GROUP_NUMBER)
                : EMPTY;
    }

    private static String getCommandUuidPart(final String commandText) {
        final Matcher matcher = COMMAND_WITH_UUID_PATTERN.matcher(commandText);
        return matcher.find()
                ? matcher.group(UUID_WITH_SLASH_GROUP_NUMBER)
                : EMPTY;
    }
}
