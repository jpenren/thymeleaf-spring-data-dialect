package org.thymeleaf.dialect.springdata;

import static org.thymeleaf.dialect.springdata.StringPool.CLASS;
import static org.thymeleaf.dialect.springdata.StringPool.EMPTY;
import static org.thymeleaf.dialect.springdata.StringPool.HREF;
import static org.thymeleaf.dialect.springdata.StringPool.SORTED_PREFIX;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractAttributeModifierAttrProcessor;

class PaginationSortAttrProcessor extends AbstractAttributeModifierAttrProcessor {
	private static final String ATTR_NAME = "pagination-sort";

	protected PaginationSortAttrProcessor() {
		super(ATTR_NAME);
	}

	@Override
	protected Map<String, String> getModifiedAttributeValues(Arguments arguments, Element element, String attributeName) {
		final String attributeValue = element.getAttributeValue(attributeName);
        final Page<?> page = PageUtils.findPage(arguments);
        final String url = PageUtils.createSortUrl(arguments, attributeValue);
        
        ProcessorUtils.removeAttribute(element, ATTR_NAME);
        
        //Append class to the element if sorted by this field
        Sort sort = page.getSort();
        final boolean isSorted = sort!=null && sort.getOrderFor(attributeValue)!=null;
        final String clas = isSorted ? SORTED_PREFIX.concat(sort.getOrderFor(attributeValue).getDirection().toString().toLowerCase()) : EMPTY;
        
        final Map<String, String> modifiedAttributes = new HashMap<String, String>();
        modifiedAttributes.put(HREF, url);
        modifiedAttributes.put(CLASS, clas);
        
		return modifiedAttributes;
	}
	
	@Override
	protected ModificationType getModificationType(Arguments arguments, Element element, String attributeName, String newAttributeName) {
		return CLASS.equals(newAttributeName) ? ModificationType.APPEND_WITH_SPACE : ModificationType.SUBSTITUTION;
	}

	@Override
	protected boolean removeAttributeIfEmpty(Arguments arguments, Element element, String attributeName, String newAttributeName) {
		return true;
	}

	@Override
	protected boolean recomputeProcessorsAfterExecution(Arguments arguments, Element element, String attributeName) {
		return false;
	}

	@Override
	public int getPrecedence() {
		return 1000;
	}

}
