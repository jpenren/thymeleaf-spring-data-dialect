package org.thymeleaf.dialect.springdata;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.springdata.decorator.PaginationDecorator;
import org.thymeleaf.dialect.springdata.decorator.PaginationDecoratorRegistry;
import org.thymeleaf.dialect.springdata.util.Strings;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

final class PaginationAttrProcessor extends AbstractAttributeTagProcessor {
    private static final String PAGINATION = "pagination";
    public static final int PRECEDENCE = 1000;

    public PaginationAttrProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, null, false, PAGINATION, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        String attrValue = String.valueOf(attributeValue).trim();
        PaginationDecorator decorator = PaginationDecoratorRegistry.getInstance().getDecorator(attrValue);
        String html = decorator.decorate(tag, context);

        boolean isUlNode = Strings.UL.equalsIgnoreCase(tag.getElementCompleteName());
        if (isUlNode) {
            structureHandler.replaceWith(html, false);
        } else {
            structureHandler.setBody(html, false);
        }
    }

}
