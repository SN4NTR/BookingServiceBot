package com.amiron.booking.core.constant;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class RegexConstants {

    public static final String UUID_PATTERN = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})";
    public static final String DAY_OF_MONTH_PATTERN = "(\\d{1,2})";
    public static final String MONTH_PATTERN = "(\\d{1,2})";
    public static final String YEAR_PATTERN = "(\\d{4})";
    public static final String HOURS_PATTERN = "(\\d{1,2})";
    public static final String MINUTES_PATTERN = "(\\d{1,2})";
    public static final String EMAIL_PATTERN = "([a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6})";
}
