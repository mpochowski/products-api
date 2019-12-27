package com.godaddy.uk.productsapi.utilities;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.util.Objects.isNull;

public class DateTimeUtilities {

    private DateTimeUtilities() throws IllegalAccessException {
        throw new IllegalAccessException("Cannot be instantiated.");
    }

    /**
     * Defines default zone the application is working with.
     * This is to ensure application works consistent even if deployed to machines in different timezone.
     */
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Europe/London");

    /**
     * Converts local date time into epoch time.
     *
     * @param localDateTime nullable local date time
     * @return epoch time in default time zone, or 0 if local date time is null
     */
    public static long toEpochSecond(@Nullable LocalDateTime localDateTime) {
        if (isNull(localDateTime)) {
            return 0;
        }

        return localDateTime.atZone(DEFAULT_ZONE).toEpochSecond();
    }

}