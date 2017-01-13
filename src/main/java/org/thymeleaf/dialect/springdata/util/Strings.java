package org.thymeleaf.dialect.springdata.util;

public final class Strings {

    public static final String EMPTY = "";
    public static final String BLANK = " ";
    public static final String CLASS = "class";
    public static final String UL = "ul";
    public static final String HREF = "href";
    public static final String COMMA = ",";
    public static final String AND = "&";
    public static final String Q_MARK = "?";
    public static final String EQ = "=";
    public static final String PAGE = "page";
    public static final String SIZE = "size";
    public static final String SORTED_PREFIX = "sorted-";
    public static final String SORT = "sort";

    private Strings() {
    }

    public static String concat(String... strings) {
        final StringBuilder builder = new StringBuilder();
        for (String string : strings) {
            builder.append(string);
        }

        return builder.toString();
    }

    public static boolean isEmpty(String text) {
        return text == null || text.equals(EMPTY);
    }

}
