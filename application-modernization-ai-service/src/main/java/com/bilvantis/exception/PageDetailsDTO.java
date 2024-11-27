package com.bilvantis.exception;

import lombok.Data;

@Data
public class PageDetailsDTO {
    private Integer pageNo;
    private Integer totalPages;
    private Integer perPage;
    private String sortingOrder;
}