package org.thymeleaf.dialect.springdata.decorator;

import java.util.Locale;

import org.springframework.data.domain.Page;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.springdata.util.Messages;
import org.thymeleaf.dialect.springdata.util.PageUtils;
import org.thymeleaf.dialect.springdata.util.Strings;
import org.thymeleaf.model.IProcessableElementTag;

abstract class AbstractPagerDecorator implements PaginationDecorator {

    public final String decorate(final IProcessableElementTag tag, final ITemplateContext context) {
        String bundleName = getClass().getSimpleName();
        Locale locale = context.getLocale();
        Page<?> page = PageUtils.findPage(context);

        // previous
        boolean isFirstPage = page.getNumber() == 0;
        String previousPage = PageUtils.createPageUrl(context, page.getNumber() - 1);
        String prevKey = isFirstPage ? "pager.previous" : "pager.previous.link";
        String prev = Messages.getMessage(bundleName, prevKey, locale, previousPage);

        // next
        boolean isLatestPage = page.getNumber() == (page.getTotalPages() - 1);
        String nextPage = PageUtils.createPageUrl(context, page.getNumber() + 1);
        String nextKey = isLatestPage ? "pager.next" : "pager.next.link";
        String next = Messages.getMessage(bundleName, nextKey, locale, nextPage);

        String content = Strings.concat(prev, next);

        return Messages.getMessage(bundleName, "pager", locale, content);
    }

}
