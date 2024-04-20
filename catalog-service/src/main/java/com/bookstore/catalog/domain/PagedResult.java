package com.bookstore.catalog.domain;

import java.util.List;

public record PagedResult<T>(
        List<T> data,
        long totalElements,
        int pageNumber,
        int totalPages,
        boolean isfirst,
        boolean islast,
        boolean hasNext,
        boolean hasPrev
){ }
