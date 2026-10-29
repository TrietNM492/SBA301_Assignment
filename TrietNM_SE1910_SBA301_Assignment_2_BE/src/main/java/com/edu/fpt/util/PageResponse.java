package com.edu.fpt.util;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageResponse<T> {

    private List<T> content;
    private PaginationMetadata pagination;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.pagination = new PaginationMetadata(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }


    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public PaginationMetadata getPagination() {
        return pagination;
    }

    public void setPagination(PaginationMetadata pagination) {
        this.pagination = pagination;
    }


    public static class PaginationMetadata {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean isFirst;
        private boolean isLast;

        public PaginationMetadata(int pageNumber, int pageSize, long totalElements, int totalPages, boolean isFirst, boolean isLast) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.isFirst = isFirst;
            this.isLast = isLast;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public boolean isIsFirst() {
            return isFirst;
        }

        public boolean isIsLast() {
            return isLast;
        }
    }

}
