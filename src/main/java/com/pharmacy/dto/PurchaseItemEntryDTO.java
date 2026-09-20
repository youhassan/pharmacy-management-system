package com.pharmacy.dto;

import java.time.LocalDate;

public class PurchaseItemEntryDTO {
    private String medicineName;
    private int quantityPur;
    private double unitCost;
    private boolean isNewMedicine;
    private LocalDate expiryDate;
    private int minimumStock;

    public PurchaseItemEntryDTO() {
    }

    public PurchaseItemEntryDTO(String medicineName, int quantityPur, double unitCost) {
        this.medicineName = medicineName;
        this.quantityPur = quantityPur;
        this.unitCost = unitCost;
        this.isNewMedicine = false;
    }

    public PurchaseItemEntryDTO(String medicineName, int quantityPur, double unitCost, LocalDate expiryDate, int minimumStock) {
        this.medicineName = medicineName;
        this.quantityPur = quantityPur;
        this.unitCost = unitCost;
        this.isNewMedicine = true;
        this.expiryDate = expiryDate;
        this.minimumStock = minimumStock;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getQuantityPur() {
        return quantityPur;
    }

    public void setQuantityPur(int quantityPur) {
        this.quantityPur = quantityPur;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public boolean isNewMedicine() {
        return isNewMedicine;
    }

    public void setNewMedicine(boolean newMedicine) {
        isNewMedicine = newMedicine;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }
}
