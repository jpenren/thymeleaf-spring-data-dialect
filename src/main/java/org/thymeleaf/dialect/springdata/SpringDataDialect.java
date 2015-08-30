package org.thymeleaf.dialect.springdata;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractXHTMLEnabledDialect;
import org.thymeleaf.processor.IProcessor;

public class SpringDataDialect extends AbstractXHTMLEnabledDialect{
	public static final String PREFIX="sd";
	
	public String getPrefix() {
		return PREFIX;
	}
	
	@Override
	public Set<IProcessor> getProcessors() {
		final Set<IProcessor> attrProcessors = new HashSet<IProcessor>();
        attrProcessors.add(new PaginationAttrProcessor());
        attrProcessors.add(new PaginationSortAttrProcessor());
        attrProcessors.add(new PaginationSummaryAttrProcessor());
        attrProcessors.add(new PageObjectAttrProcessor());
        attrProcessors.add(new PaginationUrlAttrProcessor());
        attrProcessors.add(new PaginationQualifierAttrProcessor());
        
        return attrProcessors;
	}

}
