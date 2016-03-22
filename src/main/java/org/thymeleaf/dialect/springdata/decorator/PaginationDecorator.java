package org.thymeleaf.dialect.springdata.decorator;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;


public interface PaginationDecorator {
	
	String getIdentifier();
	String decorate(Element element, Arguments arguments);
	
}
