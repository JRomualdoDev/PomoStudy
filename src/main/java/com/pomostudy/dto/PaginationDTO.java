package com.pomostudy.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class PaginationDTO<T> {

    private final List<T> content;
    private final int totalPages;
    private final long totalElements;
    private final int pageNumber;
    private final int pageSize;

    public PaginationDTO(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
    }

    public List<T> getContent() {
        return content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }
}
