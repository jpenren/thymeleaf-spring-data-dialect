package org.thymeleaf.dialect.springdata;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.dialect.springdata.util.Expressions;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

final class PaginationSplitAttrProcessor extends AbstractAttributeTagProcessor {
    private static final String ATTR_NAME = "pagination-split";
    public static final int PRECEDENCE = 900;

    protected PaginationSplitAttrProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        if (context instanceof WebEngineContext) {
            Number split = (Number) Expressions.evaluate(context, attributeValue);
            ((WebEngineContext) context).setVariable(Keys.PAGINATION_SPLIT_KEY, split);
        }
    }

}
