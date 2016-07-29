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

final class PageSizeSelectorAttrProcessor extends AbstractAttributeTagProcessor {
	private static final String MESSAGE_PREFIX = "page.size.selector.";
	private static final String DEFAULT_STYLE = MESSAGE_PREFIX + "default";
	private static final String ATTR_NAME = "page-size-selector";
	public static final int PRECEDENCE = 900;
	private static final String BUNDLE_NAME = "PageSizeSelector";
	private int[] selectorValues = new int[]{10, 20, 50, 100};
	
	protected PageSizeSelectorAttrProcessor(final String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
	}

	@Override
	protected void doProcess(ITemplateContext context,
			IProcessableElementTag tag, AttributeName attributeName,
			String attributeValue, IElementTagStructureHandler structureHandler) {
		
		Locale locale = context.getLocale();
		String selectorStyle = String.valueOf(attributeValue).trim();
		String messageKey = getMessageKey(selectorStyle);
		String options = composeSelectorOptions(selectorStyle, context);
		String message = Messages.getMessage(BUNDLE_NAME, messageKey, locale, options);

		structureHandler.setBody(message, false);
	}
	
	private String getMessageKey(String selectorStyle){
		return "".equals(selectorStyle) ? DEFAULT_STYLE: MESSAGE_PREFIX.concat(selectorStyle);
	}
	
	/**
	 * Create select html content, list of options
	 * @param selectorStyle html selector style
	 * @param context execution context
	 * @param selectedValue current selected value
	 * @return available page size options as html
	 */
	private String composeSelectorOptions(String selectorStyle, ITemplateContext context){
		Page<?> page = PageUtils.findPage(context);
		int currentPageSize = page.getSize();
		Locale locale = context.getLocale();
		StringBuilder sb = new StringBuilder();
		for (int value : selectorValues) {
			String url = PageUtils.createPageSizeUrl(context, value);
			boolean isSelectedValue = value==currentPageSize;
			String messageKey = getMessageKey(selectorStyle).concat(isSelectedValue ? ".option.selected" : ".option");
			String option = Messages.getMessage(BUNDLE_NAME, messageKey , locale, value, url);
			sb.append( option );
		}
		
		return sb.toString();
	}
	
}
