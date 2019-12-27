package com.godaddy.uk.productsapi.utilities;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class DateTimeUtilitiesTest {

    @Test
    public void testToEpochSecond() {
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2015, 1, 10, 15, 20, 10);

        // when
        long result = DateTimeUtilities.toEpochSecond(localDateTime);

        // then
        assertEquals(1420903210, result);
    }

    @Test
    public void testToEpochSecond_nullLocalDateTime() {
        // given
        LocalDateTime localDateTime = null;

        // when
        long result = DateTimeUtilities.toEpochSecond(localDateTime);

        // then
        assertEquals(0, result);
    }

}