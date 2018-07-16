package org.thymeleaf.dialect.springdata;

import org.springframework.data.domain.Sort.Direction;

final class PaginationSortAscAttrProcessor extends PaginationSortBaseAttrProcessor {
    private static final String ATTR_NAME = "pagination-sort-asc";
    public static final int PRECEDENCE = 1000;

    public PaginationSortAscAttrProcessor(final String dialectPrefix) {
        super(dialectPrefix, ATTR_NAME, PRECEDENCE);
    }
    
    protected Direction getForcedDirection() {
        return Direction.ASC;
    }
}
