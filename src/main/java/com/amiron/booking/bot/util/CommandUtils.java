package com.amiron.booking.bot.util;

import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class CommandUtils {

    private static final Pattern COMMAND_WITH_UUID_PATTERN = Pattern.compile("(/)([a-zA-Z]+)((/)([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}))");
    private static final int UUID_WITH_SLASH_GROUP_NUMBER = 3;
    private static final int UUID_GROUP_NUMBER = 5;

    public static String removeUuidPartFromCommand(final String commandText) {
        final String uuidPart = getCommandUuidPart(commandText);
        return commandText.replace(uuidPart, EMPTY);
    }

    public static String getUuidFromCommand(final String commandText) {
        final Matcher matcher = COMMAND_WITH_UUID_PATTERN.matcher(commandText);
        return matcher.find()
                ? matcher.group(UUID_GROUP_NUMBER)
                : EMPTY;
    }

    public static boolean isCommandWithUuid(final String commandText) {
        return COMMAND_WITH_UUID_PATTERN.matcher(commandText).matches();
    }

    private static String getCommandUuidPart(final String commandText) {
        final Matcher matcher = COMMAND_WITH_UUID_PATTERN.matcher(commandText);
        return matcher.find()
                ? matcher.group(UUID_WITH_SLASH_GROUP_NUMBER)
                : EMPTY;
    }
}
