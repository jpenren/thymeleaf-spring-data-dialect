package org.thymeleaf.dialect.springdata.decorator;

import org.springframework.data.domain.Page;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.springdata.Keys;
import org.thymeleaf.dialect.springdata.SpringDataDialect;
import org.thymeleaf.dialect.springdata.util.Messages;
import org.thymeleaf.dialect.springdata.util.PageUtils;
import org.thymeleaf.dialect.springdata.util.Strings;
import org.thymeleaf.model.IProcessableElementTag;

import java.util.Locale;
import java.util.Map;

public final class FullPaginationDecorator implements PaginationDecorator {
    private static final String DEFAULT_CLASS = "pagination";
    private static final String BUNDLE_NAME = FullPaginationDecorator.class.getSimpleName();
    private static final int DEFAULT_PAGE_SPLIT = 7;
    private static final String CSS_ATTR_PREFIX = SpringDataDialect.PREFIX + ":" + DEFAULT_CLASS + "-";

    // define custom CSS tags
    private String cssLaquo = "page-item";
    private String cssRaquo = "page-item";
    private String cssPrevious = "page-item";
    private String cssNext = "page-item";
    private String cssPageItem = "page-item";
    private String cssPageLink = "page-link";
    private String cssDisabled = "disabled";
    private String cssActive = "active";

    public String getIdentifier() {
        return "full";
    }

    public String decorate(final IProcessableElementTag tag, final ITemplateContext context) {

        Page<?> page = PageUtils.findPage(context);

        configureCss(tag.getAttributeMap());

        // laquo
        String firstPage = PageUtils.createPageUrl(context, 0);
        Locale locale = context.getLocale();
        String laquo = PageUtils.isFirstPage(page) ? getLaquo(locale) : getLaquo(firstPage, locale);

        // Previous page
        String previous = getPreviousPageLink(page, context);

        // Links
        String pageLinks = createPageLinks(page, context);

        // Next page
        String next = getNextPageLink(page, context);

        // raquo
        String lastPage = PageUtils.createPageUrl(context, page.getTotalPages() - 1);
        String raquo = page.isLast() ? getRaquo(locale) : getRaquo(lastPage, locale);

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
        return Messages.getMessage(BUNDLE_NAME, "laquo", locale, cssLaquo, cssPageLink, cssDisabled);
    }

    private String getLaquo(String firstPage, Locale locale) {
        return Messages.getMessage(BUNDLE_NAME, "laquo.link", locale, firstPage, cssLaquo, cssPageLink);
    }

    private String getRaquo(Locale locale) {
        return Messages.getMessage(BUNDLE_NAME, "raquo", locale, cssRaquo, cssPageLink, cssDisabled);
    }

    private String getRaquo(String lastPage, Locale locale) {
        return Messages.getMessage(BUNDLE_NAME, "raquo.link", locale, lastPage, cssRaquo, cssPageLink);
    }

    private String getLink(int pageNumber, Locale locale) {
        return Messages.getMessage(BUNDLE_NAME, "link.active", locale, pageNumber, cssActive, cssPageLink);
    }

    private String getLink(int pageNumber, String url, Locale locale) {
        return Messages.getMessage(BUNDLE_NAME, "link", locale, url, pageNumber, cssPageItem, cssPageLink);
    }

    private String getPreviousPageLink(Page<?> page, final ITemplateContext context) {
        String msgKey = PageUtils.hasPrevious(page) ? "previous.page.link" : "previous.page";
        Locale locale = context.getLocale();
        int previousPage = page.getNumber()-1;
        String link = PageUtils.createPageUrl(context, previousPage);

        return Messages.getMessage(BUNDLE_NAME, msgKey, locale, link, cssPrevious, cssPageLink, cssDisabled);
    }

    private String getNextPageLink(Page<?> page, final ITemplateContext context) {
        String msgKey = page.hasNext() ? "next.page.link" : "next.page";
        Locale locale = context.getLocale();
        int nextPage = page.getNumber() + 1;
        String link = PageUtils.createPageUrl(context, nextPage);

        return Messages.getMessage(BUNDLE_NAME, msgKey, locale, link, cssNext, cssPageLink, cssDisabled);
    }

    private void configureCss(Map<String,String> attributeMap) {

        if (attributeMap.get(CSS_ATTR_PREFIX + "laquo") != null) {
            this.cssLaquo = attributeMap.get(CSS_ATTR_PREFIX + "laquo");
        }

        if (attributeMap.get(CSS_ATTR_PREFIX + "raquo") != null) {
            this.cssRaquo = attributeMap.get(CSS_ATTR_PREFIX + "raquo");
        }

        if (attributeMap.get(CSS_ATTR_PREFIX + "previous") != null) {
            this.cssPrevious = attributeMap.get(CSS_ATTR_PREFIX + "previous");
        }

        if (attributeMap.get(CSS_ATTR_PREFIX + "next") != null) {
            this.cssNext = attributeMap.get(CSS_ATTR_PREFIX + "next");
        }

        if (attributeMap.get(CSS_ATTR_PREFIX + "page-item") != null) {
            this.cssPageItem = attributeMap.get(CSS_ATTR_PREFIX + "page-item");
        }

        if (attributeMap.get(CSS_ATTR_PREFIX + "page-link") != null) {
            this.cssPageLink = attributeMap.get(CSS_ATTR_PREFIX + "page-link");
        }

        if (attributeMap.get(CSS_ATTR_PREFIX + "disabled") != null) {
            this.cssDisabled = attributeMap.get(CSS_ATTR_PREFIX + "disabled");
        }

        if (attributeMap.get(CSS_ATTR_PREFIX + "active") != null) {
            this.cssActive = attributeMap.get(CSS_ATTR_PREFIX + "active");
        }
    }

}
