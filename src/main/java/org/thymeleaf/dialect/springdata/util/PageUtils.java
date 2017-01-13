package org.thymeleaf.dialect.springdata.util;

import static org.thymeleaf.dialect.springdata.util.Strings.AND;
import static org.thymeleaf.dialect.springdata.util.Strings.COMMA;
import static org.thymeleaf.dialect.springdata.util.Strings.EMPTY;
import static org.thymeleaf.dialect.springdata.util.Strings.EQ;
import static org.thymeleaf.dialect.springdata.util.Strings.PAGE;
import static org.thymeleaf.dialect.springdata.util.Strings.Q_MARK;
import static org.thymeleaf.dialect.springdata.util.Strings.SIZE;
import static org.thymeleaf.dialect.springdata.util.Strings.SORT;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dialect.springdata.Keys;
import org.thymeleaf.dialect.springdata.exception.InvalidObjectParameterException;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.unbescape.html.HtmlEscape;

@SuppressWarnings("unchecked")
public final class PageUtils {

    private PageUtils() {
    }

    public static Page<?> findPage(final ITemplateContext context) {
        // 1. Get Page object from local variables (defined with sd:page-object)
        // 2. Search Page using ${page} expression
        // 3. Search Page object as request attribute

        final Object pageFromLocalVariable = context.getVariable(Keys.PAGE_VARIABLE_KEY);
        if (isPageInstance(pageFromLocalVariable)) {
            return (Page<?>) pageFromLocalVariable;
        }

        // Check if not null and Page instance available with ${page} expression
        final IEngineConfiguration configuration = context.getConfiguration();
        final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
        final IStandardExpression expression = parser.parseExpression(context, Keys.PAGE_EXPRESSION);
        final Object page = expression.execute(context);
        if (isPageInstance(page)) {
            return (Page<?>) page;
        }

        // Search for Page object, and only one instance, as request attribute
        if (context instanceof IWebContext) {
            HttpServletRequest request = ((IWebContext) context).getRequest();
            Enumeration<String> attrNames = request.getAttributeNames();
            Page<?> pageOnRequest = null;
            while (attrNames.hasMoreElements()) {
                String attrName = (String) attrNames.nextElement();
                Object attr = request.getAttribute(attrName);
                if (isPageInstance(attr)) {
                    if (pageOnRequest != null) {
                        throw new InvalidObjectParameterException("More than one Page object found on request!");
                    }

                    pageOnRequest = (Page<?>) attr;
                }
            }

            if (pageOnRequest != null) {
                return pageOnRequest;
            }
        }

        throw new InvalidObjectParameterException("Invalid or not present Page object found on request!");
    }

    public static String createPageUrl(final ITemplateContext context, int pageNumber) {
        final String prefix = getParamPrefix(context);
        final Collection<String> excludedParams = Arrays.asList(new String[] { prefix.concat(PAGE) });
        final String baseUrl = buildBaseUrl(context, excludedParams);

        return buildUrl(baseUrl, context).append(PAGE).append(EQ).append(pageNumber).toString();
    }

    /**
     * Creates an url to sort data by fieldName
     * 
     * @param context execution context
     * @param fieldName field name to sort
     * @return sort URL
     */
    public static String createSortUrl(final ITemplateContext context, final String fieldName) {
        // Params can be prefixed to manage multiple pagination on the same page
        final String prefix = getParamPrefix(context);
        final Collection<String> excludedParams = Arrays
                .asList(new String[] { prefix.concat(SORT), prefix.concat(PAGE) });
        final String baseUrl = buildBaseUrl(context, excludedParams);

        final StringBuilder sortParam = new StringBuilder();
        final Page<?> page = findPage(context);
        final Sort sort = page.getSort();
        final boolean hasPreviousOrder = sort != null && sort.getOrderFor(fieldName) != null;
        if (hasPreviousOrder) {
            // Sort parameters exists for this field, modify direction
            Order previousOrder = sort.getOrderFor(fieldName);
            Direction dir = previousOrder.isAscending() ? Direction.DESC : Direction.ASC;
            sortParam.append(fieldName).append(COMMA).append(dir.toString().toLowerCase());
        } else {
            sortParam.append(fieldName);
        }

        return buildUrl(baseUrl, context).append(SORT).append(EQ).append(sortParam).toString();
    }

    public static String createPageSizeUrl(final ITemplateContext context, int pageSize) {
        final String prefix = getParamPrefix(context);
        // Reset page number to avoid empty lists
        final Collection<String> excludedParams = Arrays
                .asList(new String[] { prefix.concat(SIZE), prefix.concat(PAGE) });
        final String baseUrl = buildBaseUrl(context, excludedParams);

        return buildUrl(baseUrl, context).append(SIZE).append(EQ).append(pageSize).toString();
    }

    public static int getFirstItemInPage(final Page<?> page) {
        return page.getSize() * page.getNumber() + 1;
    }

    public static int getLatestItemInPage(final Page<?> page) {
        return page.getSize() * page.getNumber() + page.getNumberOfElements();
    }

    private static String buildBaseUrl(final ITemplateContext context, Collection<String> excludeParams) {
        // URL defined with pagination-url tag
        final String url = (String) context.getVariable(Keys.PAGINATION_URL_KEY);

        if (url == null && context instanceof IWebContext) {
            // Creates url from actual request URI and parameters
            final StringBuilder builder = new StringBuilder();
            final IWebContext webContext = (IWebContext) context;
            final HttpServletRequest request = webContext.getRequest();

            // URL base path from request
            builder.append(request.getRequestURI());

            Map<String, String[]> params = request.getParameterMap();
            Set<Entry<String, String[]>> entries = params.entrySet();
            boolean firstParam = true;
            for (Entry<String, String[]> param : entries) {
                // Append params not excluded to basePath
                String name = param.getKey();
                if (!excludeParams.contains(name)) {
                    if (firstParam) {
                        builder.append(Q_MARK);
                        firstParam = false;
                    } else {
                        builder.append(AND);
                    }

                    // Iterate over all values to create multiple values per
                    // parameter
                    String[] values = param.getValue();
                    Collection<String> paramValues = Arrays.asList(values);
                    Iterator<String> it = paramValues.iterator();
                    while (it.hasNext()) {
                        String value = it.next();
                        builder.append(name).append(EQ).append(value);
                        if (it.hasNext()) {
                            builder.append(AND);
                        }
                    }
                }
            }

            // Escape to HTML content
            return HtmlEscape.escapeHtml4Xml(builder.toString());
        }

        return url == null ? EMPTY : url;
    }

    private static boolean isPageInstance(Object page) {
        return page != null && (page instanceof Page<?>);
    }

    private static StringBuilder buildUrl(String baseUrl, final ITemplateContext context) {
        final String paramAppender = String.valueOf(baseUrl).contains(Q_MARK) ? AND : Q_MARK;
        final String prefix = getParamPrefix(context);

        return new StringBuilder(baseUrl).append(paramAppender).append(prefix);
    }

    private static String getParamPrefix(final ITemplateContext context) {
        final String prefix = (String) context.getVariable(Keys.PAGINATION_QUALIFIER_PREFIX);

        return prefix == null ? EMPTY : prefix.concat("_");
    }

}
