package model;

public class SaleItem {
    private int itemID;
    private int saleID;
    private int medicineID;
    private int quantitySold;
    private double unitPrice;

    public SaleItem(int itemID, int saleID, int medicineID, int quantitySold, double unitPrice)
    {
        this.itemID = itemID;
        this.saleID = saleID;
        this.medicineID = medicineID;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
    }

    public int  getItemID() {return this.itemID;}
    public void setItemID(int itemID) {this.itemID = itemID;}

    public int getSaleID() {return this.saleID;}
    public void setSaleID(int saleID) {this.saleID = saleID;}

    public int getMedicineID() {return this.medicineID;}
    public void setMedicineID(int medicineID) {this.medicineID = medicineID;}

    public int getQuantitySold() {return this.quantitySold;}
    public void setQuantitySold(int quantitySold) {this.quantitySold = quantitySold;}

    public double getUnitPrice() {return this.unitPrice;}
    public void setUnitPrice(double unitPrice) {this.unitPrice = unitPrice;}
}
