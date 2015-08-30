package org.thymeleaf.dialect.springdata;

import org.thymeleaf.Arguments;
import org.thymeleaf.dialect.springdata.decorator.PaginationDecoratorFactory;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;

final class PaginationAttrProcessor extends AbstractAttrProcessor {
	private static final String ATTR_NAME = "pagination";
	
	protected PaginationAttrProcessor() {
		super(ATTR_NAME);
	}

	@Override
	protected ProcessorResult processAttribute(Arguments arguments, Element element, String attrName) {
	    final String attributeValue = element.getAttributeValue(attrName);
	    
	    ProcessorUtils.removeAttribute(element, ATTR_NAME);
	    element.clearChildren();
	    
	    PaginationDecoratorFactory.getDecorator(attributeValue, arguments).decorate(element);
	    
		return ProcessorResult.ok();
	}
	
	@Override
	public int getPrecedence() {
		return 1000;
	}

}
