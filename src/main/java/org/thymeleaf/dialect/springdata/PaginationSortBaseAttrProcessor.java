package org.thymeleaf.dialect.springdata;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.springdata.util.Expressions;
import org.thymeleaf.dialect.springdata.util.PageUtils;
import org.thymeleaf.dialect.springdata.util.Strings;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import static org.thymeleaf.dialect.springdata.util.Strings.*;

abstract class PaginationSortBaseAttrProcessor extends AbstractAttributeTagProcessor {
    public PaginationSortBaseAttrProcessor(final String dialectPrefix, final String attrName, final int precedence) {
        super(TemplateMode.HTML, dialectPrefix, null, false, attrName, true, precedence, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        String attrValue = String.valueOf(Expressions.evaluate(context, attributeValue)).trim();
        Page<?> page = PageUtils.findPage(context);
        String url = PageUtils.createSortUrl(context, attrValue, getForcedDirection());

        // Append class to the element if sorted by this field
        Sort sort = page.getSort();
        boolean isSorted = sort != null && sort.getOrderFor(attrValue) != null;
        String clas = isSorted
                ? SORTED_PREFIX.concat(sort.getOrderFor(attrValue).getDirection().toString().toLowerCase())
                : EMPTY;

        structureHandler.setAttribute(HREF, url);
        String currentClass = tag.getAttributeValue(CLASS);
        structureHandler.setAttribute(CLASS, Strings.concat(currentClass, BLANK, clas));
    }
    
    /**
     * Optional "forced" sort direction, if sorting in only one direction is allowed.
     * @return null if sorting in either direction is allowed, otherwise specific direction
     */
    protected abstract Direction getForcedDirection();
}
