package org.thymeleaf.dialect.springdata;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractTextChildModifierAttrProcessor;

class PaginationSummaryAttrProcessor extends AbstractTextChildModifierAttrProcessor {
	private static final String DEFAULT_MESSAGE_KEY = "pagination.summary";
	private static final String NO_VALUES_MESSAGE_KEY = "pagination.summary.empty";
	private static final String ATTR_NAME = "pagination-summary";
	
	protected PaginationSummaryAttrProcessor() {
		super(ATTR_NAME);
	}

	@Override
	protected String getText(Arguments arguments, Element element, String attributeName) {
		final String attributeValue = element.getAttributeValue(attributeName);
		final String messageKey = attributeValue==null ? DEFAULT_MESSAGE_KEY : attributeValue;
		
		ProcessorUtils.removeAttribute(element, ATTR_NAME);
		
		//Compose message parameters:
		// {0} first reg. position
		// {1} latest page reg. position
		// {2} total elements in DB
		Page<?> page = PageUtils.findPage(arguments);
		if(page.getTotalElements()==0){
			return getMessage(arguments, NO_VALUES_MESSAGE_KEY, null);
		}
		
		List<Number> params = new ArrayList<Number>();
		params.add(PageUtils.getFirstItemInPage(page));
		params.add(PageUtils.getLatestItemInPage(page));
		params.add(page.getTotalElements());
		
		return getMessage(arguments, messageKey, params.toArray());
	}

	@Override
	public int getPrecedence() {
		return 1000;
	}

}
