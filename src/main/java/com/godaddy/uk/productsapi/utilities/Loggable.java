package com.godaddy.uk.productsapi.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Loggable {

    /**
     * Provides common, consistent logger implementation.
     *
     * @return Logger instance using this class name
     */
    default Logger logger() {
        return LoggerFactory.getLogger(this.getClass());
    }

}