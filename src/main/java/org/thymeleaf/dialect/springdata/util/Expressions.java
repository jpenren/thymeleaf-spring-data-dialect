package org.thymeleaf.dialect.springdata.util;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

public final class Expressions {

    public Expressions() {
    }

    public static Object evaluate(IExpressionContext context, String expressionValue) {
        final String value = String.valueOf(expressionValue).trim();
        final IStandardExpressionParser expressionParser = StandardExpressions
                .getExpressionParser(context.getConfiguration());
        final IStandardExpression expression = expressionParser.parseExpression(context, value);

        return expression.execute(context);
    }

}
