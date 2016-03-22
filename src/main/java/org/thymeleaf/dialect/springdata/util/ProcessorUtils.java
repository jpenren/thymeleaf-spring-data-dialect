package org.thymeleaf.dialect.springdata.util;

import org.thymeleaf.dialect.springdata.SpringDataDialect;
import org.thymeleaf.dom.Element;

public final class ProcessorUtils {
	
	private ProcessorUtils() {
		
	}
	
	public static void removeAttribute(final Element element, final String attributeName){
		element.removeAttribute(attributeName);
		element.removeAttribute(SpringDataDialect.PREFIX.concat(":").concat(attributeName));
	}

}
