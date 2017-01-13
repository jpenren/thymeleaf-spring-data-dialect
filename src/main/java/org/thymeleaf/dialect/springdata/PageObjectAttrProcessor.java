package org.thymeleaf.dialect.springdata;

import org.springframework.data.domain.Page;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.dialect.springdata.exception.InvalidObjectParameterException;
import org.thymeleaf.dialect.springdata.util.Expressions;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

final class PageObjectAttrProcessor extends AbstractAttributeTagProcessor {
    private static final String ATTR_NAME = "page-object";
    public static final int PRECEDENCE = 900;

    protected PageObjectAttrProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        if (context instanceof WebEngineContext) {
            Object page = Expressions.evaluate(context, attributeValue);

            if (!(page instanceof Page<?>)) {
                throw new InvalidObjectParameterException(
                        "Parameter " + attributeValue + " is not an Page<?> instance!");
            }

            ((WebEngineContext) context).setVariable(Keys.PAGE_VARIABLE_KEY, page);
        }
    }

}
