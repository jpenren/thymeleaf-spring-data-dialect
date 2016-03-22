package org.thymeleaf.dialect.springdata;

import java.util.Locale;

import org.springframework.data.domain.Page;
import org.thymeleaf.Arguments;
import org.thymeleaf.dialect.springdata.util.Messages;
import org.thymeleaf.dialect.springdata.util.PageUtils;
import org.thymeleaf.dialect.springdata.util.ProcessorUtils;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractTextChildModifierAttrProcessor;
	
final class PaginationSummaryAttrProcessor extends AbstractTextChildModifierAttrProcessor {
	private static final String DEFAULT_MESSAGE_KEY = "pagination.summary";
	private static final String NO_VALUES_MESSAGE_KEY = "pagination.summary.empty";
	private static final String ATTR_NAME = "pagination-summary";
	private static final String BUNDLE_NAME = "PaginationSummary";
	
	protected PaginationSummaryAttrProcessor() {
		super(ATTR_NAME);
	}

	@Override
	protected String getText(Arguments arguments, Element element, String attributeName) {
		ProcessorUtils.removeAttribute(element, ATTR_NAME);
		
		//Compose message parameters:
		// {0} first reg. position
		// {1} latest page reg. position
		// {2} total elements in DB
		Page<?> page = PageUtils.findPage(arguments);
		int firstItem = PageUtils.getFirstItemInPage(page);
		int latestItem = PageUtils.getLatestItemInPage(page);
		int totalElements = (int) page.getTotalElements();
		boolean isEmpty = page.getTotalElements() == 0;
		Locale locale = arguments.getContext().getLocale();
		String messageKey = isEmpty ? NO_VALUES_MESSAGE_KEY : DEFAULT_MESSAGE_KEY;
		
		return Messages.getMessage(BUNDLE_NAME, messageKey, locale, firstItem, latestItem, totalElements);
	}

	@Override
	public int getPrecedence() {
		return 900;
	}
	
}
