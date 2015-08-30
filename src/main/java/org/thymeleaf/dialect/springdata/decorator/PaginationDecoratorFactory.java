package org.thymeleaf.dialect.springdata.decorator;

import org.thymeleaf.Arguments;

public final class PaginationDecoratorFactory {

	private PaginationDecoratorFactory() {
		
	}
	
	public static PaginationDecorator getDecorator(final String identifier, final Arguments arguments){
		if( "full".equals(identifier) ){
			return FullPaginationDecorator.with(arguments);
		}
		
		if( "pager".equals(identifier) ){
			return PagerDecorator.with(arguments);
		}
		
		throw new RuntimeException("Not implemented");
	}
	
}
