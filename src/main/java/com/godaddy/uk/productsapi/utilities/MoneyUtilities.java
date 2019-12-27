package com.godaddy.uk.productsapi.utilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import static java.util.Objects.isNull;

public class MoneyUtilities {

    private MoneyUtilities() throws IllegalAccessException {
        throw new IllegalAccessException("Cannot be instantiated.");
    }

    /**
     * Sum money values of given items.
     *
     * @param items          the list of items to sum money values.
     * @param valueExtractor the value extractor function
     * @param <T>            any type
     * @return 0 if items are null, sum of item values extracted using value extractor function otherwise
     */
    public static <T> double sumMoneyValues(@Nullable List<T> items, @NotNull Function<T, Double> valueExtractor) {
        if (isNull(items) || items.size() == 0) {
            return 0.0D;
        }

        return items
                .stream()
                .map(valueExtractor)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.valueOf(0D), BigDecimal::add)
                .doubleValue();
    }

}