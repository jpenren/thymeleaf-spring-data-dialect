package org.thymeleaf.dialect.springdata.decorator;

import static org.thymeleaf.dialect.springdata.StringPool.A;
import static org.thymeleaf.dialect.springdata.StringPool.ARIA_HIDDEN;
import static org.thymeleaf.dialect.springdata.StringPool.BLANK;
import static org.thymeleaf.dialect.springdata.StringPool.CLASS;
import static org.thymeleaf.dialect.springdata.StringPool.DISABLED;
import static org.thymeleaf.dialect.springdata.StringPool.HREF;
import static org.thymeleaf.dialect.springdata.StringPool.LI;
import static org.thymeleaf.dialect.springdata.StringPool.NEXT;
import static org.thymeleaf.dialect.springdata.StringPool.PAGINATION;
import static org.thymeleaf.dialect.springdata.StringPool.PREVIOUS;
import static org.thymeleaf.dialect.springdata.StringPool.SPAN;
import static org.thymeleaf.dialect.springdata.StringPool.TRUE;
import static org.thymeleaf.dialect.springdata.StringPool.UL;

import org.springframework.data.domain.Page;
import org.thymeleaf.Arguments;
import org.thymeleaf.dialect.springdata.PageUtils;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
import org.thymeleaf.util.MessageResolutionUtils;
import org.unbescape.html.HtmlEscape;

final class PagerDecorator implements PaginationDecorator{
	private static final String LARR = HtmlEscape.unescapeHtml("&larr;");
	private static final String RARR = HtmlEscape.unescapeHtml("&rarr;");
	private static final String PREV_MSG_KEY = "pagination.pager.previous";
	private static final String NEXT_MSG_KEY = "pagination.pager.next";
	
	private Page<?> page;
	private Arguments arguments;
	
	private PagerDecorator(Arguments arguments) {
		this.arguments = arguments;
		this.page = PageUtils.findPage(arguments);
	}
	
	public static PagerDecorator with(Arguments arguments) {
		
		return new PagerDecorator(arguments);
	}
	
	public void decorate(Element element){
		Element ul = element;
		if( !element.getNormalizedName().equals(UL) ){
			ul = new Element(UL);
		    ul.setAttribute(CLASS, PAGINATION);
	    	element.addChild(ul);
		}
    	
    	//previous
		boolean isFirstPage = page.getNumber()==0;
		String prev = LARR.concat(BLANK).concat(getMessage(arguments, PREV_MSG_KEY));
		Element prevLi = isFirstPage ? createTextItem(PREVIOUS, prev) : createLinkItem(PREVIOUS, page.getNumber()-1, prev);
		ul.addChild(prevLi);
		
	    //next
		boolean isLatestPage = page.getNumber()==(page.getTotalPages()-1);
		String next = getMessage(arguments, NEXT_MSG_KEY).concat(BLANK).concat(RARR);
		Element nextLi = isLatestPage ? createTextItem(NEXT, next) : createLinkItem(NEXT, page.getNumber()+1, next);
    	ul.addChild(nextLi);
	}
	
	private String getMessage(Arguments arguments, String key){
		return MessageResolutionUtils.resolveMessageForTemplate(arguments, key, null);
	}
	
	private Element createLinkItem(String clas, int pageNumber, String text){
		Element li = new Element(LI);
		li.setAttribute(CLASS, clas);
		
		Element a = new Element(A);
		li.addChild(a);
		a.setAttribute(HREF, PageUtils.createPageUrl(arguments, pageNumber));
		
		Element span = new Element(SPAN);
		span.setAttribute(ARIA_HIDDEN, TRUE);
		span.addChild(new Text(text));
		a.addChild(span);
		
		return li;
	}
	
	private Element createTextItem(String clas, String text){
		Element li = new Element(LI);
		li.setAttribute(CLASS, clas.concat(BLANK).concat(DISABLED));
		
		Element wrapper = new Element(SPAN);
		Element span = new Element(SPAN);
		span.setAttribute(ARIA_HIDDEN, TRUE);
		span.addChild(new Text(text));
		
		wrapper.addChild(span);
		li.addChild(wrapper);
		
		return li;
	}

}
