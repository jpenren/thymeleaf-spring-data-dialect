package org.thymeleaf.dialect.springdata;

import java.util.Locale;

import org.springframework.data.domain.Page;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.springdata.util.Messages;
import org.thymeleaf.dialect.springdata.util.PageUtils;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

final class PageSizeSelectorAttrProcessor extends AbstractAttributeTagProcessor {
	private static final String DEFAULT_MESSAGE_KEY = "page.size.selector";
	private static final String ATTR_NAME = "page-size-selector";
	public static final int PRECEDENCE = 900;
	private static final String BUNDLE_NAME = "PageSizeSelector";
	private int[] selectorValues = new int[]{10, 20, 50, 100};
	
	protected PageSizeSelectorAttrProcessor(final String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
	}

	@Override
	protected void doProcess(ITemplateContext context,
			IProcessableElementTag tag, AttributeName attributeName,
			String attributeValue, IElementTagStructureHandler structureHandler) {
		
		Locale locale = context.getLocale();
		Page<?> page = PageUtils.findPage(context);
		int pageSize = page.getSize();
		String attrValue = String.valueOf(attributeValue).trim();
		String options = composeSelectorOptions(pageSize);
		String message = Messages.getMessage(BUNDLE_NAME, DEFAULT_MESSAGE_KEY, locale, options);

		structureHandler.setBody(message, false);
	}
	
	private String composeSelectorOptions(int selectedValue){
		StringBuilder sb = new StringBuilder();
		for (int value : selectorValues) {
			sb.append("<option value=\"").append(value).append("\"");
			if(value==selectedValue){
				sb.append(" selected=\"selected\"");
			}
			sb.append(">").append(value).append("</option>");
		}
		
		return sb.toString();
	}
	
}
