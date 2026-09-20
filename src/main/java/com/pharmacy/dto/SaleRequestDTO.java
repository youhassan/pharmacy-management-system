package com.pharmacy.dto;

import java.time.LocalDate;
import java.util.List;

public class SaleRequestDTO {
    private LocalDate saleDate;
    private List<SaleItemEntryDTO> items;

    public SaleRequestDTO() {}

    public SaleRequestDTO(LocalDate saleDate, List<SaleItemEntryDTO> items) {
        this.saleDate = saleDate;
        this.items = items;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public List<SaleItemEntryDTO> getItems() {
        return items;
    }

    public void setItems(List<SaleItemEntryDTO> items) {
        this.items = items;
    }
}
