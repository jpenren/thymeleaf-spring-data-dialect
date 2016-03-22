package org.thymeleaf.dialect.springdata.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dialect.springdata.Keys;
import org.thymeleaf.dialect.springdata.exception.InvalidObjectParameterException;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

import static org.thymeleaf.dialect.springdata.util.Strings.*;

@SuppressWarnings("unchecked")
public final class PageUtils {
	
	private PageUtils() {
		
	}
	
	public static int getFirstItemInPage(final Page<?> page){
		return page.getSize()*page.getNumber() + 1 ;
	}
	
	public static int getLatestItemInPage(final Page<?> page){
		return page.getSize()*page.getNumber() + page.getNumberOfElements();
	}
	
	public static Page<?> findPage(final Arguments arguments){
		//1. Get Page object from local variables (defined with sd:page-object)
		//2. Search Page using ${page} expression
		//3. Search Page object as request attribute 
		
		final Object pageFromLocalVariables = arguments.getLocalVariable(Keys.PAGE_VARIABLE_KEY);
		if( isPageInstance(pageFromLocalVariables) ){
			return (Page<?>) pageFromLocalVariables;
		}
		
		//Check if not null and Page instance available with ${page} expression
		final Configuration configuration = arguments.getConfiguration();
		final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
		final IStandardExpression expression = parser.parseExpression(configuration, arguments, Keys.PAGE_EXPRESSION);
	    final Object page = expression.execute(configuration, arguments);
	    if( isPageInstance(page) ){
	    	return (Page<?>) page;
	    }
	    
	    //Search for Page object, and only one instance, as request attribute
	    final IContext context = arguments.getContext();
	    if( context instanceof IWebContext){
	    	HttpServletRequest request = ((IWebContext) context).getHttpServletRequest();
	    	Enumeration<String> attrNames = request.getAttributeNames();
	    	Page<?> pageOnRequest = null;
	    	while (attrNames.hasMoreElements()) {
				String attrName = (String) attrNames.nextElement();
				Object attr = request.getAttribute(attrName);
				if( isPageInstance(attr) ){
					if( pageOnRequest!=null ){
						throw new InvalidObjectParameterException("More than one Page object found on request!");
					}
					
					pageOnRequest = (Page<?>) attr;
				}
			}
	    	
	    	if( pageOnRequest!=null ){
	    		return pageOnRequest;
	    	}
	    }
	    
	    throw new InvalidObjectParameterException("Invalid or not present Page object found on request!");
	}
	
	public static String createPageUrl(final Arguments arguments, int pageNumber){
		String prefix = getParamPrefix(arguments);
		final Collection<String> excludedParams = Arrays.asList(new String[]{prefix.concat(PAGE)});
		final String baseUrl = buildBaseUrl(arguments, excludedParams);
		
		return buildUrl(baseUrl, arguments).append(PAGE).append(EQ).append(pageNumber).toString();
	}
	
	/**
	 * Creates an url to sort data by fieldName
	 * @param arguments execution context
	 * @param fieldName sort field name
	 * @return sort url link
	 */
	public static String createSortUrl(final Arguments arguments, final String fieldName){
		//Params can be prefixed to manage multiple pagination on the same page
		String prefix = getParamPrefix(arguments);
		final Collection<String> excludedParams = Arrays.asList(new String[]{prefix.concat(SORT), prefix.concat(PAGE)});
		final String baseUrl = buildBaseUrl(arguments, excludedParams);
		
		final StringBuilder sortParam = new StringBuilder();
		Page<?> page = findPage(arguments);
		Sort sort = page.getSort();
		boolean hasPreviousOrder = sort!=null && sort.getOrderFor(fieldName)!=null;
		if( hasPreviousOrder ){
			//Sort parameters exists for this field, modify direction
			Order previousOrder = sort.getOrderFor(fieldName);
			Direction dir = previousOrder.isAscending() ? Direction.DESC : Direction.ASC;
			sortParam.append(fieldName).append(COMMA).append(dir.toString().toLowerCase());
		}else{
			sortParam.append(fieldName);
		}
		
		return buildUrl(baseUrl, arguments).append(SORT).append(EQ).append(sortParam).toString();
	}

	private static String buildBaseUrl(final Arguments arguments, Collection<String> excludeParams){
		//URL defined with pagination-url tag
        final String url = (String) arguments.getLocalVariable(Keys.PAGINATION_URL_KEY);
        
		final IContext context = arguments.getContext();
		if( url==null && context instanceof IWebContext){
			//Creates url from actual request URI and parameters
			final StringBuilder builder = new StringBuilder();
			final IWebContext webContext = (IWebContext) context;
	        final HttpServletRequest request = webContext.getHttpServletRequest();
	        
        	//URL base path from request
        	builder.append(request.getRequestURI());
        
	        Map<String, String[]> params = request.getParameterMap();
	        Set<Entry<String, String[]>> entries = params.entrySet();
	        boolean firstParam = true;
	        for (Entry<String, String[]> param : entries) {
	        	//Append params not excluded to basePath
	        	String name = param.getKey();
	        	if( !excludeParams.contains(name) ){
	        		if(firstParam){
		        		builder.append(Q_MARK);
		        		firstParam=false;
		        	}else{
		        		builder.append(AND);
		        	}
	        		
	        		//Iterate over all values to create multiple values for parameter
	        		String[] values = param.getValue();
	        		Collection<String> paramValues = Arrays.asList(values);
	        		Iterator<String> it = paramValues.iterator();
	        		while ( it.hasNext() ) {
						String value = it.next();
						builder.append(name).append(EQ).append(value);
						if( it.hasNext() ){
							builder.append(AND);
						}
					}
	        	}
			}

	        return builder.toString();
		}
		
		return url==null ? EMPTY : url;
	}
	
	
	
	private static boolean isPageInstance(Object page){
		return page!=null && (page instanceof Page<?>);
	}
	
	private static StringBuilder buildUrl(String baseUrl, Arguments arguments){
		String paramAppender = String.valueOf(baseUrl).contains(Q_MARK) ? AND : Q_MARK;
		String prefix = getParamPrefix(arguments);
		
		return new StringBuilder(baseUrl).append(paramAppender).append(prefix);
	}
	
	private static String getParamPrefix(Arguments arguments){
		String prefix = (String) arguments.getLocalVariable(Keys.PAGINATION_QUALIFIER_PREFIX);
		
		return prefix==null ? EMPTY : prefix.concat("_");
	}
	
}
