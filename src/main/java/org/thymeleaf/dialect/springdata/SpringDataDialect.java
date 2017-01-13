package org.thymeleaf.dialect.springdata;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

public final class SpringDataDialect implements IProcessorDialect {
    public static final String PREFIX = "sd";
    public static final String NAME = "SpringDataDialect";
    public static final int PRECEDENCE = 1000;

    public String getName() {
        return NAME;
    }

    public String getPrefix() {
        return PREFIX;
    }

    public int getDialectProcessorPrecedence() {
        return PRECEDENCE;
    }

    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, PREFIX));
        processors.add(new PaginationAttrProcessor(PREFIX));
        processors.add(new PaginationSortAttrProcessor(PREFIX));
        processors.add(new PaginationSummaryAttrProcessor(PREFIX));
        processors.add(new PageObjectAttrProcessor(PREFIX));
        processors.add(new PaginationUrlAttrProcessor(PREFIX));
        processors.add(new PaginationQualifierAttrProcessor(PREFIX));
        processors.add(new PaginationSplitAttrProcessor(PREFIX));
        processors.add(new PageSizeSelectorAttrProcessor(PREFIX));

        return processors;
    }

}
