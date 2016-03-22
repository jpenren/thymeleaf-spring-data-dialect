package org.thymeleaf.dialect.springdata;

import java.util.Collections;
import java.util.Map;

import org.thymeleaf.Arguments;
import org.thymeleaf.dialect.springdata.util.ProcessorUtils;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;

final class PaginationQualifierAttrProcessor extends AbstractAttrProcessor {
	private static final String ATTR_NAME = "pagination-qualifier";

	protected PaginationQualifierAttrProcessor() {
		super(ATTR_NAME);
	}

	@Override
	protected ProcessorResult processAttribute(Arguments arguments, Element element, String attrName) {
	    final String attributeValue = element.getAttributeValue(attrName);
		Map<String, Object> vars = Collections.<String, Object>singletonMap(Keys.PAGINATION_QUALIFIER_PREFIX, attributeValue);
		
		ProcessorUtils.removeAttribute(element, ATTR_NAME);
		
		return ProcessorResult.setLocalVariables(vars);
	}
	
	@Override
	public int getPrecedence() {
		return 900;
	}

}
