package org.thymeleaf.dialect.springdata;

import org.thymeleaf.Arguments;
import org.thymeleaf.dialect.springdata.decorator.PaginationDecorator;
import org.thymeleaf.dialect.springdata.decorator.PaginationDecoratorRegistry;
import org.thymeleaf.dialect.springdata.util.ProcessorUtils;
import org.thymeleaf.dialect.springdata.util.Strings;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
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
	    
	    PaginationDecorator decorator = PaginationDecoratorRegistry.getInstance().getDecorator(attributeValue);
	    String content = decorator.decorate(element, arguments);
	    final Text newNode = new Text(Strings.EMPTY);
    	newNode.setContent(content, true);
    	
	    String elementName = element.getNormalizedName();
	    boolean isUl = Strings.UL.equals(elementName);
	    if( isUl ){
	    	element.getParent().addChild(newNode);
	    	element.getParent().removeChild(element);
	    }else{
	    	element.addChild(newNode);
	    }
	    
		return ProcessorResult.ok();
	}
	
	@Override
	public int getPrecedence() {
		return 1000;
	}	
	
}
