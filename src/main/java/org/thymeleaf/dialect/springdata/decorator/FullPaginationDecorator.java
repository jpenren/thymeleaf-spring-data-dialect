package org.thymeleaf.dialect.springdata.decorator;

import static org.thymeleaf.dialect.springdata.StringPool.A;
import static org.thymeleaf.dialect.springdata.StringPool.ACTIVE;
import static org.thymeleaf.dialect.springdata.StringPool.CLASS;
import static org.thymeleaf.dialect.springdata.StringPool.DISABLED;
import static org.thymeleaf.dialect.springdata.StringPool.HREF;
import static org.thymeleaf.dialect.springdata.StringPool.LI;
import static org.thymeleaf.dialect.springdata.StringPool.PAGINATION;
import static org.thymeleaf.dialect.springdata.StringPool.SPAN;
import static org.thymeleaf.dialect.springdata.StringPool.UL;

import org.springframework.data.domain.Page;
import org.thymeleaf.Arguments;
import org.thymeleaf.dialect.springdata.PageUtils;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
import org.unbescape.html.HtmlEscape;

final class FullPaginationDecorator implements PaginationDecorator{
	private static final String LAQUO = HtmlEscape.unescapeHtml("&laquo;");
	private static final String RAQUO = HtmlEscape.unescapeHtml("&raquo;");
	private Page<?> page;
	private Arguments arguments;
	
	private FullPaginationDecorator(Arguments arguments) {
		this.arguments = arguments;
		this.page = PageUtils.findPage(arguments);
	}
	
	public static FullPaginationDecorator with(Arguments arguments) {
		return new FullPaginationDecorator(arguments);
	}
	
	public void decorate(Element element){
		Element ul = element;
		if( !element.getNormalizedName().equals(UL) ){
			ul = new Element(UL);
		    ul.setAttribute(CLASS, PAGINATION);
	    	element.addChild(ul);
		}
    	
    	//laquo
	    String firstPage = PageUtils.createPageUrl(arguments, 0);
    	boolean isFirstPage = page.getNumber()==0;
    	Element laquo = isFirstPage ? createDisabledItem(LAQUO) : createLinkItem(firstPage, LAQUO);
    	ul.addChild(laquo);
    	
    	//Pages
		int currentPage = page.getNumber();
		int totalPages = page.getTotalPages();
	    for (int i = 0; i < totalPages; i++) {
	    	String pageNumber = String.valueOf(i+1);
	    	String link = PageUtils.createPageUrl(arguments, i);
	    	boolean isCurrentPage = i==currentPage;
	    	Element li = isCurrentPage ? createActiveItem(pageNumber) : createLinkItem(link, pageNumber);
		    ul.addChild(li);
		}
	    
	    //raquo
	    boolean isLastPage = page.getTotalPages()==0 || page.getNumber()==(page.getTotalPages()-1);
	    String lastPage = PageUtils.createPageUrl(arguments, page.getTotalPages()-1);
    	Element raquo = isLastPage ? createDisabledItem(RAQUO) : createLinkItem(lastPage, RAQUO);
    	ul.addChild(raquo);
	}

	public static Element createLinkItem(String url, String text){
		Element li = new Element(LI);
    	Element a = new Element(A);
    	a.setAttribute(HREF, url);
    	Element span = new Element(SPAN);
    	span.addChild(new Text(text));
    	a.addChild(span);
    	li.addChild(a);
    	
    	return li;
	}
	
	private Element createDisabledItem(String text){
		Element li = createTextItem(text);
		li.setAttribute(CLASS, DISABLED);
    	
    	return li;
	}
	
	private Element createActiveItem(String text) {
		Element li = createTextItem(text);
		li.setAttribute(CLASS, ACTIVE);
    	
    	return li;
	}
	
	private Element createTextItem(String text){
		Element li = new Element(LI);
    	Element wrapper = new Element(SPAN);
    	Element span = new Element(SPAN);
    	span.addChild(new Text(text));
    	wrapper.addChild(span);
    	li.addChild(wrapper);
    	
    	return li;
	}
	
}
