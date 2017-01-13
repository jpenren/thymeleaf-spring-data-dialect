package org.thymeleaf.dialect.springdata.decorator;

import java.util.Locale;

import org.springframework.data.domain.Page;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.springdata.Keys;
import org.thymeleaf.dialect.springdata.util.Messages;
import org.thymeleaf.dialect.springdata.util.PageUtils;
import org.thymeleaf.dialect.springdata.util.Strings;
import org.thymeleaf.model.IProcessableElementTag;

public final class FullPaginationDecorator implements PaginationDecorator {
    private static final String DEFAULT_CLASS = "pagination";
    private static final String BUNDLE_NAME = FullPaginationDecorator.class.getSimpleName();
    private static final int DEFAULT_PAGE_SPLIT = 7;

    public String getIdentifier() {
        return "full";
    }

    public String decorate(final IProcessableElementTag tag, final ITemplateContext context) {

        Page<?> page = PageUtils.findPage(context);

        // laquo
        String firstPage = PageUtils.createPageUrl(context, 0);
        boolean isFirstPage = page.getNumber() == 0;
        Locale locale = context.getLocale();
        String laquo = isFirstPage ? getLaquo(locale) : getLaquo(firstPage, locale);

        // Previous page
        String previous = getPreviousPageLink(page, context);

        // Links
        String pageLinks = createPageLinks(page, context);

        // Next page
        String next = getNextPageLink(page, context);

        // raquo
        boolean isLastPage = page.getTotalPages() == 0 || page.getNumber() == (page.getTotalPages() - 1);
        String lastPage = PageUtils.createPageUrl(context, page.getTotalPages() - 1);
        String raquo = isLastPage ? getRaquo(locale) : getRaquo(lastPage, locale);

        boolean isUl = Strings.UL.equalsIgnoreCase(tag.getElementCompleteName());
        String currentClass = tag.getAttributeValue(Strings.CLASS);
        String clas = (isUl && !Strings.isEmpty(currentClass)) ? currentClass : DEFAULT_CLASS;

        return Messages.getMessage(BUNDLE_NAME, "pagination", locale, clas, laquo, previous, pageLinks, next, raquo);
    }

    private String createPageLinks(final Page<?> page, final ITemplateContext context) {
        if( page.getTotalElements()==0 ){
            return Strings.EMPTY;
        }
        
        int pageSplit = DEFAULT_PAGE_SPLIT;
        Object paramValue = context.getVariable(Keys.PAGINATION_SPLIT_KEY);
        if (paramValue != null) {
            pageSplit = (Integer) paramValue;
        }

        int firstPage = 0;
        int latestPage = page.getTotalPages();
        int currentPage = page.getNumber();
        if (latestPage >= pageSplit) {
            // Total pages > than split value, create links to split value
            int pageDiff = latestPage - currentPage;
            if (currentPage == 0) {
                // From first page to split value
                latestPage = pageSplit;
            } else if (pageDiff < pageSplit) {
                // From split value to latest page
                firstPage = currentPage - (pageSplit - pageDiff);
            } else {
                // From current page -1 to split value
                firstPage = currentPage - 1;
                latestPage = currentPage + pageSplit - 1;
            }
        }

        StringBuilder builder = new StringBuilder();
        // Page links
        for (int i = firstPage; i < latestPage; i++) {
            int pageNumber = i + 1;
            String link = PageUtils.createPageUrl(context, i);
            boolean isCurrentPage = i == currentPage;
            Locale locale = context.getLocale();
            String li = isCurrentPage ? getLink(pageNumber, locale) : getLink(pageNumber, link, locale);
            builder.append(li);
        }

        return builder.toString();
    }

    private String getLaquo(Locale locale) {
        return Messages.getMessage(BUNDLE_NAME, "laquo", locale);
    }

    private String getLaquo(String firstPage, Locale locale) {
        return Messages.getMessage(BUNDLE_NAME, "laquo.link", locale, firstPage);
    }

    private String getRaquo(Locale locale) {
        return Messages.getMessage(BUNDLE_NAME, "raquo", locale);
    }

    private String getRaquo(String lastPage, Locale locale) {
        return Messages.getMessage(BUNDLE_NAME, "raquo.link", locale, lastPage);
    }

    private String getLink(int pageNumber, Locale locale) {
        return Messages.getMessage(BUNDLE_NAME, "link.active", locale, pageNumber);
    }

    private String getLink(int pageNumber, String url, Locale locale) {
        return Messages.getMessage(BUNDLE_NAME, "link", locale, url, pageNumber);
    }

    private String getPreviousPageLink(Page<?> page, final ITemplateContext context) {
        int previousPage = page.getNumber() - 1;
        String msgKey = previousPage < 0 ? "previous.page" : "previous.page.link";
        Locale locale = context.getLocale();
        String link = PageUtils.createPageUrl(context, previousPage);

        return Messages.getMessage(BUNDLE_NAME, msgKey, locale, link);
    }

    private String getNextPageLink(Page<?> page, final ITemplateContext context) {
        int totalPages = page.getTotalPages();
        int currentPage = page.getNumber();
        int nextPage = currentPage + 1;
        boolean hasMorePages = (totalPages > 0) && (nextPage < totalPages);
        
        String msgKey = hasMorePages ? "next.page.link" : "next.page";
        Locale locale = context.getLocale();
        String link = PageUtils.createPageUrl(context, nextPage);

        return Messages.getMessage(BUNDLE_NAME, msgKey, locale, link);
    }

}
