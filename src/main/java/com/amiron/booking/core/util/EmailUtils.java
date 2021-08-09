package com.amiron.booking.core.util;

import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

import static com.amiron.booking.core.constant.RegexConstants.EMAIL_PATTERN;
import static java.util.regex.Pattern.compile;
import static lombok.AccessLevel.PRIVATE;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class EmailUtils {

    public static final Pattern EMAIL_REGEX = compile(EMAIL_PATTERN);

    public static boolean isEmail(final String text) {
        return EMAIL_REGEX.matcher(text).matches();
    }
}
