package com.pharmacy.dto;

public class SaleItemEntryDTO {
    private String medicineName;
    private int quantitySold;
    private double unitPrice;

    public SaleItemEntryDTO() {
    }

    public SaleItemEntryDTO(String medicineName, int quantitySold, double unitPrice) {
        this.medicineName = medicineName;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
