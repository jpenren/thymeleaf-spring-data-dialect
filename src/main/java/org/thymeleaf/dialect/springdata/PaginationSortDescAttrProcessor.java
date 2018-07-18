package org.thymeleaf.dialect.springdata;

import org.springframework.data.domain.Sort.Direction;

final class PaginationSortDescAttrProcessor extends PaginationSortBaseAttrProcessor {
    private static final String ATTR_NAME = "pagination-sort-desc";
    public static final int PRECEDENCE = 1000;

    public PaginationSortDescAttrProcessor(final String dialectPrefix) {
        super(dialectPrefix, ATTR_NAME, PRECEDENCE);
    }
    
    protected Direction getForcedDirection() {
        return Direction.DESC;
    }

}
