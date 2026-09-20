package com.pharmacy.dto;

import java.time.LocalDate;
import java.util.List;

public class PurchaseRequestDTO {
    private String companyName;
    private LocalDate purDate;
    private List<PurchaseItemEntryDTO> items;

    public PurchaseRequestDTO() {
    }

    public PurchaseRequestDTO(String companyName, LocalDate purDate, List<PurchaseItemEntryDTO> items) {
        this.companyName = companyName;
        this.purDate = purDate;
        this.items = items;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDate getPurDate() {
        return purDate;
    }

    public void setPurDate(LocalDate purDate) {
        this.purDate = purDate;
    }

    public List<PurchaseItemEntryDTO> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItemEntryDTO> items) {
        this.items = items;
    }
}
