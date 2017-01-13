package org.thymeleaf.dialect.springdata;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

final class PaginationQualifierAttrProcessor extends AbstractAttributeTagProcessor {
    private static final String ATTR_NAME = "pagination-qualifier";
    public static final int PRECEDENCE = 900;

    protected PaginationQualifierAttrProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        if (context instanceof WebEngineContext) {
            String attrValue = String.valueOf(attributeValue).trim();
            ((WebEngineContext) context).setVariable(Keys.PAGINATION_QUALIFIER_PREFIX, attrValue);
        }
    }

}
