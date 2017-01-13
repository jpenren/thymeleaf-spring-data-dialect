package org.thymeleaf.dialect.springdata;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.springdata.util.PageUtils;
import org.thymeleaf.dialect.springdata.util.Strings;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import static org.thymeleaf.dialect.springdata.util.Strings.*;

final class PaginationSortAttrProcessor extends AbstractAttributeTagProcessor {
    private static final String ATTR_NAME = "pagination-sort";
    public static final int PRECEDENCE = 1000;

    public PaginationSortAttrProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        String attrValue = String.valueOf(attributeValue).trim();
        Page<?> page = PageUtils.findPage(context);
        String url = PageUtils.createSortUrl(context, attrValue);

        // Append class to the element if sorted by this field
        Sort sort = page.getSort();
        boolean isSorted = sort != null && sort.getOrderFor(attributeValue) != null;
        String clas = isSorted
                ? SORTED_PREFIX.concat(sort.getOrderFor(attributeValue).getDirection().toString().toLowerCase())
                : EMPTY;

        structureHandler.setAttribute(HREF, url);
        String currentClass = tag.getAttributeValue(CLASS);
        structureHandler.setAttribute(CLASS, Strings.concat(currentClass, BLANK, clas));
    }

}
