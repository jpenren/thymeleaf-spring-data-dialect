package org.thymeleaf.dialect.springdata.util;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

public final class Messages {
    private static final String BASE_PATH = "thymeleaf-spring-data-dialect/";

    private Messages() {
    }

    public static String getMessage(String bundleName, String key, Locale locale, Object... arguments) {
        final String resourceName = Strings.concat(BASE_PATH, bundleName);
        final String msg = ResourceBundle.getBundle(resourceName, locale).getString(key);

        return MessageFormat.format(msg, arguments);
    }

    public static String getMessage(String bundleName, String key, Locale locale) {
        return getMessage(bundleName, key, locale, Collections.EMPTY_LIST);
    }

}
