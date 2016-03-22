package org.thymeleaf.dialect.springdata;

import java.util.Collections;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dialect.springdata.exception.InvalidObjectParameterException;
import org.thymeleaf.dialect.springdata.util.ProcessorUtils;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

final class PageObjectAttrProcessor extends AbstractAttrProcessor {
	private static final String ATTR_NAME = "page-object";
	
	protected PageObjectAttrProcessor() {
		super(ATTR_NAME);
	}

	@Override
	protected ProcessorResult processAttribute(Arguments arguments, Element element, String attrName) {
		final Configuration configuration = arguments.getConfiguration();
	    final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
	    final String attributeValue = element.getAttributeValue(attrName);
	    final IStandardExpression expression = parser.parseExpression(configuration, arguments, attributeValue);
	    
	    ProcessorUtils.removeAttribute(element, ATTR_NAME);
	    
	    Object page = expression.execute(configuration, arguments);
	    if( page==null || !(page instanceof Page<?>)){
	    	throw new InvalidObjectParameterException("No Page<?> object found with page-object parameter: "+attributeValue);
	    }
	    
		Map<String, Object> vars = Collections.<String, Object>singletonMap(Keys.PAGE_VARIABLE_KEY, page);
		
		return ProcessorResult.setLocalVariables(vars);
	}
	
	@Override
	public int getPrecedence() {
		return 900;
	}

}
