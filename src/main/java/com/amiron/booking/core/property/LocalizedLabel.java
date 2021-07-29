package com.amiron.booking.core.property;

import lombok.NoArgsConstructor;

import java.util.Locale;
import java.util.ResourceBundle;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class LocalizedLabel {

    private static final String LABELS_BUNDLE_NAME = "labels";

    public static String getLocalizedLabel(String messageKey) {
        return getLocalizedLabel(messageKey, Locale.getDefault());
    }

    public static String getLocalizedLabel(String messageKey, Locale locale) {
        return ResourceBundle.getBundle(LABELS_BUNDLE_NAME, locale).getString(messageKey);
    }
}
