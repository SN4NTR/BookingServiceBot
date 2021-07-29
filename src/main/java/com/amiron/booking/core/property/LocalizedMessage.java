package com.amiron.booking.core.property;

import lombok.NoArgsConstructor;

import java.util.Locale;
import java.util.ResourceBundle;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public final class LocalizedMessage {

    private static final String MESSAGES_BUNDLE_NAME = "messages";

    public static String getLocalizedMessage(String messageKey) {
        return getLocalizedMessage(messageKey, Locale.getDefault());
    }

    public static String getLocalizedMessage(String messageKey, Locale locale) {
        return ResourceBundle.getBundle(MESSAGES_BUNDLE_NAME, locale).getString(messageKey);
    }
}
