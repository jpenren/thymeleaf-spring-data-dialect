package org.thymeleaf.dialect.springdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.springdata.util.Messages;
import org.thymeleaf.dialect.springdata.util.PageUtils;
import org.thymeleaf.dialect.springdata.util.Strings;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

final class PageSizeSelectorAttrProcessor extends AbstractAttributeTagProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(PageSizeSelectorAttrProcessor.class);
    private static final String MESSAGE_PREFIX = "page.size.selector.";
    private static final String DEFAULT_STYLE = MESSAGE_PREFIX + "default";
    private static final String ATTR_NAME = "page-size-selector";
    public static final int PRECEDENCE = 900;
    private static final String BUNDLE_NAME = "PageSizeSelector";
    private static final String SELECTOR_VALUES = "page.size.selector.values";
    private final List<Integer> selectorValues = new ArrayList<Integer>();

    protected PageSizeSelectorAttrProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        Locale locale = context.getLocale();
        loadSelectorValues(locale);
        String selectorStyle = String.valueOf(attributeValue).trim();
        String messageKey = getMessageKey(selectorStyle);
        String options = composeSelectorOptions(selectorStyle, context);
        String message = Messages.getMessage(BUNDLE_NAME, messageKey, locale, options);

        structureHandler.setBody(message, false);
    }

    private void loadSelectorValues(Locale locale) {
        if (selectorValues.isEmpty()) {
            String property = Messages.getMessage(BUNDLE_NAME, SELECTOR_VALUES, locale);
            String[] values = property.split(Strings.COMMA);
            for (String value : values) {
                try {
                    selectorValues.add(Integer.parseInt(value.trim()));
                } catch (Exception e) {
                    LOGGER.error("Invalid page size value: {}", value, e);
                }
            }
        }
    }

    private String getMessageKey(String selectorStyle) {
        return "".equals(selectorStyle) ? DEFAULT_STYLE : MESSAGE_PREFIX.concat(selectorStyle);
    }

    /**
     * Create select html content, list of options
     * 
     * @param selectorStyle
     *            html selector style
     * @param context
     *            execution context
     * @param selectedValue
     *            current selected value
     * @return available page size options as html
     */
    private String composeSelectorOptions(String selectorStyle, ITemplateContext context) {
        Page<?> page = PageUtils.findPage(context);
        int currentPageSize = page.getSize();
        Locale locale = context.getLocale();
        StringBuilder sb = new StringBuilder();
        for (int value : selectorValues) {
            String url = PageUtils.createPageSizeUrl(context, value);
            boolean isSelectedValue = value == currentPageSize;
            String messageKey = getMessageKey(selectorStyle).concat(isSelectedValue ? ".option.selected" : ".option");
            String option = Messages.getMessage(BUNDLE_NAME, messageKey, locale, value, url);
            sb.append(option);
        }

        return sb.toString();
    }

}
