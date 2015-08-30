package org.thymeleaf.dialect.springdata;

import org.thymeleaf.dom.Element;

final class ProcessorUtils {
	
	private ProcessorUtils() {
		
	}
	
	public static void removeAttribute(final Element element, final String attributeName){
		element.removeAttribute(attributeName);
		element.removeAttribute(SpringDataDialect.PREFIX.concat(":").concat(attributeName));
	}

}
