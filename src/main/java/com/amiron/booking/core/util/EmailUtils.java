package com.amiron.booking.core.util;

import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class EmailUtils {

    public static final Pattern EMAIL_REGEX = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$");

    public static boolean isEmail(final String text) {
        return EMAIL_REGEX.matcher(text).matches();
    }
}
