package org.thymeleaf.dialect.springdata;

import java.util.Locale;

import org.springframework.data.domain.Page;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.springdata.util.Messages;
import org.thymeleaf.dialect.springdata.util.PageUtils;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

final class PaginationSummaryAttrProcessor extends AbstractAttributeTagProcessor {
    private static final String DEFAULT_MESSAGE_KEY = "pagination.summary";
    private static final String COMPACT_MESSAGE_KEY = "pagination.summary.compact";
    private static final String NO_VALUES_MESSAGE_KEY = "pagination.summary.empty";
    private static final String ATTR_NAME = "pagination-summary";
    public static final int PRECEDENCE = 900;
    private static final String BUNDLE_NAME = "PaginationSummary";

    protected PaginationSummaryAttrProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        // Compose message with parameters:
        // {0} first reg. position
        // {1} latest page reg. position
        // {2} total elements
        // pagination.summary=Showing {0} to {1} of {2} entries

        Locale locale = context.getLocale();
        Page<?> page = PageUtils.findPage(context);
        int firstItem = PageUtils.getFirstItemInPage(page);
        int latestItem = PageUtils.getLatestItemInPage(page);
        int totalElements = (int) page.getTotalElements();
        boolean isEmpty = page.getTotalElements() == 0;

        String attrValue = String.valueOf(attributeValue).trim();
        String messageTemplate = "compact".equals(attrValue) ? COMPACT_MESSAGE_KEY : DEFAULT_MESSAGE_KEY;
        String messageKey = isEmpty ? NO_VALUES_MESSAGE_KEY : messageTemplate;
        String message = Messages.getMessage(BUNDLE_NAME, messageKey, locale, firstItem, latestItem, totalElements);

        structureHandler.setBody(message, false);
    }

}
